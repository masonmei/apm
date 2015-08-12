
package com.baidu.oped.apm.profiler.modifier;

import java.security.ProtectionDomain;

/**
 * class Modifier 
 *
 * @author meidongxu@baidu.com
 */
public interface Modifier {
    byte[] modify(ClassLoader classLoader, String className, ProtectionDomain protectedDomain, byte[] classFileBuffer);
}
