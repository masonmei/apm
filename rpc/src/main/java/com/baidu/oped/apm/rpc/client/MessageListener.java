
package com.baidu.oped.apm.rpc.client;

import org.jboss.netty.channel.Channel;

import com.baidu.oped.apm.rpc.packet.RequestPacket;
import com.baidu.oped.apm.rpc.packet.SendPacket;

/**
 * class MessageListener 
 *
 * @author meidongxu@baidu.com
 */
public interface MessageListener {

    void handleSend(SendPacket sendPacket, Channel channel);

    void handleRequest(RequestPacket requestPacket, Channel channel);

}
