
package com.baidu.oped.apm.rpc.server.handler;

import java.util.Map;

import com.baidu.oped.apm.rpc.packet.HandshakeResponseCode;

/**
 * class HandshakerHandler 
 *
 * @author meidongxu@baidu.com
 */



public interface HandshakerHandler {
    
    HandshakeResponseCode handleHandshake(Map properties);

}
