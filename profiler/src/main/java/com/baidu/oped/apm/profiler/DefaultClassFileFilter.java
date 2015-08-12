
package com.baidu.oped.apm.profiler;

import java.security.ProtectionDomain;

/**
 * class DefaultClassFileFilter 
 *
 * @author meidongxu@baidu.com
 */
public class DefaultClassFileFilter implements ClassFileFilter {

    private final ClassLoader agentLoader;

    public DefaultClassFileFilter(ClassLoader agentLoader) {
        if (agentLoader == null) {
            throw new NullPointerException("agentLoader must not be null");
        }
        this.agentLoader = agentLoader;
    }

    @Override
    public boolean doFilter(ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classFileBuffer) {
        // fast skip java classes
        if (className.startsWith("java")) {
            if (className.startsWith("/", 4) || className.startsWith("x/", 4)) {
                return SKIP;
            }
        }

        if (classLoader == agentLoader) {
            // skip classes loaded by agent class loader.
            return SKIP;
        }

        // Skip pinpoint packages too.
        if (className.startsWith("com/baidu/oped/apm/")) {
            return SKIP;
        }
        return CONTINUE;
    }
}
