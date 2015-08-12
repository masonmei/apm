
package com.baidu.oped.apm.rpc.message;

import com.baidu.oped.apm.rpc.packet.SendPacket;

import org.jboss.netty.buffer.ChannelBuffer;
import org.junit.Assert;
import org.junit.Test;

/**
 * class SendPacketTest 
 *
 * @author meidongxu@baidu.com
 */
public class SendPacketTest {
    @Test
    public void testToBuffer() throws Exception {
        byte[] bytes = new byte[10];
        SendPacket packetSend = new SendPacket(bytes);

        ChannelBuffer channelBuffer = packetSend.toBuffer();

        short packetType = channelBuffer.readShort();
        SendPacket packet = (SendPacket) SendPacket.readBuffer(packetType, channelBuffer);
        Assert.assertArrayEquals(bytes, packet.getPayload());


    }
}
