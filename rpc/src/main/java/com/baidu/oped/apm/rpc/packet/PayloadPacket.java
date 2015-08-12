
package com.baidu.oped.apm.rpc.packet;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * class PayloadPacket 
 *
 * @author meidongxu@baidu.com
 */
public class PayloadPacket {

    private static final Logger logger = LoggerFactory.getLogger(PayloadPacket.class);

    private static final ChannelBuffer EMPTY_BUFFER = ChannelBuffers.buffer(0);


    public static ChannelBuffer readPayload(ChannelBuffer buffer) {
        if (buffer.readableBytes() < 4) {
            buffer.resetReaderIndex();
            return null;
        }

        final int payloadLength = buffer.readInt();
        if (payloadLength <= 0) {
            return EMPTY_BUFFER;
        }

        if (buffer.readableBytes() < payloadLength) {
            buffer.resetReaderIndex();
            return null;
        }
        return buffer.readBytes(payloadLength);
    }


    public static ChannelBuffer appendPayload(final ChannelBuffer header, final byte[] payload) {
        if (payload == null) {
            // this is also payload header
            header.writeInt(-1);
            return header;
        } else {
            header.writeInt(payload.length);
            ChannelBuffer payloadWrap = ChannelBuffers.wrappedBuffer(payload);
            return ChannelBuffers.wrappedBuffer(true, header, payloadWrap);
        }
    }

}
