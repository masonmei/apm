
package com.baidu.oped.apm.common.util;

import junit.framework.Assert;

import org.junit.Test;

import com.baidu.oped.apm.common.util.RpcCodeRange;

/**
 * class RpcCodeRangeTest 
 *
 * @author meidongxu@baidu.com
 */
public class RpcCodeRangeTest {

    @Test
    public void testIsRpcRange() throws Exception {
        Assert.assertTrue(RpcCodeRange.isRpcRange(RpcCodeRange.RPC_START));
        Assert.assertTrue(RpcCodeRange.isRpcRange((short) (RpcCodeRange.RPC_END - 1)));
        Assert.assertFalse(RpcCodeRange.isRpcRange((short) 1));
    }

}