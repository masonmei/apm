
package com.baidu.oped.apm.profiler.modifier.db.mysql.interceptor;

import com.baidu.oped.apm.bootstrap.context.DatabaseInfo;
import com.baidu.oped.apm.bootstrap.context.Trace;
import com.baidu.oped.apm.bootstrap.context.TraceContext;
import com.baidu.oped.apm.bootstrap.interceptor.*;
import com.baidu.oped.apm.bootstrap.interceptor.tracevalue.DatabaseInfoTraceValue;
import com.baidu.oped.apm.bootstrap.logging.PLogger;
import com.baidu.oped.apm.bootstrap.logging.PLoggerFactory;
import com.baidu.oped.apm.bootstrap.util.InterceptorUtils;
import com.baidu.oped.apm.common.ServiceType;

/**
 * class MySQLConnectionCreateInterceptor 
 *
 * @author meidongxu@baidu.com
 */
public class MySQLConnectionCreateInterceptor implements SimpleAroundInterceptor, TraceContextSupport {

    private final PLogger logger = PLoggerFactory.getLogger(this.getClass());
    private final boolean isDebug = logger.isDebugEnabled();

    private TraceContext traceContext;


    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
        if (isDebug) {
            logger.afterInterceptor(target, args, result, throwable);
        }
        if (args == null || args.length != 5) {
            return;
        }

        final String hostToConnectTo = getString(args[0]);
        final Integer portToConnectTo = getInteger(args[1]);
        final String databaseId = getString(args[3]);
        // In case of loadbalance, connectUrl is modified. 
//        final String url = getString(args[4]);
        DatabaseInfo databaseInfo = null;
        if (hostToConnectTo != null && portToConnectTo != null && databaseId != null) {
            // It's dangerous to use this url directly
            databaseInfo = traceContext.createDatabaseInfo(ServiceType.MYSQL, ServiceType.MYSQL_EXECUTE_QUERY, hostToConnectTo, portToConnectTo, databaseId);
            if (InterceptorUtils.isSuccess(throwable)) {
                // Set only if connection is success.
                if (target instanceof DatabaseInfoTraceValue) {
                    ((DatabaseInfoTraceValue)target).__setTraceDatabaseInfo(databaseInfo);
                }
            }
        }

        final Trace trace = traceContext.currentTraceObject();
        if (trace == null) {
            return;
        }
        
        // We must do this if current transaction is being recorded.
        if (databaseInfo != null) {
            trace.recordServiceType(databaseInfo.getExecuteQueryType());
            trace.recordEndPoint(databaseInfo.getMultipleHost());
            trace.recordDestinationId(databaseInfo.getDatabaseId());
        }

    }

    private String getString(Object value) {
        if (value instanceof String) {
            return (String) value;
        }
        return null;
    }

    private Integer getInteger(Object value) {
        if (value instanceof Integer) {
            return (Integer) value;
        }
        return null;
    }

    @Override
    public void before(Object target, Object[] args) {

    }


    @Override
    public void setTraceContext(TraceContext traceContext) {
        this.traceContext = traceContext;
    }

}
