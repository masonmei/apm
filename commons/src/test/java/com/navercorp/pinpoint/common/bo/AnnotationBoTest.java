
package com.baidu.oped.apm.common.bo;

import com.baidu.oped.apm.common.AnnotationKey;
import com.baidu.oped.apm.common.bo.AnnotationBo;
import com.baidu.oped.apm.common.buffer.AutomaticBuffer;
import com.baidu.oped.apm.common.buffer.Buffer;

import org.junit.Assert;
import org.junit.Test;

/**
 * class AnnotationBoTest 
 *
 * @author meidongxu@baidu.com
 */
public class AnnotationBoTest {
    @Test
    public void testGetVersion() throws Exception {

    }

    @Test
    public void testSetVersion() throws Exception {

    }

    @Test
    public void testWriteValue() throws Exception {
        AnnotationBo bo = new AnnotationBo();
        bo.setKey(AnnotationKey.API.getCode());
        bo.setByteValue("value".getBytes("UTF-8"));
//        int bufferSize = bo.getBufferSize();

        Buffer buffer = new AutomaticBuffer(128);
        bo.writeValue(buffer);

        AnnotationBo bo2 = new AnnotationBo();
        buffer.setOffset(0);
        bo2.readValue(buffer);
        Assert.assertEquals(bo.getKey(), bo2.getKey());
        Assert.assertEquals(bo.getValueType(), bo2.getValueType());
        Assert.assertArrayEquals(bo.getByteValue(), bo2.getByteValue());
    }

}
