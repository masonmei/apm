package com.baidu.oped.apm.bootstrap.instrument;

import com.baidu.oped.apm.bootstrap.interceptor.MethodDescriptor;

/**
 * class MethodInfo
 *
 * @author meidongxu@baidu.com
 */
public interface MethodInfo {
    String getName();

    String[] getParameterTypes();

    int getModifiers();

    boolean isConstructor();

    MethodDescriptor getDescriptor();
}
