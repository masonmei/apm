package com.baidu.oped.apm.collector.receiver.udp;

import com.baidu.oped.apm.collector.util.PooledObject;

import java.net.DatagramPacket;

/**
 * Created by mason on 11/12/15.
 */
public class PooledPacketWrap implements Runnable {
    private final PacketHandler<DatagramPacket> packetHandler;
    private final PooledObject<DatagramPacket> pooledObject;

    public PooledPacketWrap(PacketHandler<DatagramPacket> packetHandler, PooledObject<DatagramPacket> pooledObject) {
        if (packetHandler == null) {
            throw new NullPointerException("packetReceiveHandler must not be null");
        }
        if (pooledObject == null) {
            throw new NullPointerException("pooledObject must not be null");
        }
        this.packetHandler = packetHandler;
        this.pooledObject = pooledObject;
    }

    @Override
    public void run() {
        final DatagramPacket packet = pooledObject.getObject();
        try {
            packetHandler.receive(packet);
        } finally {
            pooledObject.returnObject();
        }
    }
}
