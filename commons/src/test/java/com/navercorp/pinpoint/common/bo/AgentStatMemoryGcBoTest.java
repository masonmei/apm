
package com.baidu.oped.apm.common.bo;

import static org.junit.Assert.*;

import org.junit.Test;

import com.baidu.oped.apm.common.bo.AgentStatMemoryGcBo;
import com.baidu.oped.apm.thrift.dto.TJvmGcType;

/**
 * class AgentStatMemoryGcBoTest 
 *
 * @author meidongxu@baidu.com
 */
public class AgentStatMemoryGcBoTest {

    @Test
    public void testByteArrayConversion() {
        // Given
        final AgentStatMemoryGcBo.Builder builder = new AgentStatMemoryGcBo.Builder("agentId", 0L, 1L);
        builder.gcType(TJvmGcType.G1.name());
        builder.jvmMemoryHeapUsed(Long.MIN_VALUE);
        builder.jvmMemoryHeapMax(Long.MAX_VALUE);
        builder.jvmMemoryNonHeapUsed(Long.MIN_VALUE);
        builder.jvmMemoryNonHeapMax(Long.MAX_VALUE);
        builder.jvmGcOldCount(1L);
        builder.jvmGcOldTime(2L);
        final AgentStatMemoryGcBo testBo = builder.build();
        // When
        final byte[] serializedBo = testBo.writeValue();
        final AgentStatMemoryGcBo deserializedBo = new AgentStatMemoryGcBo.Builder(serializedBo).build();
        // Then
        assertEquals(testBo.getAgentId(), deserializedBo.getAgentId());
        assertEquals(testBo.getStartTimestamp(), deserializedBo.getStartTimestamp());
        assertEquals(testBo.getTimestamp(), deserializedBo.getTimestamp());
        assertEquals(testBo.getGcType(), deserializedBo.getGcType());
        assertEquals(testBo.getJvmMemoryHeapUsed(), deserializedBo.getJvmMemoryHeapUsed());
        assertEquals(testBo.getJvmMemoryHeapMax(), deserializedBo.getJvmMemoryHeapMax());
        assertEquals(testBo.getJvmMemoryNonHeapUsed(), deserializedBo.getJvmMemoryNonHeapUsed());
        assertEquals(testBo.getJvmMemoryNonHeapMax(), deserializedBo.getJvmMemoryNonHeapMax());
        assertEquals(testBo.getJvmGcOldCount(), deserializedBo.getJvmGcOldCount());
        assertEquals(testBo.getJvmGcOldTime(), deserializedBo.getJvmGcOldTime());
    }

}
