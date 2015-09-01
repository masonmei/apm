package com.baidu.oped.apm.statistics.collector.record.processor;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import org.junit.Before;
import org.junit.Test;

import com.baidu.oped.apm.common.jpa.entity.Trace;
import com.baidu.oped.apm.common.jpa.entity.WebTransactionStatistic;
import com.baidu.oped.apm.statistics.collector.ApdexDecider;
import com.baidu.oped.apm.statistics.collector.DefaultApdexDecider;

/**
 * Created by mason on 9/1/15.
 */
public class WebTransactionProcessorTest {
    private ApdexDecider decider;
    private WebTransactionProcessor processor;
    private List<Trace> items;

    @Before
    public void setUp() throws Exception {
        decider = new DefaultApdexDecider(500);
        processor = new WebTransactionProcessor();
        processor.setDecider(decider);
        items = new ArrayList<>();
        Trace trace = new Trace();

        trace.setRpc("/city/sync");
        trace.setStartTime(10000l);
        trace.setAppId(100L);
        trace.setInstanceId(1000L);
        trace.setElapsed(1000);
        trace.setErrCode(1);

        items.add(trace);
        Trace trace1 = new Trace();

        trace1.setRpc("/city/sync");
        trace1.setStartTime(10000l);
        trace.setAppId(100L);
        trace.setInstanceId(1000L);
        trace1.setElapsed(300);
        trace1.setErrCode(1);

        items.add(trace1);

        Trace trace2 = new Trace();
        trace2.setRpc("/city/sync1");
        trace2.setStartTime(10000l);
        trace.setAppId(100L);
        trace.setInstanceId(1000L);
        trace2.setElapsed(2500);
        trace2.setErrCode(1);
        items.add(trace2);
    }

    @Test
    public void testNewStatisticInstance() throws Exception {
        Iterable<WebTransactionStatistic> process = processor.process(items);
        assertEquals(2, StreamSupport.stream(process.spliterator(), false).count());
    }
}