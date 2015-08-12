
package com.baidu.oped.apm.common.buffer;

import junit.framework.Assert;

import org.junit.Test;

import com.baidu.oped.apm.common.buffer.Buffer;
import com.baidu.oped.apm.common.buffer.FixedBuffer;
import com.baidu.oped.apm.common.buffer.OffsetFixedBuffer;

/**
 * class OffsetFixedBufferTest 
 *
 * @author meidongxu@baidu.com
 */
public class OffsetFixedBufferTest {

    @Test
    public void testFixedBuffer() throws Exception {
        new OffsetFixedBuffer(new byte[10], 10);
        try {
            new OffsetFixedBuffer(new byte[10], 11);
            Assert.fail();
        } catch (IndexOutOfBoundsException ignore) {
        }
        try {
            new OffsetFixedBuffer(new byte[10], -1);
            Assert.fail();
        } catch (IndexOutOfBoundsException ignore) {
        }
    }

    @Test
    public void testGetBuffer() throws Exception {
        final int putValue = 10;
        Buffer buffer = new OffsetFixedBuffer(new byte[10], 2);
        buffer.put(putValue);
        byte[] intBuffer = buffer.getBuffer();
        Assert.assertEquals(intBuffer.length, 4);

        Buffer read = new FixedBuffer(intBuffer);
        int value = read.readInt();
        Assert.assertEquals(putValue, value);
    }
}
