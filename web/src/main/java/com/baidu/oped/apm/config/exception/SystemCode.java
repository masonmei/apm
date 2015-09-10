package com.baidu.oped.apm.config.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * class SystemCode
 *
 * @author meidongxu@baidu.com
 */

public enum SystemCode {
    OK(1),
    INVALID_PARAMETER(2),
    INVALID_PARAMETER_VALUE(3),
    INTERNAL_ERROR(4),
    EXCEED_MAX_RETURN_DATA_POINTS(5),
    EXCEED_MAX_QUERY_DATA_POINTS(6),
    AUTHENTICATION_ERROR(7),
    AUTHORIZATION_ERROR(8),
    ACCESS_DENIED(9);

    private static Map<Integer, SystemCode> hash = new HashMap<Integer, SystemCode>();
    private final int value;

    private SystemCode(int value) {
        this.value = value;
    }

    public static SystemCode getEnumItem(int value) {
        if (hash.isEmpty()) {
            initEnumHash();
        }
        return hash.get(value);
    }

    private static void initEnumHash() {
        for (SystemCode code : SystemCode.values()) {
            hash.put(code.getValue(), code);
        }
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        String packageName = getClass().getPackage().getName();
        String className = getClass().getName();
        className = className.replace("$", ".");
        return className.substring(packageName.length() + 1, className.length()) + "." + name();
    }
}
