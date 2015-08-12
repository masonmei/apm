
package com.baidu.oped.apm.profiler.modifier.arcus.interceptor;

import com.baidu.oped.apm.bootstrap.interceptor.SimpleAroundInterceptor;
import com.baidu.oped.apm.bootstrap.interceptor.TargetClassLoader;
import com.baidu.oped.apm.bootstrap.logging.PLogger;
import com.baidu.oped.apm.bootstrap.logging.PLoggerFactory;
import com.baidu.oped.apm.bootstrap.util.MetaObject;

import net.spy.memcached.ops.Operation;

/**
 * class FutureSetOperationInterceptor 
 *
 * @author meidongxu@baidu.com
 */
public class FutureSetOperationInterceptor implements SimpleAroundInterceptor, TargetClassLoader {

    private final PLogger logger = PLoggerFactory.getLogger(this.getClass());
    private final boolean isDebug = logger.isDebugEnabled();

    private MetaObject<Object> setOperation = new MetaObject<Object>("__setOperation", Operation.class);
    

    @Override
    public void before(Object target, Object[] args) {
        if (isDebug) {
            logger.beforeInterceptor(target, args);
        }

        setOperation.invoke(target, (Operation) args[0]);
    }

    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
    }
}
