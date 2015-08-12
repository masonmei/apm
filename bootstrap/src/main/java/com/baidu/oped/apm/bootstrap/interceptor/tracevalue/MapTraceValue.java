package com.baidu.oped.apm.bootstrap.interceptor.tracevalue;

import java.util.Map;

/**
 * class MapTraceValue
 *
 * @author meidongxu@baidu.com
 */
public interface MapTraceValue extends TraceValue {
    void __setTraceBindValue(Map<String, Object> value);

    Map<String, Object> __getTraceBindValue();
}
