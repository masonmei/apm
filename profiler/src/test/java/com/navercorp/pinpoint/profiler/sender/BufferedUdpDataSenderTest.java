
package com.baidu.oped.apm.profiler.sender;

import com.baidu.oped.apm.common.util.ThreadMXBeanUtils;
import com.baidu.oped.apm.profiler.sender.BufferedUdpDataSender;

import junit.framework.Assert;

import org.junit.Test;

/**
 * class BufferedUdpDataSenderTest 
 *
 * @author meidongxu@baidu.com
 */
public class BufferedUdpDataSenderTest {

    @Test
    public void testSendPacket() throws Exception {



    }

    @Test
    public void testStop_StopFlushThread() throws Exception {

        final BufferedUdpDataSender sender = new BufferedUdpDataSender("localhost", 9999, "testUdpSender", 100);

        final String flushThreadName = sender.getFlushThreadName();

        Assert.assertTrue(ThreadMXBeanUtils.findThreadName(flushThreadName));

        sender.stop();

        Assert.assertFalse(ThreadMXBeanUtils.findThreadName(flushThreadName));
        // ?? finally { send.stop() }
    }
}