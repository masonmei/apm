package com.baidu.oped.apm.bootstrap.interceptor.tracevalue;

import com.baidu.oped.apm.bootstrap.context.DatabaseInfo;

/**
 * class DatabaseInfoTraceValue
 *
 * @author meidongxu@baidu.com
 */
public interface DatabaseInfoTraceValue extends TraceValue {
    void __setTraceDatabaseInfo(DatabaseInfo value);

    DatabaseInfo __getTraceDatabaseInfo();

}
