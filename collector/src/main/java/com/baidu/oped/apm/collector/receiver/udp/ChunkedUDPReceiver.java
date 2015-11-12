
package com.baidu.oped.apm.collector.receiver.udp;

import com.baidu.oped.apm.collector.config.UdpServerConfig;
import com.baidu.oped.apm.thrift.io.ChunkHeaderTBaseDeserializer;
import com.baidu.oped.apm.thrift.io.ChunkHeaderTBaseDeserializerFactory;
import com.baidu.oped.apm.thrift.io.DeserializerFactory;
import com.baidu.oped.apm.thrift.io.ThreadLocalHeaderTBaseDeserializerFactory;

import java.net.DatagramPacket;

/**
 * Chunked UDP packet receiver
 * 
 * @author jaehong.kim
 */
public class ChunkedUDPReceiver extends AbstractUDPReceiver {

    private final DeserializerFactory<ChunkHeaderTBaseDeserializer> deserializerFactory = new ThreadLocalHeaderTBaseDeserializerFactory<ChunkHeaderTBaseDeserializer>(new ChunkHeaderTBaseDeserializerFactory());

    public ChunkedUDPReceiver(UdpServerConfig serverConfig, PacketHandlerFactory<DatagramPacket> packetHandlerFactory) {
        super(serverConfig, packetHandlerFactory);
    }

//    @Override
//    Runnable getPacketDispatcher(AbstractUDPReceiver receiver, DatagramPacket packet) {
//        return new DispatchPacket(receiver, packet);
//    }

//    private class DispatchPacket implements Runnable {
//        private final AbstractUDPReceiver receiver;
//        private final DatagramPacket packet;
//
//        private DispatchPacket(AbstractUDPReceiver receiver, DatagramPacket packet) {
//            if (packet == null) {
//                throw new NullPointerException("packet must not be null");
//            }
//            this.receiver = receiver;
//            this.packet = packet;
//        }
//
//        @Override
//        public void run() {
//            Timer.Context time = receiver.getTimer().time();
//
//            final ChunkHeaderTBaseDeserializer deserializer = deserializerFactory.createDeserializer();
//            try {
//                List<TBase<?, ?>> list = deserializer.deserialize(packet.getData(), packet.getOffset(), packet.getLength());
//                if (list == null) {
//                    return;
//                }
//
//                for (TBase<?, ?> tBase : list) {
//                    if (tBase instanceof L4Packet) {
//                        if (logger.isDebugEnabled()) {
//                            L4Packet packet = (L4Packet) tBase;
//                            logger.debug("udp l4 packet {}", packet.getHeader());
//                        }
//                        continue;
//                    }
//                    // Network port availability check packet
//                    if (tBase instanceof NetworkAvailabilityCheckPacket) {
//                        if (logger.isDebugEnabled()) {
//                            logger.debug("received udp network availability check packet.");
//                        }
//                        responseOK();
//                        continue;
//                    }
//                    // dispatch signifies business logic execution
//                    receiver.getDispatchHandler().dispatchSendMessage(tBase);
//                }
//            } catch (TException e) {
//                if (logger.isWarnEnabled()) {
//                    logger.warn("packet serialize error. SendSocketAddress:{} Cause:{}", packet.getSocketAddress(), e.getMessage(), e);
//                }
//                if (logger.isDebugEnabled()) {
//                    logger.debug("packet dump hex:{}", PacketUtils.dumpDatagramPacket(packet));
//                }
//            } catch (Exception e) {
//                if (logger.isWarnEnabled()) {
//                    logger.warn("Unexpected error. SendSocketAddress:{} Cause:{} ", packet.getSocketAddress(), e.getMessage(), e);
//                }
//                if (logger.isDebugEnabled()) {
//                    logger.debug("packet dump hex:{}", PacketUtils.dumpDatagramPacket(packet));
//                }
//            } finally {
//                receiver.getDatagramPacketPool().returnObject(packet);
//                time.stop();
//            }
//        }
//
//        private void responseOK() {
//            try {
//                byte[] okBytes = NetworkAvailabilityCheckPacket.DATA_OK;
//                DatagramPacket pongPacket = new DatagramPacket(okBytes, okBytes.length, packet.getSocketAddress());
//                receiver.getSocket().send(pongPacket);
//            } catch (IOException e) {
//                if (logger.isWarnEnabled()) {
//                    logger.warn("pong error. SendSocketAddress:{} Cause:{}", packet.getSocketAddress(), e.getMessage(), e);
//                }
//                if (logger.isDebugEnabled()) {
//                    logger.debug("packet dump hex:{}", PacketUtils.dumpDatagramPacket(packet));
//                }
//            }
//        }
//    }

}
