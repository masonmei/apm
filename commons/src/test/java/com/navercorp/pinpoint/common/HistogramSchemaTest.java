
package com.baidu.oped.apm.common;

import org.junit.Assert;
import org.junit.Test;

import com.baidu.oped.apm.common.HistogramSchema;
import com.baidu.oped.apm.common.ServiceType;

/**
 * class HistogramSchemaTest 
 *
 * @author meidongxu@baidu.com
 */
public class HistogramSchemaTest {
    @Test
    public void testAddHistogramSlot() throws Exception {

    }

    @Test
    public void testGetHistogramSlotList() throws Exception {

    }

    @Test
    public void testCreateNode() throws Exception {

    }

    @Test
    public void testFindHistogramSlot() throws Exception {
        HistogramSchema histogramSchema = ServiceType.TOMCAT.getHistogramSchema();
        Assert.assertEquals(histogramSchema.findHistogramSlot(999).getSlotTime(), 1000);
        Assert.assertEquals(histogramSchema.findHistogramSlot(1000).getSlotTime(), 1000);
        Assert.assertEquals(histogramSchema.findHistogramSlot(1111).getSlotTime(), 3000);
    }



    @Test
    public void testGetHistogramSlotIndex() throws Exception {

    }
}
