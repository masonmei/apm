
package com.baidu.oped.apm.rpc;

import org.junit.Test;

import com.baidu.oped.apm.rpc.ClassPreLoader;

/**
 * class ClassPreLoaderTest 
 *
 * @author meidongxu@baidu.com
 */
public class ClassPreLoaderTest {
    @Test
    public void testPreload() throws Exception {
        ClassPreLoader.preload();
    }
}
