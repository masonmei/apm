
package com.baidu.oped.apm.profiler.modifier.redis.interceptor;

import java.util.Map;

import com.baidu.oped.apm.bootstrap.context.RecordableTrace;
import com.baidu.oped.apm.bootstrap.interceptor.SpanEventSimpleAroundInterceptor;
import com.baidu.oped.apm.bootstrap.interceptor.TargetClassLoader;
import com.baidu.oped.apm.bootstrap.interceptor.tracevalue.MapTraceValue;
import com.baidu.oped.apm.common.ServiceType;

/**
 * Jedis Pipeline(redis client) method interceptor
 * 
 * @author jaehong.kim
 *
 */
public class JedisPipelineMethodInterceptor extends SpanEventSimpleAroundInterceptor implements TargetClassLoader {

    public JedisPipelineMethodInterceptor() {
        super(JedisPipelineMethodInterceptor.class);
    }

    @Override
    public void doInBeforeTrace(RecordableTrace trace, Object target, Object[] args) {
        trace.markBeforeTime();
    }

    @Override
    public void doInAfterTrace(RecordableTrace trace, Object target, Object[] args, Object result, Throwable throwable) {
        String endPoint = null;
        if (target instanceof MapTraceValue) {
            final Map<String, Object> traceValue = ((MapTraceValue) target).__getTraceBindValue();
            if (traceValue != null) {
                endPoint = (String) traceValue.get("endPoint");
            }
        }

        trace.recordApi(getMethodDescriptor());
        trace.recordEndPoint(endPoint != null ? endPoint : "Unknown");
        trace.recordDestinationId(ServiceType.REDIS.toString());
        trace.recordServiceType(ServiceType.REDIS);
        trace.recordException(throwable);
        trace.markAfterTime();
    }
}