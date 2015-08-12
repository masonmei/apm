
package com.baidu.oped.apm.profiler.modifier.arcus.interceptor;

import com.baidu.oped.apm.bootstrap.interceptor.SimpleAroundInterceptor;
import com.baidu.oped.apm.bootstrap.interceptor.TargetClassLoader;
import com.baidu.oped.apm.bootstrap.logging.PLogger;
import com.baidu.oped.apm.bootstrap.logging.PLoggerFactory;
import com.baidu.oped.apm.bootstrap.util.MetaObject;

import net.spy.memcached.ops.Operation;

/**
 * class AddOpInterceptor 
 *
 * @author meidongxu@baidu.com
 */
public class AddOpInterceptor implements SimpleAroundInterceptor, TargetClassLoader {

    private final PLogger logger = PLoggerFactory.getLogger(this.getClass());
    private final boolean isDebug = logger.isDebugEnabled();

    private MetaObject<String> getServiceCode = new MetaObject<String>("__getServiceCode");
    private MetaObject<String> setServiceCode = new MetaObject<String>("__setServiceCode", String.class);

    @Override
    public void before(Object target, Object[] args) {
        if (isDebug) {
            logger.beforeInterceptor(target, args);
        }

        String serviceCode = getServiceCode.invoke(target);
        Operation op = (Operation) args[1];

        setServiceCode.invoke(op, serviceCode);
    }

    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
        if (isDebug) {
            logger.afterInterceptor(target, args, result, throwable);
        }
    }
}
