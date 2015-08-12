package com.baidu.oped.apm.bootstrap.plugin;

import org.junit.Test;

import junit.framework.Assert;

/**
 * class BytecodeUtilsTest
 *
 * @author meidongxu@baidu.com
 */
public class BytecodeUtilsTest {

    @Test
    public void testDefineClass() throws Exception {

    }

    @Test
    public void testGetClassFile() throws Exception {

    }

    @Test
    public void testGetClassFile_SystemClassLoader() {
        // SystemClassLoader class
        Class<String> stringClass = String.class;
        byte[] stringClassBytes = BytecodeUtils.getClassFile(stringClass.getClassLoader(), stringClass.getName());
        Assert.assertNotNull(stringClassBytes);
    }
}