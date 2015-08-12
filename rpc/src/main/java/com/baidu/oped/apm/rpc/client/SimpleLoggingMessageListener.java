
package com.baidu.oped.apm.rpc.client;

import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.oped.apm.rpc.packet.RequestPacket;
import com.baidu.oped.apm.rpc.packet.ResponsePacket;
import com.baidu.oped.apm.rpc.packet.SendPacket;

/**
 * class SimpleLoggingMessageListener 
 *
 * @author meidongxu@baidu.com
 */
public class SimpleLoggingMessageListener implements MessageListener {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final SimpleLoggingMessageListener LISTENER = new SimpleLoggingMessageListener();

    @Override
    public void handleSend(SendPacket sendPacket, Channel channel) {
        logger.info("handlerSend {} {}", sendPacket, channel);
    }

    @Override
    public void handleRequest(RequestPacket requestPacket, Channel channel) {
        channel.write(new ResponsePacket(requestPacket.getRequestId(), new byte[0]));
        logger.info("handlerRequest {} {}", requestPacket, channel);
    }

}
