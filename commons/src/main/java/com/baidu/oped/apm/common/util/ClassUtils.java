
package com.baidu.oped.apm.common.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * 获取类实例的属性值(如果父类和子类有相同的属性，则只取子类)
     *
     * @param clazz
     *            类名
     * @param includeParentClass
     *            是否包括父类的属性值
     * @return 属性名=属性
     */
    public static Map<String, Field> getClassFields(Class<?> clazz, boolean includeParentClass) {
        Map<String, Field> map = new HashMap<String, Field>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            map.put(field.getName(), field);
        }
        if (includeParentClass)
            getParentClassFields(map, clazz.getSuperclass());
        return map;
    }

    /**
     * 获取类实例的父类的属性值
     *
     * @param map
     *            类实例的属性值Map
     * @param clazz
     *            类名
     * @return 属性名=属性
     */
    private static void getParentClassFields(Map<String, Field> map, Class<?> clazz) {
        if (clazz == null) {
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!map.containsKey(field.getName())) {
                map.put(field.getName(), field);
            }
        }
        if (clazz.getSuperclass() == null) {
            return;
        }
        getParentClassFields(map, clazz.getSuperclass());
    }

}
