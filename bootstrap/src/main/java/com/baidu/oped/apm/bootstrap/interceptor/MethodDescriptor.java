package com.baidu.oped.apm.bootstrap.interceptor;

/**
 * class MethodDescriptor
 *
 * @author meidongxu@baidu.com
 */
public interface MethodDescriptor {
    String getMethodName();

    String getClassName();

    String[] getParameterTypes();

    String[] getParameterVariableName();

    String getParameterDescriptor();

    int getLineNumber();

    String getFullName();

    int getApiId();

    void setApiId(int apiId);

    String getApiDescriptor();
}
