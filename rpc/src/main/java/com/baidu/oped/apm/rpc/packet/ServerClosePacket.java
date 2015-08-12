
package com.baidu.oped.apm.rpc.packet;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * class ServerClosePacket 
 *
 * @author meidongxu@baidu.com
 */
public class ServerClosePacket extends BasicPacket {

    @Override
    public short getPacketType() {
        return PacketType.CONTROL_SERVER_CLOSE;
    }

    @Override
    public ChannelBuffer toBuffer() {

        ChannelBuffer header = ChannelBuffers.buffer(2 + 4);
        header.writeShort(PacketType.CONTROL_SERVER_CLOSE);

        return PayloadPacket.appendPayload(header, payload);
    }

    public static ServerClosePacket readBuffer(short packetType, ChannelBuffer buffer) {
        assert packetType == PacketType.CONTROL_SERVER_CLOSE;

        if (buffer.readableBytes() < 4) {
            buffer.resetReaderIndex();
            return null;
        }

        final ChannelBuffer payload = PayloadPacket.readPayload(buffer);
        if (payload == null) {
            return null;
        }
        final ServerClosePacket requestPacket = new ServerClosePacket();
        return requestPacket;

    }

    @Override
    public String toString() {
        return "ServerClosePacket";
    }
}
