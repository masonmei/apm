
package com.baidu.oped.apm.common.util;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

import com.google.common.primitives.Longs;
import com.baidu.oped.apm.common.PinpointConstants;
import com.baidu.oped.apm.thrift.dto.TSpan;

/**
 * class SpanUtilsTest 
 *
 * @author meidongxu@baidu.com
 */
public class SpanUtilsTest {
    @Test
    public void testGetTraceIndexRowKeyWhiteSpace() throws Exception {
        String agentId = "test test";
        long time = System.currentTimeMillis();
        check(agentId, time);
    }

    @Test
    public void testGetTraceIndexRowKey1() throws Exception {
        String agentId = "test";
        long time = System.currentTimeMillis();
        check(agentId, time);
    }

    @Test
    public void testGetTraceIndexRowKey2() throws Exception {
        String agentId = "";
        for (int i = 0; i < PinpointConstants.AGENT_NAME_MAX_LEN; i++) {
            agentId += "1";
        }

        long time = System.currentTimeMillis();
        check(agentId, time);
    }

    @Test
    public void testGetTraceIndexRowKey3() throws Exception {
        String agentId = "";
        for (int i = 0; i < PinpointConstants.AGENT_NAME_MAX_LEN + 1; i++) {
            agentId += "1";
        }

        long time = System.currentTimeMillis();
        try {
            check(agentId, time);
            Assert.fail("error");
        } catch (IndexOutOfBoundsException ignore) {
        }
    }

    private void check(String agentId0, long l1) {
        TSpan span = new TSpan();
        span.setAgentId(agentId0);
        span.setStartTime(l1);

        byte[] traceIndexRowKey = SpanUtils.getAgentIdTraceIndexRowKey(span.getAgentId(), span.getStartTime());

        String agentId = BytesUtils.toString(traceIndexRowKey, 0, PinpointConstants.AGENT_NAME_MAX_LEN).trim();
        Assert.assertEquals(agentId0, agentId);
        
        long time = Longs.fromByteArray(Arrays.copyOfRange(traceIndexRowKey, PinpointConstants.AGENT_NAME_MAX_LEN, PinpointConstants.AGENT_NAME_MAX_LEN + 8));
        time = TimeUtils.recoveryTimeMillis(time);
        Assert.assertEquals(time, l1);
    }
}
