
package com.baidu.oped.apm.profiler.modifier.tomcat.interceptor;

import com.baidu.oped.apm.bootstrap.interceptor.LifeCycleEventListener;
import com.baidu.oped.apm.bootstrap.interceptor.SimpleAroundInterceptor;
import com.baidu.oped.apm.bootstrap.interceptor.TargetClassLoader;
import com.baidu.oped.apm.bootstrap.logging.PLogger;
import com.baidu.oped.apm.bootstrap.logging.PLoggerFactory;

/**
 * class StandardServiceStopInterceptor 
 *
 * @author meidongxu@baidu.com
 */



public class StandardServiceStopInterceptor implements SimpleAroundInterceptor, TargetClassLoader {

    private final PLogger logger = PLoggerFactory.getLogger(this.getClass());
    private final boolean isDebug = logger.isDebugEnabled();

    private LifeCycleEventListener lifeCycleEventListener;

    public StandardServiceStopInterceptor(LifeCycleEventListener lifeCycleEventListener) {
        this.lifeCycleEventListener = lifeCycleEventListener;
    }

    @Override
    public void before(Object target, Object[] args) {

    }

    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
        if (isDebug) {
            logger.afterInterceptor(target, args, result, throwable);
        }
        // TODO Is stop() invoked when start failed?
        // if (!InterceptorUtils.isSuccess(result)) {
        // return;
        // }
        lifeCycleEventListener.stop();
    }
}
