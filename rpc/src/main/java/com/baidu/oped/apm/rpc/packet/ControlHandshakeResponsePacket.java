
package com.baidu.oped.apm.rpc.packet;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * class ControlHandshakeResponsePacket 
 *
 * @author meidongxu@baidu.com
 */
public class ControlHandshakeResponsePacket extends ControlPacket {

    public static final String CODE = "code";
    public static final String SUB_CODE = "subCode";
    
    public ControlHandshakeResponsePacket(byte[] payload) {
        super(payload);
    }

    public ControlHandshakeResponsePacket(int requestId, byte[] payload) {
        super(payload);
        setRequestId(requestId);
    }

    @Override
    public short getPacketType() {
        return PacketType.CONTROL_HANDSHAKE_RESPONSE;
    }

    @Override
    public ChannelBuffer toBuffer() {

        ChannelBuffer header = ChannelBuffers.buffer(2 + 4 + 4);
        header.writeShort(PacketType.CONTROL_HANDSHAKE_RESPONSE);
        header.writeInt(getRequestId());

        return PayloadPacket.appendPayload(header, payload);
    }

    public static ControlHandshakeResponsePacket readBuffer(short packetType, ChannelBuffer buffer) {
        assert packetType == PacketType.CONTROL_HANDSHAKE_RESPONSE;

        if (buffer.readableBytes() < 8) {
            buffer.resetReaderIndex();
            return null;
        }

        final int messageId = buffer.readInt();
        final ChannelBuffer payload = PayloadPacket.readPayload(buffer);
        if (payload == null) {
            return null;
        }
        final ControlHandshakeResponsePacket helloPacket = new ControlHandshakeResponsePacket(payload.array());
        helloPacket.setRequestId(messageId);
        return helloPacket;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName());
        sb.append("{requestId=").append(getRequestId());
        sb.append(", ");
        if (payload == null) {
            sb.append("payload=null");
        } else {
            sb.append("payloadLength=").append(payload.length);
        }
        sb.append('}');
        return sb.toString();
    }

}
