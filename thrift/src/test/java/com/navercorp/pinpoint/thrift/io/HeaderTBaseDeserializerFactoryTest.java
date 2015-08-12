
package com.baidu.oped.apm.thrift.io;

import junit.framework.Assert;

import org.junit.Test;

import com.baidu.oped.apm.thrift.io.HeaderTBaseSerializerFactory;

/**
 * class HeaderTBaseDeserializerFactoryTest 
 *
 * @author meidongxu@baidu.com
 */
public class HeaderTBaseDeserializerFactoryTest {

    @Test
    public void optionTest1() {

        HeaderTBaseSerializerFactory factory = new HeaderTBaseSerializerFactory();
        Assert.assertTrue(factory.isSafetyGuranteed());
    }

    @Test
    public void optionTest2() {
        HeaderTBaseSerializerFactory factory = new HeaderTBaseSerializerFactory(true, 1);
        Assert.assertTrue(factory.isSafetyGuranteed());
    }

    @Test
    public void optionTest() {
        HeaderTBaseSerializerFactory factory = new HeaderTBaseSerializerFactory(false, 1);
        Assert.assertFalse(factory.isSafetyGuranteed());
    }
}
