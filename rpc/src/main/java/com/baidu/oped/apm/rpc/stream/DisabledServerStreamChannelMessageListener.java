
package com.baidu.oped.apm.rpc.stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.oped.apm.rpc.packet.stream.BasicStreamPacket;
import com.baidu.oped.apm.rpc.packet.stream.StreamClosePacket;
import com.baidu.oped.apm.rpc.packet.stream.StreamCreatePacket;

/**
 * class DisabledServerStreamChannelMessageListener 
 *
 * @author meidongxu@baidu.com
 */


public class DisabledServerStreamChannelMessageListener implements ServerStreamChannelMessageListener {

    public static final ServerStreamChannelMessageListener INSTANCE = new DisabledServerStreamChannelMessageListener();

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public short handleStreamCreate(ServerStreamChannelContext streamChannelContext, StreamCreatePacket packet) {
        logger.info("{} handleStreamCreate unsupported operation. StreamChannel:{}, Packet:{}", this.getClass().getSimpleName(), streamChannelContext, packet);
        return BasicStreamPacket.TYPE_SERVER_UNSUPPORT;
    }

    @Override
    public void handleStreamClose(ServerStreamChannelContext streamChannelContext, StreamClosePacket packet) {
        logger.info("{} handleStreamClose unsupported operation. StreamChannel:{}, Packet:{}", this.getClass().getSimpleName(), streamChannelContext, packet);
    }

}
