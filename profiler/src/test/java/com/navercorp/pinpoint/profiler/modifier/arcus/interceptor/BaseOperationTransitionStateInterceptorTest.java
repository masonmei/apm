
package com.baidu.oped.apm.profiler.modifier.arcus.interceptor;

import junit.framework.Assert;
import net.spy.memcached.ops.OperationState;
import org.junit.Test;

/**
 * class BaseOperationTransitionStateInterceptorTest 
 *
 * @author meidongxu@baidu.com
 */
public class BaseOperationTransitionStateInterceptorTest {

    @Test
    public void testComplete() throws Exception {
        // Arcus added TIMEOUT to OperationState of memcached. 
        // So you cannot just compare enum values but have to compare by their string representation.
        String complete = OperationState.COMPLETE.toString();
        Assert.assertEquals("COMPLETE", complete);
    }

    @Test
    public void existArcusTimeoutState() throws Exception {
        // Could affects other tests because this test forces to load a class
        if (!isArcusExist()) {
            // Skip test if Arcus is not present.
            return;
        }
        
        // Test if OperationState contains TIMEDOUT value.
        OperationState[] values = OperationState.values();
        for (OperationState value : values) {
            if (value.toString().equals("TIMEDOUT")) {
                return;
            }
        }

        Assert.fail("OperationState.TIMEDOUT state not found");
    }

    private boolean isArcusExist() {
        try {
            Class.forName("net.spy.memcached.ArcusClient");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
