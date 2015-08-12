
package com.baidu.oped.apm.profiler.modifier.arcus.interceptor;

import com.baidu.oped.apm.bootstrap.interceptor.SimpleAroundInterceptor;
import com.baidu.oped.apm.bootstrap.logging.PLogger;
import com.baidu.oped.apm.bootstrap.logging.PLoggerFactory;
import com.baidu.oped.apm.bootstrap.util.MetaObject;

import net.sf.ehcache.Element;

/**
 * class FrontCacheGetFutureConstructInterceptor 
 *
 * @author meidongxu@baidu.com
 */
public class FrontCacheGetFutureConstructInterceptor implements SimpleAroundInterceptor {

    private final PLogger logger = PLoggerFactory.getLogger(this.getClass());
    private final boolean isDebug = logger.isDebugEnabled();

    // TODO This should be extracted from FrontCacheMemcachedClient.
    private static final String DEFAULT_FRONTCACHE_NAME = "front";

    private MetaObject<Object> setCacheName = new MetaObject<Object>("__setCacheName", String.class);
    private MetaObject<Object> setCacheKey = new MetaObject<Object>("__setCacheKey", String.class);

    @Override
    public void before(Object target, Object[] args) {
        // do nothing.
    }

    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
        if (isDebug) {
            logger.afterInterceptor(target, args, result, throwable);
        }

        try {
            setCacheName.invoke(target, DEFAULT_FRONTCACHE_NAME);

            if (args[0] instanceof Element) {
                Element element = (Element) args[0];
                setCacheKey.invoke(target, element.getObjectKey());
            }
        } catch (Exception e) {
            logger.error("failed to add metadata: {}", e);
        }
    }
}
