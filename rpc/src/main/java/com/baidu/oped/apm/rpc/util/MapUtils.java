
package com.baidu.oped.apm.rpc.util;

import java.util.Map;

/**
 * class MapUtils 
 *
 * @author meidongxu@baidu.com
 */




public final class MapUtils {

    private MapUtils() {
    }

    public static String getString(Map<Object, Object> map, String key) {
        return getString(map, key, null);
    }

    public static String getString(Map<Object, Object> map, String key, String defaultValue) {
        if (map == null) {
            return defaultValue;
        }

        final Object value = map.get(key);
        if (value instanceof String) {
            return (String) value;
        }

        return defaultValue;
    }

    public static Boolean getBoolean(Map<Object, Object> map, String key) {
        return getBoolean(map, key, false);
    }

    public static Boolean getBoolean(Map<Object, Object> map, String key, Boolean defaultValue) {
        if (map == null) {
            return defaultValue;
        }

        final Object value = map.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }

        return defaultValue;
    }


    public static Integer getInteger(Map<Object, Object> map, String key) {
        return getInteger(map, key, null);
    }

    public static Integer getInteger(Map<Object, Object> map, String key, Integer defaultValue) {
        if (map == null) {
            return defaultValue;
        }

        final Object value = map.get(key);
        if (value instanceof Integer) {
            return (Integer) value;
        }

        return defaultValue;
    }
    
    public static Long getLong(Map<Object, Object> map, String key) {
        return getLong(map, key, null);
    }

    public static Long getLong(Map<Object, Object> map, String key, Long defaultValue) {
        if (map == null) {
            return defaultValue;
        }

        final Object value = map.get(key);
        if (value instanceof Long) {
            return (Long) value;
        }

        return defaultValue;
    }

}
