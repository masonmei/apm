
package com.baidu.oped.apm.profiler.monitor;

import static org.junit.Assert.*;

import com.baidu.oped.apm.profiler.monitor.AgentStatMonitor;
import com.baidu.oped.apm.test.PeekableDataSender;
import com.baidu.oped.apm.thrift.dto.TAgentStatBatch;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * class AgentStatMonitorTest 
 *
 * @author meidongxu@baidu.com
 */
public class AgentStatMonitorTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private PeekableDataSender<TAgentStatBatch> peekableDataSender;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.peekableDataSender = new PeekableDataSender<TAgentStatBatch>();
    }

    @Test
    public void testAgentStatMonitor() throws InterruptedException {
        // Given
        final long collectionIntervalMs = 1000 * 1;
        final int numCollectionsPerBatch = 2;
        final int minNumBatchToTest = 2;
        final long totalTestDurationMs = collectionIntervalMs * numCollectionsPerBatch * minNumBatchToTest;
        // When
        System.setProperty("pinpoint.log", "test.");
        AgentStatMonitor monitor = new AgentStatMonitor(this.peekableDataSender, "agentId", System.currentTimeMillis(), collectionIntervalMs,
                numCollectionsPerBatch);
        monitor.start();
        Thread.sleep(totalTestDurationMs);
        monitor.stop();
        // Then
        assertTrue(peekableDataSender.size() >= minNumBatchToTest);
        for (TAgentStatBatch agentStatBatch : peekableDataSender) {
            logger.debug("agentStatBatch:{}", agentStatBatch);
            assertTrue(agentStatBatch.getAgentStats().size() <= numCollectionsPerBatch);
        }
    }

}
