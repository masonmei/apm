
package com.baidu.oped.apm.rpc.server;

import com.baidu.oped.apm.rpc.packet.RequestPacket;
import com.baidu.oped.apm.rpc.packet.SendPacket;
import com.baidu.oped.apm.rpc.server.handler.HandshakerHandler;

/**
 * class ServerMessageListener 
 *
 * @author meidongxu@baidu.com
 */
public interface ServerMessageListener extends HandshakerHandler {

    void handleSend(SendPacket sendPacket, SocketChannel channel);

    // TODO make another tcp channel in case of exposed channel.
    void handleRequest(RequestPacket requestPacket, SocketChannel channel);

}
