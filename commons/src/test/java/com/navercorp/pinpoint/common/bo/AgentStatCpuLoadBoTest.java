
package com.baidu.oped.apm.common.bo;

import static org.junit.Assert.*;

import org.junit.Test;

import com.baidu.oped.apm.common.bo.AgentStatCpuLoadBo;

/**
 * class AgentStatCpuLoadBoTest 
 *
 * @author meidongxu@baidu.com
 */
public class AgentStatCpuLoadBoTest {

    // for comparing CPU Usage up to 2 decimal places
    private static final double DELTA = 1e-4;

    @Test
    public void testByteArrayConversion() {
        // Given
        final AgentStatCpuLoadBo testBo = createTestBo(0.22871734201908112D, 0.23790152370929718D);
        // When
        final byte[] serializedBo = testBo.writeValue();
        final AgentStatCpuLoadBo deserializedBo = new AgentStatCpuLoadBo.Builder(serializedBo).build();
        // Then
        assertEquals(testBo.getAgentId(), deserializedBo.getAgentId());
        assertEquals(testBo.getStartTimestamp(), deserializedBo.getStartTimestamp());
        assertEquals(testBo.getTimestamp(), deserializedBo.getTimestamp());
        assertEquals(testBo.getJvmCpuLoad(), deserializedBo.getJvmCpuLoad(), DELTA);
        assertEquals(testBo.getSystemCpuLoad(), deserializedBo.getSystemCpuLoad(), DELTA);
    }

    @Test
    public void testByteArrayConversionEdges() {
        // Given
        final AgentStatCpuLoadBo testBo = createTestBo(Double.MIN_VALUE, Double.MAX_VALUE);
        // When
        final byte[] serializedBo = testBo.writeValue();
        final AgentStatCpuLoadBo deserializedBo = new AgentStatCpuLoadBo.Builder(serializedBo).build();
        // Then
        assertEquals(testBo.getAgentId(), deserializedBo.getAgentId());
        assertEquals(testBo.getStartTimestamp(), deserializedBo.getStartTimestamp());
        assertEquals(testBo.getTimestamp(), deserializedBo.getTimestamp());
        assertEquals(testBo.getJvmCpuLoad(), deserializedBo.getJvmCpuLoad(), DELTA);
        assertEquals(testBo.getSystemCpuLoad(), deserializedBo.getSystemCpuLoad(), DELTA);
    }

    @Test
    public void testByteArrayConversionNanValues() {
        // Given
        final AgentStatCpuLoadBo testBo = createTestBo(Double.NaN, Double.NaN);
        // When
        final byte[] serializedBo = testBo.writeValue();
        final AgentStatCpuLoadBo deserializedBo = new AgentStatCpuLoadBo.Builder(serializedBo).build();
        // Then
        assertEquals(testBo.getAgentId(), deserializedBo.getAgentId());
        assertEquals(testBo.getStartTimestamp(), deserializedBo.getStartTimestamp());
        assertEquals(testBo.getTimestamp(), deserializedBo.getTimestamp());
        assertEquals(testBo.getJvmCpuLoad(), deserializedBo.getJvmCpuLoad(), DELTA);
        assertEquals(testBo.getSystemCpuLoad(), deserializedBo.getSystemCpuLoad(), DELTA);
    }

    @Test
    public void testByteArrayConversionInfiniteValues() {
        // Given
        final AgentStatCpuLoadBo testBo = createTestBo(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
        // When
        final byte[] serializedBo = testBo.writeValue();
        final AgentStatCpuLoadBo deserializedBo = new AgentStatCpuLoadBo.Builder(serializedBo).build();
        // Then
        assertEquals(testBo.getAgentId(), deserializedBo.getAgentId());
        assertEquals(testBo.getStartTimestamp(), deserializedBo.getStartTimestamp());
        assertEquals(testBo.getTimestamp(), deserializedBo.getTimestamp());
        assertEquals(testBo.getJvmCpuLoad(), deserializedBo.getJvmCpuLoad(), DELTA);
        assertEquals(testBo.getSystemCpuLoad(), deserializedBo.getSystemCpuLoad(), DELTA);
    }

    private AgentStatCpuLoadBo createTestBo(double jvmCpuLoad, double systemCpuLoad) {
        final AgentStatCpuLoadBo.Builder builder = new AgentStatCpuLoadBo.Builder("agentId", 0L, 0L);
        builder.jvmCpuLoad(jvmCpuLoad);
        builder.systemCpuLoad(systemCpuLoad);
        return builder.build();
    }

}
