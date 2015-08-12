
package com.baidu.oped.apm.profiler.modifier.db.interceptor;

import com.baidu.oped.apm.bootstrap.context.RecordableTrace;
import com.baidu.oped.apm.bootstrap.interceptor.*;
import com.baidu.oped.apm.common.ServiceType;

/**
 * Maybe we should trace get of Datasource.
 * @author emeroad
 */
public class DataSourceCloseInterceptor extends SpanEventSimpleAroundInterceptor {



    public DataSourceCloseInterceptor() {
        super(DataSourceCloseInterceptor.class);
    }

    @Override
    public void doInBeforeTrace(RecordableTrace trace, final Object target, Object[] args) {
        trace.markBeforeTime();
    }

    @Override
    public void doInAfterTrace(RecordableTrace trace, Object target, Object[] args, Object result, Throwable throwable) {
        trace.recordServiceType(ServiceType.DBCP);
        trace.recordApi(getMethodDescriptor());
        trace.recordException(throwable);

        trace.markAfterTime();
    }
}
