
package com.baidu.oped.apm.rpc.stream;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;

import com.baidu.oped.apm.rpc.packet.stream.StreamCreatePacket;

/**
 * class ClientStreamChannel 
 *
 * @author meidongxu@baidu.com
 */
public class ClientStreamChannel extends StreamChannel {

    public ClientStreamChannel(Channel channel, int streamId, StreamChannelManager streamChannelManager) {
        super(channel, streamId, streamChannelManager);
    }

    public ChannelFuture sendCreate(byte[] payload) {
        assertState(StreamChannelStateCode.OPEN_AWAIT);

        StreamCreatePacket packet = new StreamCreatePacket(getStreamId(), payload);
        return this.getChannel().write(packet);
    }

    boolean changeStateOpen() {
        boolean result = getState().changeStateOpen();

        logger.info(makeStateChangeMessage(StreamChannelStateCode.OPEN, result));
        return result;
    }

    boolean changeStateOpenAwait() {
        boolean result = getState().changeStateOpenAwait();

        logger.info(makeStateChangeMessage(StreamChannelStateCode.OPEN_AWAIT, result));
        return result;
    }

}
