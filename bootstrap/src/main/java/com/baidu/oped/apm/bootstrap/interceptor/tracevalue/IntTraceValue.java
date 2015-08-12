package com.baidu.oped.apm.bootstrap.interceptor.tracevalue;

/**
 * class IntTraceValue
 *
 * @author meidongxu@baidu.com
 */
public interface IntTraceValue extends TraceValue {
    void __setTraceInt(int value);

    int __getTraceInt();
}
