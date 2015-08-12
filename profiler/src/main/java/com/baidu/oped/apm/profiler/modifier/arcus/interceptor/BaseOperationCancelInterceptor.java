
package com.baidu.oped.apm.profiler.modifier.arcus.interceptor;

import com.baidu.oped.apm.bootstrap.context.AsyncTrace;
import com.baidu.oped.apm.bootstrap.interceptor.SimpleAroundInterceptor;
import com.baidu.oped.apm.bootstrap.logging.PLogger;
import com.baidu.oped.apm.bootstrap.logging.PLoggerFactory;
import com.baidu.oped.apm.bootstrap.util.MetaObject;
import com.baidu.oped.apm.bootstrap.util.TimeObject;
import com.baidu.oped.apm.profiler.context.DefaultAsyncTrace;

import net.spy.memcached.protocol.BaseOperationImpl;

/**
 * class BaseOperationCancelInterceptor 
 *
 * @author meidongxu@baidu.com
 */
@Deprecated
public class BaseOperationCancelInterceptor implements SimpleAroundInterceptor {

    private final PLogger logger = PLoggerFactory.getLogger(this.getClass());
    private final boolean isDebug = logger.isDebugEnabled();

    private MetaObject getAsyncTrace = new MetaObject("__getAsyncTrace");

    @Override
    public void before(Object target, Object[] args) {
        if (isDebug) {
            logger.beforeInterceptor(target, args);
        }

        AsyncTrace asyncTrace = (AsyncTrace) getAsyncTrace.invoke(target);
        if (asyncTrace == null) {
            logger.debug("asyncTrace not found ");
            return;
        }

        if (asyncTrace.getState() != DefaultAsyncTrace.STATE_INIT) {
            // Operation already completed.
            return;
        }

        BaseOperationImpl baseOperation = (BaseOperationImpl) target;
        if (!baseOperation.isCancelled()) {
            TimeObject timeObject = (TimeObject) asyncTrace.getAttachObject();
            timeObject.markCancelTime();
        }
    }

    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {

    }
}
