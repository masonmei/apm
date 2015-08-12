package com.baidu.oped.apm.bootstrap.config;

import java.io.IOException;

import org.junit.Test;

import junit.framework.Assert;

/**
 * class ProfilableClassFilterTest
 *
 * @author meidongxu@baidu.com
 */
public class ProfilableClassFilterTest {

    @Test
    public void testIsProfilableClassWithNoConfiguration() throws IOException {
        ProfilableClassFilter filter =
                new ProfilableClassFilter("com.baidu.oped.apm.testweb.controller.*,com.baidu.oped.apm.testweb.MyClass");

        Assert.assertFalse(filter.filter("com/baidu/oped/apm/testweb/controllers/MyController"));
        Assert.assertFalse(filter.filter("net/spider/king/wang/Jjang"));
        Assert.assertFalse(filter.filter("com/baidu/oped/apm/testweb2/controller/MyController"));
        Assert.assertFalse(filter.filter("com/baidu/oped/apm/testweb2/MyClass"));
    }

    /**
     * <pre>
     * configuration is
     * profile.package.include=com.baidu.oped.apm.testweb.controller.*,com.baidu.oped.apm.testweb.MyClass
     * </pre>
     *
     * @throws IOException
     */
    @Test
    public void testIsProfilableClass() throws IOException {
        ProfilableClassFilter filter =
                new ProfilableClassFilter("com.baidu.oped.apm.testweb.controller.*,com.baidu.oped.apm.testweb.MyClass");

        Assert.assertTrue(filter.filter("com/baidu/oped/apm/testweb/MyClass"));
        Assert.assertTrue(filter.filter("com/baidu/oped/apm/testweb/controller/MyController"));
        Assert.assertTrue(filter.filter("com/baidu/oped/apm/testweb/controller/customcontroller/MyCustomController"));

        Assert.assertFalse(filter.filter("com/baidu/oped/apm/testweb/MyUnknownClass"));
        Assert.assertFalse(filter.filter("com/baidu/oped/apm/testweb/controller2/MyController"));
    }

}