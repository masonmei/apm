package com.baidu.oped.apm.collector.receiver.udp;

/**
 * Created by mason on 11/12/15.
 */
public interface PacketHandler<T> {
    void receive(T packet);
}
