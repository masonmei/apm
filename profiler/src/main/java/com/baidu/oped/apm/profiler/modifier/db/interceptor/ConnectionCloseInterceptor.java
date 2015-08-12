
package com.baidu.oped.apm.profiler.modifier.db.interceptor;

import com.baidu.oped.apm.bootstrap.interceptor.SimpleAroundInterceptor;
import com.baidu.oped.apm.bootstrap.interceptor.tracevalue.DatabaseInfoTraceValueUtils;
import com.baidu.oped.apm.bootstrap.logging.PLogger;
import com.baidu.oped.apm.bootstrap.logging.PLoggerFactory;

/**
 * class ConnectionCloseInterceptor 
 *
 * @author meidongxu@baidu.com
 */
public class ConnectionCloseInterceptor implements SimpleAroundInterceptor {

    private final PLogger logger = PLoggerFactory.getLogger(this.getClass());
    private final boolean isDebug = logger.isDebugEnabled();


    @Override
    public void before(Object target, Object[] args) {
        if (isDebug) {
            logger.beforeInterceptor(target, args);
        }
        // In case of close, we have to delete data even if the invocation failed.
        DatabaseInfoTraceValueUtils.__setTraceDatabaseInfo(target, null);
    }

    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
    }
}
