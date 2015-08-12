
package com.baidu.oped.apm.rpc.packet;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * class Packet 
 *
 * @author meidongxu@baidu.com
 */
public interface Packet {

    short getPacketType();

    byte[] getPayload();

   ChannelBuffer toBuffer();
}
