package com.baidu.oped.apm.bootstrap.interceptor;

/**
 * C-style API (doesn't return an object) in order to reduce the number of object instantiating
 *
 * @author emeroad
 */
public interface ParameterExtractor {
    public static final Object NULL = new Object();

    public static final int NOT_FOUND = -1;

    int getIndex();

    Object extractObject(Object[] parameterList);
}
