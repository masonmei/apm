
package com.baidu.oped.apm.profiler.modifier.tomcat.interceptor;

import com.baidu.oped.apm.bootstrap.context.TraceContext;
import com.baidu.oped.apm.bootstrap.interceptor.SimpleAroundInterceptor;
import com.baidu.oped.apm.bootstrap.interceptor.TargetClassLoader;
import com.baidu.oped.apm.bootstrap.interceptor.TraceContextSupport;
import com.baidu.oped.apm.bootstrap.logging.PLogger;
import com.baidu.oped.apm.bootstrap.logging.PLoggerFactory;

import org.apache.catalina.connector.Connector;

/**
 * class ConnectorInitializeInterceptor 
 *
 * @author meidongxu@baidu.com
 */
public class ConnectorInitializeInterceptor implements SimpleAroundInterceptor, TraceContextSupport, TargetClassLoader {

    private PLogger logger = PLoggerFactory.getLogger(this.getClass());
    private final boolean isDebug = logger.isDebugEnabled();

    private TraceContext traceContext;
    
    @Override
    public void setTraceContext(TraceContext traceContext) {
        this.traceContext = traceContext;
    }

    @Override
    public void before(Object target, Object[] args) {

    }

    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
        if (isDebug) {
            logger.afterInterceptor(target, args, result, throwable);
        }
        Connector connector = (Connector) target;
        this.traceContext.getServerMetaDataHolder().addConnector(connector.getProtocol(), connector.getPort());

    }
}
