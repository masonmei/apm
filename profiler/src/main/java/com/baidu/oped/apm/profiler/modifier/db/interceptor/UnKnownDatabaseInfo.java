
package com.baidu.oped.apm.profiler.modifier.db.interceptor;

import com.baidu.oped.apm.bootstrap.context.DatabaseInfo;
import com.baidu.oped.apm.common.ServiceType;
import com.baidu.oped.apm.profiler.modifier.db.DefaultDatabaseInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * class UnKnownDatabaseInfo 
 *
 * @author meidongxu@baidu.com
 */
public class UnKnownDatabaseInfo {
    public static final DatabaseInfo INSTANCE;

    static{
        final List<String> urls = new ArrayList<String>();
        urls.add("unknown");
        INSTANCE = new DefaultDatabaseInfo(ServiceType.UNKNOWN_DB, ServiceType.UNKNOWN_DB_EXECUTE_QUERY, "unknown", "unknown", urls, "unknown");
    }
}
