
package com.baidu.oped.apm.rpc.packet.stream;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import com.baidu.oped.apm.rpc.packet.PacketType;
import com.baidu.oped.apm.rpc.packet.PayloadPacket;
import com.baidu.oped.apm.rpc.util.AssertUtils;

/**
 * class StreamResponsePacket 
 *
 * @author meidongxu@baidu.com
 */



public class StreamResponsePacket extends BasicStreamPacket {

    private final static short PACKET_TYPE = PacketType.APPLICATION_STREAM_RESPONSE;

    private final byte[] payload;

    public StreamResponsePacket(int streamChannelId, byte[] payload) {
        super(streamChannelId);

        AssertUtils.assertNotNull(payload);
        this.payload = payload;
    }

    @Override
    public short getPacketType() {
        return PACKET_TYPE;
    }

    @Override
    public byte[] getPayload() {
        return payload;
    }

    @Override
    public ChannelBuffer toBuffer() {
        ChannelBuffer header = ChannelBuffers.buffer(2 + 4 + 4);
        header.writeShort(getPacketType());
        header.writeInt(getStreamChannelId());

        return PayloadPacket.appendPayload(header, payload);
    }

    public static StreamResponsePacket readBuffer(short packetType, ChannelBuffer buffer) {
        assert packetType == PACKET_TYPE;

        if (buffer.readableBytes() < 8) {
            buffer.resetReaderIndex();
            return null;
        }

        final int streamChannelId = buffer.readInt();
        final ChannelBuffer payload = PayloadPacket.readPayload(buffer);
        if (payload == null) {
            return null;
        }

        final StreamResponsePacket packet = new StreamResponsePacket(streamChannelId, payload.array());
        return packet;
    }

}
