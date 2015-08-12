
package com.baidu.oped.apm.profiler.context.storage;

import com.baidu.oped.apm.common.ServiceType;
import com.baidu.oped.apm.common.Version;
import com.baidu.oped.apm.profiler.AgentInformation;
import com.baidu.oped.apm.profiler.context.Span;
import com.baidu.oped.apm.profiler.context.SpanChunkFactory;
import com.baidu.oped.apm.profiler.context.SpanEvent;
import com.baidu.oped.apm.profiler.context.storage.BufferedStorage;
import com.baidu.oped.apm.profiler.sender.CountingDataSender;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * class BufferedStorageTest 
 *
 * @author meidongxu@baidu.com
 */
public class BufferedStorageTest {

    private AgentInformation agentInformation = new AgentInformation("agentId", "applicationName", 0, 1, "hostName", "127.0.0.1", ServiceType.TOMCAT.getCode(), Version.VERSION);
    private SpanChunkFactory spanChunkFactory = new SpanChunkFactory(agentInformation);
    private CountingDataSender countingDataSender = new CountingDataSender();

    @Before
    public void before() {
        countingDataSender.stop();
    }

    @Test
    public void testStore_Noflush() throws Exception {
        BufferedStorage bufferedStorage = new BufferedStorage(countingDataSender, spanChunkFactory, 10);

        Span span = new Span();
        SpanEvent spanEvent = new SpanEvent(span);
        bufferedStorage.store(spanEvent);
        bufferedStorage.store(spanEvent);

        Assert.assertEquals(0, countingDataSender.getTotalCount());
    }

    @Test
    public void testStore_flush() throws Exception {
        BufferedStorage bufferedStorage = new BufferedStorage(countingDataSender, spanChunkFactory, 1);

        Span span = new Span();
        SpanEvent spanEvent = new SpanEvent(span);
        bufferedStorage.store(spanEvent);
        bufferedStorage.store(spanEvent);

        Assert.assertEquals(0, countingDataSender.getSenderCounter(), 2);
        Assert.assertEquals(0, countingDataSender.getTotalCount(), 2);

        Assert.assertEquals(0, countingDataSender.getSpanChunkCounter(), 2);
        Assert.assertEquals(0, countingDataSender.getSpanCounter(), 0);
    }


    @Test
    public void testStore_spanFlush() throws Exception {
        BufferedStorage bufferedStorage = new BufferedStorage(countingDataSender, spanChunkFactory, 10);

        Span span = new Span();
        bufferedStorage.store(span);
        bufferedStorage.store(span);
        bufferedStorage.store(span);

        Assert.assertEquals(0, countingDataSender.getSenderCounter(), 3);
        Assert.assertEquals(0, countingDataSender.getTotalCount(), 3);

        Assert.assertEquals(0, countingDataSender.getSpanCounter(), 3);
        Assert.assertEquals(0, countingDataSender.getSpanChunkCounter(), 0);
    }

    @Test
    public void testStore_spanLastFlush() throws Exception {
        BufferedStorage bufferedStorage = new BufferedStorage(countingDataSender, spanChunkFactory, 10);

        Span span = new Span();
        SpanEvent spanEvent = new SpanEvent(span);
        bufferedStorage.store(spanEvent);
        bufferedStorage.store(spanEvent);
        bufferedStorage.store(span);

        Assert.assertEquals(0, countingDataSender.getSenderCounter(), 1);
        Assert.assertEquals(0, countingDataSender.getTotalCount(), 1);

        Assert.assertEquals(0, countingDataSender.getSpanCounter(), 1);
        Assert.assertEquals(0, countingDataSender.getSpanChunkCounter(), 0);
    }
}