package com.baidu.oped.apm.bootstrap;

import java.net.URL;

import org.junit.Test;

import junit.framework.Assert;

/**
 * class PinpointURLClassLoaderTest
 *
 * @author meidongxu@baidu.com
 */
public class PinpointURLClassLoaderTest {

    @Test
    public void testOnLoadClass() throws Exception {

        PinpointURLClassLoader cl =
                new PinpointURLClassLoader(new URL[] {}, Thread.currentThread().getContextClassLoader());
        try {
            cl.loadClass("test");
            Assert.fail();
        } catch (ClassNotFoundException e) {
        }

        //        try {
        //            cl.loadClass("com.baidu.oped.apm.profiler.DefaultAgent");
        //        } catch (ClassNotFoundException e) {
        //
        //        }
        // should be able to test using the above code, but it is not possible from bootstrap testcase.
        // it could be possible by specifying the full path to the URL classloader, but it would be harder to maintain.
        // for now, just test if DefaultAgent is specified to be loaded
        Assert.assertTrue(cl.onLoadClass("com.baidu.oped.apm.profiler.DefaultAgent"));
    }
}
