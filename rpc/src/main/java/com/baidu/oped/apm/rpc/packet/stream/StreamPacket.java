
package com.baidu.oped.apm.rpc.packet.stream;

import com.baidu.oped.apm.rpc.packet.Packet;

/**
 * class StreamPacket 
 *
 * @author meidongxu@baidu.com
 */



public interface StreamPacket extends Packet {

    int getStreamChannelId();

}
