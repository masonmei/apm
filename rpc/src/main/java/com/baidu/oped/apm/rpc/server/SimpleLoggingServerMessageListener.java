
package com.baidu.oped.apm.rpc.server;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.oped.apm.rpc.packet.HandshakeResponseCode;
import com.baidu.oped.apm.rpc.packet.HandshakeResponseType;
import com.baidu.oped.apm.rpc.packet.RequestPacket;
import com.baidu.oped.apm.rpc.packet.SendPacket;

/**
 * class SimpleLoggingServerMessageListener 
 *
 * @author meidongxu@baidu.com
 */
public class SimpleLoggingServerMessageListener implements ServerMessageListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final SimpleLoggingServerMessageListener LISTENER = new SimpleLoggingServerMessageListener();

    @Override
    public void handleSend(SendPacket sendPacket, SocketChannel channel) {
        logger.info("handlerSend {} {}", sendPacket, channel);
    }

    @Override
    public void handleRequest(RequestPacket requestPacket, SocketChannel channel) {
        logger.info("handlerRequest {} {}", requestPacket, channel);
    }

    @Override
    public HandshakeResponseCode handleHandshake(Map properties) {
        logger.info("handleEnableWorker {}", properties);
        return HandshakeResponseType.Success.SUCCESS;
    }

}
