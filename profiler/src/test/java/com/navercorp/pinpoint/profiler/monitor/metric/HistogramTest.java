
package com.baidu.oped.apm.profiler.monitor.metric;

import com.baidu.oped.apm.common.HistogramSchema;
import com.baidu.oped.apm.common.ServiceType;
import com.baidu.oped.apm.profiler.monitor.metric.HistogramSnapshot;
import com.baidu.oped.apm.profiler.monitor.metric.LongAdderHistogram;

import org.junit.Assert;
import org.junit.Test;

/**
 * class HistogramTest 
 *
 * @author meidongxu@baidu.com
 */
public class HistogramTest {

    @Test
    public void testAddResponseTime() throws Exception {
        HistogramSchema schema = ServiceType.TOMCAT.getHistogramSchema();
        LongAdderHistogram histogram = new LongAdderHistogram(ServiceType.TOMCAT);
        histogram.addResponseTime(1000);

        histogram.addResponseTime(3000);
        histogram.addResponseTime(3000);

        histogram.addResponseTime(5000);
        histogram.addResponseTime(5000);
        histogram.addResponseTime(5000);

        histogram.addResponseTime(6000);
        histogram.addResponseTime(6000);
        histogram.addResponseTime(6000);
        histogram.addResponseTime(6000);

        histogram.addResponseTime(schema.getErrorSlot().getSlotTime());
        histogram.addResponseTime(schema.getErrorSlot().getSlotTime());
        histogram.addResponseTime(schema.getErrorSlot().getSlotTime());
        histogram.addResponseTime(schema.getErrorSlot().getSlotTime());
        histogram.addResponseTime(schema.getErrorSlot().getSlotTime());


        HistogramSnapshot snapshot = histogram.createSnapshot();
        Assert.assertEquals(snapshot.getFastCount(), 1);
        Assert.assertEquals(snapshot.getNormalCount(), 2);
        Assert.assertEquals(snapshot.getSlowCount(), 3);
        Assert.assertEquals(snapshot.getVerySlowCount(), 4);
        Assert.assertEquals(snapshot.getErrorCount(), 5);
    }

}