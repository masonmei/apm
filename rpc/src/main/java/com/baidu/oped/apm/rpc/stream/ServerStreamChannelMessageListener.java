
package com.baidu.oped.apm.rpc.stream;

import com.baidu.oped.apm.rpc.packet.stream.StreamClosePacket;
import com.baidu.oped.apm.rpc.packet.stream.StreamCreatePacket;

/**
 * class ServerStreamChannelMessageListener 
 *
 * @author meidongxu@baidu.com
 */



public interface ServerStreamChannelMessageListener {

    short handleStreamCreate(ServerStreamChannelContext streamChannelContext, StreamCreatePacket packet);

    void handleStreamClose(ServerStreamChannelContext streamChannelContext, StreamClosePacket packet);

}
