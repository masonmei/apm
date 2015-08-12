
package com.baidu.oped.apm.profiler.interceptor.bci;

import org.junit.Assert;
import org.junit.Test;

import com.baidu.oped.apm.profiler.interceptor.bci.CodeBuilder;

/**
 * class CodeBuilderTest 
 *
 * @author meidongxu@baidu.com
 */
public class CodeBuilderTest {
    @Test
    public void testCodeBuilder() throws Exception {
        CodeBuilder builder = new CodeBuilder();
        builder.begin();
        builder.format("1");
        builder.end();
        Assert.assertEquals("{1}", builder.toString());
    }

    @Test
    public void testFormat() throws Exception {
        CodeBuilder builder = new CodeBuilder();
        builder.begin();
        builder.format("1");
        builder.format("2");
        builder.end();
        Assert.assertEquals("{12}", builder.toString());
    }

    @Test
    public void testFormatAppend() throws Exception {
        CodeBuilder builder = new CodeBuilder();
        builder.begin();
        builder.format("1");
        builder.append("2");
        builder.end();
        Assert.assertEquals("{12}", builder.toString());
    }


}
