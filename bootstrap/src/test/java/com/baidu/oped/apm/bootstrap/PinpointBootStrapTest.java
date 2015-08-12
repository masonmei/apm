package com.baidu.oped.apm.bootstrap;

import org.junit.Test;

import junit.framework.Assert;

/**
 * class PinpointBootStrapTest
 *
 * @author meidongxu@baidu.com
 */
public class PinpointBootStrapTest {
    @Test
    public void testDuplicatedLoadCheck() throws Exception {
        Assert.assertFalse(PinpointBootStrap.getLoadState());
        PinpointBootStrap.premain("test", new DummyInstrumentation());

        Assert.assertTrue(PinpointBootStrap.getLoadState());

        PinpointBootStrap.premain("test", new DummyInstrumentation());
        // is leaving a log the only way to test for duplicate loading? 
        // ? check
    }
}
