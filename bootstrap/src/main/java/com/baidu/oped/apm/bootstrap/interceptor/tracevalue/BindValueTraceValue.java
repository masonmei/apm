package com.baidu.oped.apm.bootstrap.interceptor.tracevalue;

import java.util.Map;

/**
 * class BindValueTraceValue
 *
 * @author meidongxu@baidu.com
 */
public interface BindValueTraceValue extends TraceValue {
    void __setTraceBindValue(Map<Integer, String> value);

    Map<Integer, String> __getTraceBindValue();
}
