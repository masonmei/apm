package com.baidu.oped.apm.collector.receiver.udp;

import com.baidu.oped.apm.collector.receiver.DispatchHandler;
import com.baidu.oped.apm.collector.util.PacketUtils;
import com.baidu.oped.apm.thrift.io.DeserializerFactory;
import com.baidu.oped.apm.thrift.io.HeaderTBaseDeserializer;
import com.baidu.oped.apm.thrift.io.HeaderTBaseDeserializerFactory;
import com.baidu.oped.apm.thrift.io.ThreadLocalHeaderTBaseDeserializerFactory;
import org.apache.thrift.TBase;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.DatagramPacket;
import java.net.SocketAddress;

/**
 * Created by mason on 11/12/15.
 */
public abstract class AbstractUDPHandlerFactory<T extends DatagramPacket> implements PacketHandlerFactory<T> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final DeserializerFactory<HeaderTBaseDeserializer> deserializerFactory = new ThreadLocalHeaderTBaseDeserializerFactory<>(new HeaderTBaseDeserializerFactory());

    private final DispatchHandler dispatchHandler;

    private final TBaseFilter<SocketAddress> filter;

    private final PacketHandler<T> dispatchPacket = new DispatchPacket();

    public AbstractUDPHandlerFactory(DispatchHandler dispatchHandler, TBaseFilter<SocketAddress> filter) {
        if (dispatchHandler == null) {
            throw new NullPointerException("dispatchHandler must not be null");
        }
        if (filter == null) {
            throw new NullPointerException("filter must not be null");
        }
        this.dispatchHandler = dispatchHandler;
        this.filter = filter;
    }

    @Override
    public PacketHandler<T> createPacketHandler() {
        return this.dispatchPacket;
    }

    // stateless
    private class DispatchPacket implements PacketHandler<T> {

        private DispatchPacket() {
        }

        @Override
        public void receive(T packet) {
            final HeaderTBaseDeserializer deserializer = deserializerFactory.createDeserializer();
            TBase<?, ?> tBase = null;
            SocketAddress socketAddress = packet.getSocketAddress();
            try {
                tBase = deserializer.deserialize(packet.getData());
                if (!filter.filter(tBase, socketAddress)) {
                    return;
                }
                // dispatch signifies business logic execution
                dispatchHandler.dispatchSendMessage(tBase);
            } catch (TException e) {
                if (logger.isWarnEnabled()) {
                    logger.warn("packet serialize error. SendSocketAddress:{} Cause:{}", socketAddress, e.getMessage(), e);
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("packet dump hex:{}", PacketUtils.dumpDatagramPacket(packet));
                }
            } catch (Exception e) {
                // there are cases where invalid headers are received
                if (logger.isWarnEnabled()) {
                    logger.warn("Unexpected error. SendSocketAddress:{} Cause:{} tBase:{}", socketAddress, e.getMessage(), tBase, e);
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("packet dump hex:{}", PacketUtils.dumpDatagramPacket(packet));
                }
            }
        }
    }
}
