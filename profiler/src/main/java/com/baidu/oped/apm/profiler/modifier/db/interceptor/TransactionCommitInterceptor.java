
package com.baidu.oped.apm.profiler.modifier.db.interceptor;

import com.baidu.oped.apm.bootstrap.context.DatabaseInfo;
import com.baidu.oped.apm.bootstrap.context.RecordableTrace;
import com.baidu.oped.apm.bootstrap.interceptor.*;
import com.baidu.oped.apm.bootstrap.interceptor.tracevalue.DatabaseInfoTraceValueUtils;

/**
 * class TransactionCommitInterceptor 
 *
 * @author meidongxu@baidu.com
 */
public class TransactionCommitInterceptor extends SpanEventSimpleAroundInterceptor {


    public TransactionCommitInterceptor() {
        super(TransactionCommitInterceptor.class);
    }

    @Override
    protected void doInBeforeTrace(RecordableTrace trace, Object target, Object[] args) {
        trace.markBeforeTime();
    }


    @Override
    public void doInAfterTrace(RecordableTrace trace, Object target, Object[] args, Object result, Throwable throwable) {
        DatabaseInfo databaseInfo = DatabaseInfoTraceValueUtils.__getTraceDatabaseInfo(target, UnKnownDatabaseInfo.INSTANCE);

        trace.recordServiceType(databaseInfo.getType());
        trace.recordEndPoint(databaseInfo.getMultipleHost());
        trace.recordDestinationId(databaseInfo.getDatabaseId());

        trace.recordApi(getMethodDescriptor());
        trace.recordException(throwable);

        trace.markAfterTime();
    }
}
