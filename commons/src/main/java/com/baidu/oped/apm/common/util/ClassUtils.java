
package com.baidu.oped.apm.common.util;

/**
 * class ClassUtils 
 *
 * @author meidongxu@baidu.com
 */



public final class ClassUtils {
    
    private static final Object CLASS_NOT_LOADED = null;
    private static final char PACKAGE_SEPARATOR = '.';

    private ClassUtils() {
    }

    public static boolean isLoaded(String name) {
        return isLoaded(name, ClassLoaderUtils.getDefaultClassLoader());
    }
    
    public static boolean isLoaded(String name, ClassLoader classLoader) {
        if (name == null) {
            throw new IllegalArgumentException("name must not be null");
        }
        ClassLoader classLoaderToUse = classLoader;
        if (classLoaderToUse == null) {
            classLoaderToUse = ClassLoaderUtils.getDefaultClassLoader();
        }
        try {
            return (classLoaderToUse.loadClass(name) != CLASS_NOT_LOADED);
        } catch (ClassNotFoundException ignore) {
            // Swallow
        }
        return false;
    }
    
    public static String getPackageName(String fqcn) {
        if (fqcn == null) {
            throw new IllegalArgumentException("fully-qualified class name must not be null");
        }
        final int lastPackageSeparatorIndex = fqcn.lastIndexOf(PACKAGE_SEPARATOR);
        if (lastPackageSeparatorIndex == -1) {
            return "";
        }
        return fqcn.substring(0, lastPackageSeparatorIndex);
    }
}
