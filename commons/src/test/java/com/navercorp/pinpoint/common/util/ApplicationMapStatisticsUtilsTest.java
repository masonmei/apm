
package com.baidu.oped.apm.common.util;

import com.baidu.oped.apm.common.ServiceType;
import com.baidu.oped.apm.common.buffer.Buffer;
import com.baidu.oped.apm.common.buffer.FixedBuffer;

import org.junit.Assert;
import org.junit.Test;

/**
 * class ApplicationMapStatisticsUtilsTest 
 *
 * @author meidongxu@baidu.com
 */
public class ApplicationMapStatisticsUtilsTest {

    @Test
    public void makeRowKey() {
        String applicationName = "TESTAPP";
        short serviceType = 123;
        long time = System.currentTimeMillis();

        byte[] bytes = ApplicationMapStatisticsUtils.makeRowKey(applicationName, serviceType, time);

        Assert.assertEquals(applicationName, ApplicationMapStatisticsUtils.getApplicationNameFromRowKey(bytes));
        Assert.assertEquals(serviceType, ApplicationMapStatisticsUtils.getApplicationTypeFromRowKey(bytes));
    }

    @Test
    public void testMakeColumnName() throws Exception {
        final byte[] columnNameBytes = ApplicationMapStatisticsUtils.makeColumnName("test", (short) 10);
        short slotNumber = BytesUtils.bytesToShort(columnNameBytes,0);
        Assert.assertEquals(slotNumber, 10);

        String columnName = BytesUtils.toString(columnNameBytes, BytesUtils.SHORT_BYTE_LENGTH, columnNameBytes.length - BytesUtils.SHORT_BYTE_LENGTH);
        Assert.assertEquals(columnName, "test");

    }

    @Test
    public void testMakeColumnName2() {
//        short serviceType, String applicationName, String destHost, short slotNumber
        final short slotNumber = 10;
        final byte[] columnNameBytes = ApplicationMapStatisticsUtils.makeColumnName(ServiceType.TOMCAT.getCode(), "applicationName", "dest", slotNumber);
        Buffer buffer = new FixedBuffer(columnNameBytes);
        Assert.assertEquals(ServiceType.TOMCAT.getCode(), buffer.readShort());
        Assert.assertEquals(10, buffer.readShort());
        Assert.assertEquals("applicationName", buffer.read2PrefixedString());

        int offset = buffer.getOffset();
        byte[] interBuffer = buffer.getInternalBuffer();
        Assert.assertEquals(BytesUtils.toString(interBuffer, offset, interBuffer.length - offset), "dest");

    }
}

