
package com.baidu.oped.apm.profiler;

import java.security.ProtectionDomain;

/**
 * class ClassFileFilter 
 *
 * @author meidongxu@baidu.com
 */
public interface ClassFileFilter {
    public static final boolean SKIP = true;
    public static final boolean CONTINUE = false;

    boolean doFilter(ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classFileBuffer);
}
