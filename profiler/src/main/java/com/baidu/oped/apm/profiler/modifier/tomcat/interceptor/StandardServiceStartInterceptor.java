
package com.baidu.oped.apm.profiler.modifier.tomcat.interceptor;

import org.apache.catalina.util.ServerInfo;

import com.baidu.oped.apm.bootstrap.context.TraceContext;
import com.baidu.oped.apm.bootstrap.interceptor.LifeCycleEventListener;
import com.baidu.oped.apm.bootstrap.interceptor.SimpleAroundInterceptor;
import com.baidu.oped.apm.bootstrap.interceptor.TargetClassLoader;
import com.baidu.oped.apm.bootstrap.interceptor.TraceContextSupport;
import com.baidu.oped.apm.bootstrap.logging.PLogger;
import com.baidu.oped.apm.bootstrap.logging.PLoggerFactory;

/**
 * class StandardServiceStartInterceptor 
 *
 * @author meidongxu@baidu.com
 */



public class StandardServiceStartInterceptor implements SimpleAroundInterceptor, TraceContextSupport, TargetClassLoader {

    private final PLogger logger = PLoggerFactory.getLogger(this.getClass());
    private final boolean isDebug = logger.isDebugEnabled();

    private TraceContext traceContext;

    private LifeCycleEventListener lifeCycleEventListener;

    public StandardServiceStartInterceptor(LifeCycleEventListener lifeCycleEventListener) {
        this.lifeCycleEventListener = lifeCycleEventListener;
    }

    @Override
    public void before(Object target, Object[] args) {
        // Do nothing
    }

    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
        if (isDebug) {
            logger.afterInterceptor(target, args, result, throwable);
        }
        populateMetaData();
        lifeCycleEventListener.start();
    }

    private void populateMetaData() {
        String serverInfo = ServerInfo.getServerInfo();
        this.traceContext.getServerMetaDataHolder().setServerName(serverInfo);
    }

    @Override
    public void setTraceContext(TraceContext traceContext) {
        this.traceContext = traceContext;
    }
}
