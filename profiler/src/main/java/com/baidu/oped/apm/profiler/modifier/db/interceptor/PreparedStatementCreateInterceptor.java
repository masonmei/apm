
package com.baidu.oped.apm.profiler.modifier.db.interceptor;

import com.baidu.oped.apm.bootstrap.context.DatabaseInfo;
import com.baidu.oped.apm.bootstrap.context.RecordableTrace;
import com.baidu.oped.apm.bootstrap.interceptor.*;
import com.baidu.oped.apm.bootstrap.interceptor.tracevalue.DatabaseInfoTraceValue;
import com.baidu.oped.apm.bootstrap.interceptor.tracevalue.DatabaseInfoTraceValueUtils;
import com.baidu.oped.apm.bootstrap.interceptor.tracevalue.ParsingResultTraceValue;
import com.baidu.oped.apm.bootstrap.util.InterceptorUtils;
import com.baidu.oped.apm.common.util.ParsingResult;

/**
 * class PreparedStatementCreateInterceptor 
 *
 * @author meidongxu@baidu.com
 */
public class PreparedStatementCreateInterceptor extends SpanEventSimpleAroundInterceptor {


    public PreparedStatementCreateInterceptor() {
        super(PreparedStatementCreateInterceptor.class);
    }

    @Override
    public void doInBeforeTrace(RecordableTrace trace, Object target, Object[] args)  {
        trace.markBeforeTime();

        final DatabaseInfo databaseInfo = DatabaseInfoTraceValueUtils.__getTraceDatabaseInfo(target, UnKnownDatabaseInfo.INSTANCE);
        trace.recordServiceType(databaseInfo.getType());
        trace.recordEndPoint(databaseInfo.getMultipleHost());
        trace.recordDestinationId(databaseInfo.getDatabaseId());
    }

    @Override
    protected void prepareAfterTrace(Object target, Object[] args, Object result, Throwable throwable) {
        final boolean success = InterceptorUtils.isSuccess(throwable);
        if (success) {
            if (target instanceof DatabaseInfoTraceValue) {
                // set databaeInfo to PreparedStatement only when preparedStatment is generated successfully. 
                DatabaseInfo databaseInfo = ((DatabaseInfoTraceValue) target).__getTraceDatabaseInfo();
                if (databaseInfo != null) {
                    if (result instanceof DatabaseInfoTraceValue) {
                        ((DatabaseInfoTraceValue) result).__setTraceDatabaseInfo(databaseInfo);
                    }
                }
            }
            if (result instanceof ParsingResultTraceValue) {
                // 1. Don't check traceContext. preparedStatement can be created in other thread.
                // 2. While sampling is active, the thread which creates preparedStatement could not be a sampling target. So record sql anyway. 
                String sql = (String) args[0];
                ParsingResult parsingResult = getTraceContext().parseSql(sql);
                if (parsingResult != null) {
                    ((ParsingResultTraceValue)result).__setTraceParsingResult(parsingResult);
                } else {
                    if (logger.isErrorEnabled()) {
                        logger.error("sqlParsing fail. parsingResult is null sql:{}", sql);
                    }
                }
            }
        }
    }

    @Override
    public void doInAfterTrace(RecordableTrace trace, Object target, Object[] args, Object result, Throwable throwable) {
        if (result instanceof ParsingResultTraceValue) {
            ParsingResult parsingResult = ((ParsingResultTraceValue) result).__getTraceParsingResult();
            trace.recordSqlParsingResult(parsingResult);
        }
        trace.recordException(throwable);
        trace.recordApi(getMethodDescriptor());

        trace.markAfterTime();
    }


}
