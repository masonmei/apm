
package com.baidu.oped.apm.profiler.context;

import com.baidu.oped.apm.bootstrap.context.Trace;
import com.baidu.oped.apm.bootstrap.context.TraceId;

/**
 * class TraceFactory 
 *
 * @author meidongxu@baidu.com
 */
public interface TraceFactory {
    Trace currentTraceObject();

    Trace currentRpcTraceObject();

    Trace currentRawTraceObject();

    Trace disableSampling();

    // picked as sampling target at remote
    Trace continueTraceObject(TraceId traceID);

    Trace newTraceObject();

    void detachTraceObject();
}
