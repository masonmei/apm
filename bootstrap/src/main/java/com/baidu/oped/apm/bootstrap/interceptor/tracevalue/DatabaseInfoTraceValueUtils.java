package com.baidu.oped.apm.bootstrap.interceptor.tracevalue;

import com.baidu.oped.apm.bootstrap.context.DatabaseInfo;

/**
 * class DatabaseInfoTraceValueUtils
 *
 * @author meidongxu@baidu.com
 */
public final class DatabaseInfoTraceValueUtils {

    private DatabaseInfoTraceValueUtils() {
    }

    public static DatabaseInfo __getTraceDatabaseInfo(Object target, DatabaseInfo defaultValue) {
        if (target == null) {
            return defaultValue;
        }
        if (target instanceof DatabaseInfoTraceValue) {
            final DatabaseInfo databaseInfo = ((DatabaseInfoTraceValue) target).__getTraceDatabaseInfo();
            if (databaseInfo == null) {
                return defaultValue;
            }
            return databaseInfo;
        }
        return defaultValue;
    }

    public static void __setTraceDatabaseInfo(Object target, DatabaseInfo databaseInfo) {
        if (target == null) {
            return;
        }
        if (target instanceof DatabaseInfoTraceValue) {
            ((DatabaseInfoTraceValue) target).__setTraceDatabaseInfo(databaseInfo);
        }
    }
}
