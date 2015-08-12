
package com.baidu.oped.apm.test;

import com.baidu.oped.apm.bootstrap.plugin.BytecodeUtils;
import com.baidu.oped.apm.profiler.modifier.Modifier;

/**
 * class ClassTransformHelper 
 *
 * @author meidongxu@baidu.com
 */
public class ClassTransformHelper {


    public static Class<?> transformClass(ClassLoader classLoader, String className, Modifier modifier) {
        final byte[] original = BytecodeUtils.getClassFile(classLoader, className);
        final byte[] transformed = modifier.modify(classLoader, className, null, original);
        
        return BytecodeUtils.defineClass(classLoader, className, transformed);
    }

}
