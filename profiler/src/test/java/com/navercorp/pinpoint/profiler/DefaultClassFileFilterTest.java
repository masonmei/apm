
package com.baidu.oped.apm.profiler;

import junit.framework.Assert;

import org.junit.Test;

import com.baidu.oped.apm.profiler.ClassFileFilter;
import com.baidu.oped.apm.profiler.DefaultClassFileFilter;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * class DefaultClassFileFilterTest 
 *
 * @author meidongxu@baidu.com
 */
public class DefaultClassFileFilterTest {

    @Test
    public void testDoFilter_Package() throws Exception {
        ClassFileFilter filter = new DefaultClassFileFilter(this.getClass().getClassLoader());

        Assert.assertTrue(filter.doFilter(null, "java/test", null, null, null));
        Assert.assertTrue(filter.doFilter(null, "javax/test", null, null, null));
        Assert.assertTrue(filter.doFilter(null, "com/baidu/oped/apm/", null, null, null));

        Assert.assertFalse(filter.doFilter(null, "test", null, null, null));
    }


    @Test
    public void testDoFilter_ClassLoader() throws Exception {
        ClassFileFilter filter = new DefaultClassFileFilter(this.getClass().getClassLoader());


        Assert.assertTrue(filter.doFilter(this.getClass().getClassLoader(), "test", null, null, null));

        URLClassLoader classLoader = new URLClassLoader(new URL[]{});
        Assert.assertFalse(filter.doFilter(classLoader, "test", null, null, null));
    }
}