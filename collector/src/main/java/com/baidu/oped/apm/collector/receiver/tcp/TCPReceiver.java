
package com.baidu.oped.apm.collector.receiver.tcp;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.TBase;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.baidu.oped.apm.collector.receiver.DispatchHandler;
import com.baidu.oped.apm.collector.util.PacketUtils;
import com.baidu.oped.apm.common.util.ExecutorFactory;
import com.baidu.oped.apm.common.util.PinpointThreadFactory;
import com.baidu.oped.apm.rpc.packet.HandshakeResponseCode;
import com.baidu.oped.apm.rpc.packet.HandshakeResponseType;
import com.baidu.oped.apm.rpc.packet.RequestPacket;
import com.baidu.oped.apm.rpc.packet.SendPacket;
import com.baidu.oped.apm.rpc.server.PinpointServerSocket;
import com.baidu.oped.apm.rpc.server.ServerMessageListener;
import com.baidu.oped.apm.rpc.server.SocketChannel;
import com.baidu.oped.apm.rpc.util.MapUtils;
import com.baidu.oped.apm.thrift.io.DeserializerFactory;
import com.baidu.oped.apm.thrift.io.Header;
import com.baidu.oped.apm.thrift.io.HeaderTBaseDeserializer;
import com.baidu.oped.apm.thrift.io.HeaderTBaseDeserializerFactory;
import com.baidu.oped.apm.thrift.io.HeaderTBaseSerializer;
import com.baidu.oped.apm.thrift.io.HeaderTBaseSerializerFactory;
import com.baidu.oped.apm.thrift.io.L4Packet;
import com.baidu.oped.apm.thrift.io.SerializerFactory;
import com.baidu.oped.apm.thrift.io.ThreadLocalHeaderTBaseDeserializerFactory;
import com.baidu.oped.apm.thrift.io.ThreadLocalHeaderTBaseSerializerFactory;
import com.baidu.oped.apm.thrift.util.SerializationUtils;

/**
 * class TCPReceiver 
 *
 * @author meidongxu@baidu.com
 */
public class TCPReceiver {

    private final Logger logger = LoggerFactory.getLogger(TCPReceiver.class);

    private final ThreadFactory THREAD_FACTORY = new PinpointThreadFactory("Pinpoint-TCP-Worker");
    private final PinpointServerSocket pinpointServerSocket;
    private final DispatchHandler dispatchHandler;
    private final String bindAddress;
    private final int port;
    private final SerializerFactory<HeaderTBaseSerializer> serializerFactory =
            new ThreadLocalHeaderTBaseSerializerFactory<HeaderTBaseSerializer>(new HeaderTBaseSerializerFactory(true,
                                                                                                                       HeaderTBaseSerializerFactory.DEFAULT_UDP_STREAM_MAX_SIZE));
    private final DeserializerFactory<HeaderTBaseDeserializer> deserializerFactory =
            new ThreadLocalHeaderTBaseDeserializerFactory<HeaderTBaseDeserializer>(new HeaderTBaseDeserializerFactory());
    private int threadSize = 256;
    private int workerQueueSize = 1024 * 5;
    private final ThreadPoolExecutor worker =
            ExecutorFactory.newFixedThreadPool(threadSize, workerQueueSize, THREAD_FACTORY);
//    @Value("#{(pinpoint_collector_properties['collector.l4.ip']).split(',')}")
    private List<String> l4ipList;

    public TCPReceiver(DispatchHandler dispatchHandler, String bindAddress, int port, List<String> l4ipList) {
        if (dispatchHandler == null) {
            throw new NullPointerException("dispatchHandler must not be null");
        }
        if (bindAddress == null) {
            throw new NullPointerException("bindAddress must not be null");
        }

        this.pinpointServerSocket = new PinpointServerSocket();

        this.dispatchHandler = dispatchHandler;
        this.bindAddress = bindAddress;
        this.port = port;
        this.l4ipList = l4ipList;
    }

    private void setL4TcpChannel(PinpointServerSocket pinpointServerSocket) {
        if (l4ipList == null) {
            return;
        }
        try {
            List<InetAddress> inetAddressList = new ArrayList<InetAddress>();
            for (String l4Ip : l4ipList) {
                if (StringUtils.isBlank(l4Ip)) {
                    continue;
                }

                InetAddress address = InetAddress.getByName(l4Ip);
                if (address != null) {
                    inetAddressList.add(address);
                }
            }

            InetAddress[] inetAddressArray = new InetAddress[inetAddressList.size()];
            pinpointServerSocket.setIgnoreAddressList(inetAddressList.toArray(inetAddressArray));
        } catch (UnknownHostException e) {
            logger.warn("l4ipList error {}", l4ipList, e);
        }
    }

    @PostConstruct
    public void start() {
        setL4TcpChannel(pinpointServerSocket);
        // take care when attaching message handlers as events are generated from the IO thread.
        // pass them to a separate queue and handle them in a different thread.
        this.pinpointServerSocket.setMessageListener(new ServerMessageListener() {
            @Override
            public void handleSend(SendPacket sendPacket, SocketChannel channel) {
                receive(sendPacket, channel);
            }

            @Override
            public void handleRequest(RequestPacket requestPacket, SocketChannel channel) {
                requestResponse(requestPacket, channel);
            }

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
        });
        this.pinpointServerSocket.bind(bindAddress, port);

    }

    private void receive(SendPacket sendPacket, SocketChannel channel) {
        try {
            worker.execute(new Dispatch(sendPacket.getPayload(), channel.getRemoteAddress()));
        } catch (RejectedExecutionException e) {
            // cause is clear - full stack trace not necessary 
            logger.warn("RejectedExecutionException Caused:{}", e.getMessage());
        }
    }

    private void requestResponse(RequestPacket requestPacket, SocketChannel channel) {
        try {
            worker.execute(new RequestResponseDispatch(requestPacket, channel));
        } catch (RejectedExecutionException e) {
            // cause is clear - full stack trace not necessary
            logger.warn("RejectedExecutionException Caused:{}", e.getMessage());
        }

 }

    @PreDestroy
    public void stop() {
        logger.info("Pinpoint-TCP-Server stop");
        pinpointServerSocket.close();
        worker.shutdown();
        try {
            worker.awaitTermination(10, TimeUnit.SECONDS);
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
                dispatchHandler.dispatchSendMessage(tBase, bytes, Header.HEADER_SIZE, bytes.length);
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
        private final SocketChannel socketChannel;

        private RequestResponseDispatch(RequestPacket requestPacket, SocketChannel socketChannel) {
            if (requestPacket == null) {
                throw new NullPointerException("requestPacket");
            }
            this.requestPacket = requestPacket;
            this.socketChannel = socketChannel;
        }

        @Override
        public void run() {

            byte[] bytes = requestPacket.getPayload();
            SocketAddress remoteAddress = socketChannel.getRemoteAddress();
            try {
                TBase<?, ?> tBase = SerializationUtils.deserialize(bytes, deserializerFactory);
                if (tBase instanceof L4Packet) {
                    if (logger.isDebugEnabled()) {
                        L4Packet packet = (L4Packet) tBase;
                        logger.debug("tcp l4 packet {}", packet.getHeader());
                    }
                    return;
                }
                TBase result = dispatchHandler.dispatchRequestMessage(tBase, bytes, Header.HEADER_SIZE, bytes.length);
                if (result != null) {
                    byte[] resultBytes = SerializationUtils.serialize(result, serializerFactory);
                    socketChannel.sendResponseMessage(requestPacket, resultBytes);
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
