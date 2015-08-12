package com.baidu.oped.apm.bootstrap.util;

import org.junit.Test;

import junit.framework.Assert;

/**
 * class MetaObjectTest
 *
 * @author meidongxu@baidu.com
 */
public class MetaObjectTest {
    private String test;

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    @Test
    public void testSetInvoke() throws Exception {
        MetaObjectTest test = new MetaObjectTest();

        MetaObject metaObject = new MetaObject("setTest", String.class);
        Object result = metaObject.invoke(test, "set");
        Assert.assertEquals(test.getTest(), "set");
        Assert.assertNull(result);
    }

    @Test
    public void testGetInvoke() throws Exception {
        MetaObjectTest test = new MetaObjectTest();
        test.setTest("get");

        MetaObject metaObject = new MetaObject("getTest");
        Object result = metaObject.invoke(test);
        Assert.assertEquals(test.getTest(), result);
    }
}
