
package com.baidu.oped.apm.rpc.packet;

import junit.framework.Assert;

import org.jboss.netty.buffer.ChannelBuffer;
import org.junit.Test;

import com.baidu.oped.apm.rpc.packet.PacketType;
import com.baidu.oped.apm.rpc.packet.PongPacket;

/**
 * class PongPacketTest 
 *
 * @author meidongxu@baidu.com
 */
public class PongPacketTest {
    @Test
    public void testToBuffer() throws Exception {
        PongPacket pongPacket = new PongPacket();
        ChannelBuffer channelBuffer = pongPacket.toBuffer();

        short pongCode = channelBuffer.readShort();
        Assert.assertEquals(PacketType.CONTROL_PONG, pongCode);


    }
}
