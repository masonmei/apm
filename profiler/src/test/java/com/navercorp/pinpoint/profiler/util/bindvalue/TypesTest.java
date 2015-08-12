
package com.baidu.oped.apm.profiler.util.bindvalue;

import com.baidu.oped.apm.profiler.util.bindvalue.Types;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * class TypesTest 
 *
 * @author meidongxu@baidu.com
 */
public class TypesTest {

    @Test
    public void testInverse() throws Exception {
        Map<Integer, String> inverse = Types.inverse();
        Field[] fields = java.sql.Types.class.getFields();
        Assert.assertEquals(inverse.size(), fields.length);
    }
}
