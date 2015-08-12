
package com.baidu.oped.apm.rpc.server;

import java.util.HashMap;
import java.util.Map;

import org.jboss.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.oped.apm.rpc.control.ProtocolException;
import com.baidu.oped.apm.rpc.packet.ControlHandshakePacket;
import com.baidu.oped.apm.rpc.packet.ControlHandshakeResponsePacket;
import com.baidu.oped.apm.rpc.packet.HandshakeResponseCode;
import com.baidu.oped.apm.rpc.packet.HandshakeResponseType;
import com.baidu.oped.apm.rpc.server.handler.HandshakerHandler;
import com.baidu.oped.apm.rpc.util.ControlMessageEncodingUtils;

/**
 * class PinpointServerSocketHandshaker 
 *
 * @author meidongxu@baidu.com
 */



public class PinpointServerSocketHandshaker {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public HandshakeResponseCode handleHandshake(Map<Object, Object> handshakeData, HandshakerHandler handler) {
        if (handshakeData == null) {
            return HandshakeResponseType.ProtocolError.PROTOCOL_ERROR;
        }

        HandshakeResponseCode code = handler.handleHandshake(handshakeData);
        
        return code;
    }
    
    public void sendHandshakeResponse(Channel channel, int requestId, HandshakeResponseCode responseCode, boolean isFirst) {
        if (!isFirst) {
            if (HandshakeResponseCode.DUPLEX_COMMUNICATION == responseCode) {
                sendHandshakeResponse0(channel, requestId, HandshakeResponseCode.ALREADY_DUPLEX_COMMUNICATION);
            } else if (HandshakeResponseCode.SIMPLEX_COMMUNICATION == responseCode) {
                sendHandshakeResponse0(channel, requestId, HandshakeResponseCode.ALREADY_SIMPLEX_COMMUNICATION);
            } else {
                sendHandshakeResponse0(channel, requestId, responseCode);
            }
        } else {
            sendHandshakeResponse0(channel, requestId, responseCode);
        }
    }

    private void sendHandshakeResponse0(Channel channel, int requestId, HandshakeResponseCode handShakeResponseCode) {
        try {
            logger.info("write HandshakeResponsePakcet. channel:{}, HandshakeResponseCode:{}.", channel, handShakeResponseCode);

            Map<String, Object> result = new HashMap<String, Object>();
            result.put(ControlHandshakeResponsePacket.CODE, handShakeResponseCode.getCode());
            result.put(ControlHandshakeResponsePacket.SUB_CODE, handShakeResponseCode.getSubCode());

            byte[] resultPayload = ControlMessageEncodingUtils.encode(result);
            ControlHandshakeResponsePacket packet = new ControlHandshakeResponsePacket(requestId, resultPayload);

            channel.write(packet);
        } catch (ProtocolException e) {
            logger.warn(e.getMessage(), e);
        }
    }
    
    public Map<Object, Object> decodeHandshakePacket(ControlHandshakePacket message) {
        try {
            byte[] payload = message.getPayload();
            Map<Object, Object> properties = (Map) ControlMessageEncodingUtils.decode(payload);
            return properties;
        } catch (ProtocolException e) {
            logger.warn(e.getMessage(), e);
        }

        return null;
    }

}
