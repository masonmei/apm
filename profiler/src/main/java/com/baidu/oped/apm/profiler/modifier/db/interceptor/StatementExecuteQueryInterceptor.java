
package com.baidu.oped.apm.profiler.modifier.db.interceptor;

import com.baidu.oped.apm.bootstrap.context.DatabaseInfo;
import com.baidu.oped.apm.bootstrap.context.RecordableTrace;
import com.baidu.oped.apm.bootstrap.interceptor.*;
import com.baidu.oped.apm.bootstrap.interceptor.tracevalue.DatabaseInfoTraceValueUtils;

/**
 * class StatementExecuteQueryInterceptor 
 *
 * @author meidongxu@baidu.com
 */
public class StatementExecuteQueryInterceptor extends SpanEventSimpleAroundInterceptor {



    public StatementExecuteQueryInterceptor() {
        super(StatementExecuteQueryInterceptor.class);
    }

    @Override
    public void doInBeforeTrace(RecordableTrace trace, final Object target, Object[] args) {
        trace.markBeforeTime();
        /**
         * If method was not called by request handler, we skip tagging.
         */
        DatabaseInfo databaseInfo = DatabaseInfoTraceValueUtils.__getTraceDatabaseInfo(target, UnKnownDatabaseInfo.INSTANCE);

        trace.recordServiceType(databaseInfo.getExecuteQueryType());
        trace.recordEndPoint(databaseInfo.getMultipleHost());
        trace.recordDestinationId(databaseInfo.getDatabaseId());

    }


    @Override
    public void doInAfterTrace(RecordableTrace trace, Object target, Object[] args, Object result, Throwable throwable) {

        trace.recordApi(getMethodDescriptor());
        if (args.length > 0) {
            Object arg = args[0];
            if (arg instanceof String) {
                trace.recordSqlInfo((String) arg);
                // TODO more parsing result processing
            }
        }
        trace.recordException(throwable);
        trace.markAfterTime();

    }

}
