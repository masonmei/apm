
package com.baidu.oped.apm.profiler.modifier.db.interceptor;

import com.baidu.oped.apm.bootstrap.context.DatabaseInfo;
import com.baidu.oped.apm.bootstrap.context.RecordableTrace;
import com.baidu.oped.apm.bootstrap.interceptor.*;
import com.baidu.oped.apm.bootstrap.interceptor.tracevalue.DatabaseInfoTraceValueUtils;

/**
 * protected int executeUpdate(String sql, boolean isBatch, boolean returnGeneratedKeys)
 *
 * @author netspider
 * @author emeroad
 */
public class StatementExecuteUpdateInterceptor extends SpanEventSimpleAroundInterceptor {

    public StatementExecuteUpdateInterceptor() {
        super(StatementExecuteUpdateInterceptor.class);
    }

    @Override
    public void doInBeforeTrace(RecordableTrace trace, Object target, Object[] args) {

        trace.markBeforeTime();

        DatabaseInfo databaseInfo = DatabaseInfoTraceValueUtils.__getTraceDatabaseInfo(target, UnKnownDatabaseInfo.INSTANCE);

        trace.recordServiceType(databaseInfo.getExecuteQueryType());
        trace.recordEndPoint(databaseInfo.getMultipleHost());
        trace.recordDestinationId(databaseInfo.getDatabaseId());

        trace.recordApi(getMethodDescriptor());
        if (args != null && args.length > 0) {
            Object arg = args[0];
            if (arg instanceof String) {
                trace.recordSqlInfo((String) arg);
            }
        }
    }


    @Override
    public void doInAfterTrace(RecordableTrace trace, Object target, Object[] args, Object result, Throwable throwable) {
        trace.recordException(throwable);

        // TODO need to find result, execution time
        trace.markAfterTime();
    }

}
