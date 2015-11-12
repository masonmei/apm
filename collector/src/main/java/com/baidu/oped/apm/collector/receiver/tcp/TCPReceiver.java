
package com.baidu.oped.apm.collector.receiver.tcp;

import com.baidu.oped.apm.collector.config.MxCollectorProperties;
import com.baidu.oped.apm.collector.config.TcpConfig;
import com.baidu.oped.apm.collector.receiver.DispatchHandler;
import com.baidu.oped.apm.collector.receiver.TcpDispatchHandler;
import com.baidu.oped.apm.collector.rpc.handler.AgentEventHandler;
import com.baidu.oped.apm.collector.rpc.handler.AgentLifeCycleHandler;
import com.baidu.oped.apm.collector.util.PacketUtils;
import com.baidu.oped.apm.common.util.AgentEventType;
import com.baidu.oped.apm.common.util.AgentLifeCycleState;
import com.baidu.oped.apm.common.util.ExecutorFactory;
import com.baidu.oped.apm.common.util.PinpointThreadFactory;
import com.baidu.oped.apm.rpc.PinpointSocket;
import com.baidu.oped.apm.rpc.packet.*;
import com.baidu.oped.apm.rpc.server.PinpointServer;
import com.baidu.oped.apm.rpc.server.PinpointServerAcceptor;
import com.baidu.oped.apm.rpc.server.ServerMessageListener;
import com.baidu.oped.apm.rpc.server.handler.ServerStateChangeEventHandler;
import com.baidu.oped.apm.rpc.util.MapUtils;
import com.baidu.oped.apm.thrift.io.*;
import com.baidu.oped.apm.thrift.util.SerializationUtils;
import org.apache.thrift.TBase;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * class TCPReceiver
 *
 * @author meidongxu@baidu.com
 */
@Component
public class TCPReceiver {

    private final Logger logger = LoggerFactory.getLogger(TCPReceiver.class);

    private final ThreadFactory tcpWorkerThreadFactory = new PinpointThreadFactory("Pinpoint-TCP-Worker");
    private final DispatchHandler dispatchHandler;
    private final PinpointServerAcceptor serverAcceptor;

    private final String bindAddress;
    private final int port;
    private final List<String> l4ipList;

    private final ThreadPoolExecutor worker;

    private final SerializerFactory<HeaderTBaseSerializer> serializerFactory =
            new ThreadLocalHeaderTBaseSerializerFactory<>(new HeaderTBaseSerializerFactory(true,
                    HeaderTBaseSerializerFactory.DEFAULT_UDP_STREAM_MAX_SIZE));
    private final DeserializerFactory<HeaderTBaseDeserializer> deserializerFactory =
            new ThreadLocalHeaderTBaseDeserializerFactory<>(new HeaderTBaseDeserializerFactory());


    @Autowired
    @Qualifier(value = "agentEventWorker")
    private ExecutorService agentEventWorker;

    @Autowired
    private AgentEventHandler agentEventHandler;

    @Autowired
    private AgentLifeCycleHandler agentLifeCycleHandler;

//    @Resource(name = "channelStateChangeEventHandlers")
    private List<ServerStateChangeEventHandler> channelStateChangeEventHandlers = Collections.emptyList();


    @Autowired
    public TCPReceiver(TcpConfig configuration, TcpDispatchHandler dispatchHandler) {
        this(configuration, dispatchHandler, new PinpointServerAcceptor());
    }

    public TCPReceiver(TcpConfig configuration, DispatchHandler dispatchHandler,
                       PinpointServerAcceptor serverAcceptor) {
        if (configuration == null) {
            throw new NullPointerException("collector configuration must not be null");
        }

        if (dispatchHandler == null) {
            throw new NullPointerException("dispatchHandler must not be null");
        }

        this.dispatchHandler = dispatchHandler;
        this.bindAddress = configuration.getListenIp();
        this.port = configuration.getListenPort();
        this.l4ipList = configuration.getL4IpList();
        this.worker = ExecutorFactory.newFixedThreadPool(
                configuration.getWorkerThread(), configuration.getWorkerQueueSize(), tcpWorkerThreadFactory);
        this.serverAcceptor = serverAcceptor;
    }

    private void setL4TcpChannel(PinpointServerAcceptor serverAcceptor) {
        if (l4ipList == null) {
            return;
        }
        try {
            List<InetAddress> inetAddressList = new ArrayList<>();
            for (String l4Ip : l4ipList) {
                if (StringUtils.isEmpty(l4Ip)) {
                    continue;
                }

                InetAddress address = InetAddress.getByName(l4Ip);
                if (address != null) {
                    inetAddressList.add(address);
                }
            }

            InetAddress[] inetAddressArray = new InetAddress[inetAddressList.size()];
            serverAcceptor.setIgnoreAddressList(inetAddressList.toArray(inetAddressArray));
        } catch (UnknownHostException e) {
            logger.warn("l4ipList error {}", l4ipList, e);
        }
    }

    @PostConstruct
    public void start() {
        this.channelStateChangeEventHandlers.forEach(serverAcceptor::addStateChangeEventHandler);

        setL4TcpChannel(serverAcceptor);
        // take care when attaching message handlers as events are generated from the IO thread.
        // pass them to a separate queue and handle them in a different thread.
        this.serverAcceptor.setMessageListener(new ServerMessageListener() {
            @Override
            public HandshakeResponseCode handleHandshake(Map properties) {
                if (properties == null) {
                    return HandshakeResponseType.ProtocolError.PROTOCOL_ERROR;
                }

                boolean hasAllType = AgentHandshakePropertyType.hasAllType(properties);
                if (!hasAllType) {
                    return HandshakeResponseType.PropertyError.PROPERTY_ERROR;
                }

                boolean supportServer =
                        MapUtils.getBoolean(properties, AgentHandshakePropertyType.SUPPORT_SERVER.getName(), true);
                if (supportServer) {
                    return HandshakeResponseType.Success.DUPLEX_COMMUNICATION;
                } else {
                    return HandshakeResponseType.Success.SIMPLEX_COMMUNICATION;
                }
            }

            @Override
            public void handleSend(SendPacket sendPacket, PinpointSocket channel) {
                receive(sendPacket, channel);
            }

            @Override
            public void handleRequest(RequestPacket requestPacket, PinpointSocket channel) {
                requestResponse(requestPacket, channel);
            }

            @Override
            public void handlePing(PingPacket pingPacket, PinpointServer pinpointServer) {
                recordPing(pingPacket, pinpointServer);
            }
        });
        this.serverAcceptor.bind(bindAddress, port);

    }

    private void recordPing(PingPacket pingPacket, PinpointServer pinpointServer) {
        final int eventCounter = pingPacket.getPingId();
        long pingTimestamp = System.currentTimeMillis();
        try {
            if (!(eventCounter < 0)) {
                agentLifeCycleHandler.handleLifeCycleEvent(pinpointServer, pingTimestamp, AgentLifeCycleState.RUNNING, eventCounter);
            }
            agentEventHandler.handleEvent(pinpointServer, pingTimestamp, AgentEventType.AGENT_PING);
        } catch (Exception e) {
            logger.warn("Error handling ping event", e);
        }
    }

    private void receive(SendPacket sendPacket, PinpointSocket channel) {
        try {
            worker.execute(new Dispatch(sendPacket.getPayload(), channel.getRemoteAddress()));
        } catch (RejectedExecutionException e) {
            // cause is clear - full stack trace not necessary 
            logger.warn("RejectedExecutionException Caused:{}", e.getMessage());
        }
    }

    private void requestResponse(RequestPacket requestPacket, PinpointSocket pinpointSocket) {
        try {
            worker.execute(new RequestResponseDispatch(requestPacket, pinpointSocket));
        } catch (RejectedExecutionException e) {
            // cause is clear - full stack trace not necessary
            logger.warn("RejectedExecutionException Caused:{}", e.getMessage());
        }

    }

    @PreDestroy
    public void stop() {
        logger.info("Pinpoint-TCP-Server stop");
        serverAcceptor.close();
        shutdownExecutor(worker);
        shutdownExecutor(agentEventWorker);
    }

    private void shutdownExecutor(ExecutorService executor) {
        if (executor == null) {
            return;
        }
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private class Dispatch implements Runnable {
        private final byte[] bytes;
        private final SocketAddress remoteAddress;

        private Dispatch(byte[] bytes, SocketAddress remoteAddress) {
            if (bytes == null) {
                throw new NullPointerException("bytes");
            }
            this.bytes = bytes;
            this.remoteAddress = remoteAddress;
        }

        @Override
        public void run() {
            try {
                TBase<?, ?> tBase = SerializationUtils.deserialize(bytes, deserializerFactory);
                dispatchHandler.dispatchSendMessage(tBase);
            } catch (TException e) {
                if (logger.isWarnEnabled()) {
                    logger.warn("packet serialize error. SendSocketAddress:{} Cause:{}", remoteAddress, e.getMessage(),
                            e);
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("packet dump hex:{}", PacketUtils.dumpByteArray(bytes));
                }
            } catch (Exception e) {
                // there are cases where invalid headers are received
                if (logger.isWarnEnabled()) {
                    logger.warn("Unexpected error. SendSocketAddress:{} Cause:{}", remoteAddress, e.getMessage(), e);
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("packet dump hex:{}", PacketUtils.dumpByteArray(bytes));
                }
            }
        }

    }

    private class RequestResponseDispatch implements Runnable {
        private final RequestPacket requestPacket;
        private final PinpointSocket pinpointSocket;

        private RequestResponseDispatch(RequestPacket requestPacket, PinpointSocket pinpointSocket) {
            if (requestPacket == null) {
                throw new NullPointerException("requestPacket");
            }
            this.requestPacket = requestPacket;
            this.pinpointSocket = pinpointSocket;
        }

        @Override
        public void run() {

            byte[] bytes = requestPacket.getPayload();
            SocketAddress remoteAddress = pinpointSocket.getRemoteAddress();
            try {
                TBase<?, ?> tBase = SerializationUtils.deserialize(bytes, deserializerFactory);
                if (tBase instanceof L4Packet) {
                    if (logger.isDebugEnabled()) {
                        L4Packet packet = (L4Packet) tBase;
                        logger.debug("tcp l4 packet {}", packet.getHeader());
                    }
                    return;
                }
                TBase result = dispatchHandler.dispatchRequestMessage(tBase);
                if (result != null) {
                    byte[] resultBytes = SerializationUtils.serialize(result, serializerFactory);
                    pinpointSocket.response(requestPacket, resultBytes);
                }
            } catch (TException e) {
                if (logger.isWarnEnabled()) {
                    logger.warn("packet serialize error. SendSocketAddress:{} Cause:{}", remoteAddress, e.getMessage(),
                            e);
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("packet dump hex:{}", PacketUtils.dumpByteArray(bytes));
                }
            } catch (Exception e) {
                // there are cases where invalid headers are received
                if (logger.isWarnEnabled()) {
                    logger.warn("Unexpected error. SendSocketAddress:{} Cause:{}", remoteAddress, e.getMessage(), e);
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("packet dump hex:{}", PacketUtils.dumpByteArray(bytes));
                }
            }
        }
    }
}
