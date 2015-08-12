
package com.baidu.oped.apm.profiler.util;

import org.junit.Assert;
import org.junit.Test;

import com.baidu.oped.apm.profiler.util.QueryStringUtil;

/**
 * class QueryStringUtilTest 
 *
 * @author meidongxu@baidu.com
 */
public class QueryStringUtilTest {
    @Test
    public void testRemoveAllMultiSpace() throws Exception {
        String s = QueryStringUtil.removeAllMultiSpace("a   b");

        Assert.assertEquals("a b", s);
    }
}
