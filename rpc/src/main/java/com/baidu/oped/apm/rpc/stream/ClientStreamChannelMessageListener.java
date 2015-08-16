
package com.baidu.oped.apm.rpc.stream;

import com.baidu.oped.apm.rpc.packet.stream.StreamClosePacket;
import com.baidu.oped.apm.rpc.packet.stream.StreamResponsePacket;

/**
 * class ClientStreamChannelMessageListener 
 *
 * @author meidongxu@baidu.com
 */
public interface ClientStreamChannelMessageListener {

    void handleStreamData(ClientStreamChannelContext streamChannelContext, StreamResponsePacket packet);

    void handleStreamClose(ClientStreamChannelContext streamChannelContext, StreamClosePacket packet);

}
