package com.baidu.oped.apm.collector.receiver.udp;

/**
 * Created by mason on 11/12/15.
 */
public interface PacketHandlerFactory<T> {
    PacketHandler<T> createPacketHandler();
}
