
package com.baidu.oped.apm.profiler.context;

import com.baidu.oped.apm.common.ServiceType;
import com.baidu.oped.apm.common.Version;
import com.baidu.oped.apm.profiler.AgentInformation;
import com.baidu.oped.apm.profiler.context.Span;
import com.baidu.oped.apm.profiler.context.SpanChunkFactory;
import com.baidu.oped.apm.profiler.context.SpanEvent;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * class SpanChunkFactoryTest 
 *
 * @author meidongxu@baidu.com
 */
public class SpanChunkFactoryTest {
    @Test
    public void create() {
        AgentInformation agentInformation = new AgentInformation("agentId", "applicationName", 0,0, "machineName", "127.0.0.1", ServiceType.TOMCAT.getCode(), Version.VERSION);
        SpanChunkFactory spanChunkFactory = new SpanChunkFactory(agentInformation);

        try {
            spanChunkFactory.create(new ArrayList<SpanEvent>());
            Assert.fail();
        } catch (Exception e) {
        }
        // one spanEvent
        List<SpanEvent> spanEvents = new ArrayList<SpanEvent>();
        spanEvents.add(new SpanEvent(new Span()));
        spanChunkFactory.create(spanEvents);

        // two spanEvent
        spanEvents.add(new SpanEvent(new Span()));
        spanChunkFactory.create(spanEvents);

        // three
        spanEvents.add(new SpanEvent(new Span()));
        spanChunkFactory.create(spanEvents);

    }
}
