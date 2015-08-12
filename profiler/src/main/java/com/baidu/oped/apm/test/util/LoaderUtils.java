
package com.baidu.oped.apm.test.util;

import javassist.ClassPool;
import javassist.Loader;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * class LoaderUtils 
 *
 * @author meidongxu@baidu.com
 */
public final class LoaderUtils {

    private static final SecurityManager SECURITY_MANAGER = System.getSecurityManager();

    private LoaderUtils() {
    }

    public static Loader createLoader(final ClassPool classPool) {
        if (classPool == null) {
            throw new NullPointerException("classPool must not be null");
        }
        Loader loader;
        if (SECURITY_MANAGER != null) {
            loader = AccessController.doPrivileged(new PrivilegedAction<Loader>() {
                public Loader run() {
                    return new Loader(classPool);
                }
            });
        } else {
            loader = new Loader(classPool);
        }
        loader.delegateLoadingOf("org.apache.log4j.");
        return loader;
    }

}
