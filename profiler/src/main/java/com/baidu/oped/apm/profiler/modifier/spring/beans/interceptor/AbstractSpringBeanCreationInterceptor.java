
package com.baidu.oped.apm.profiler.modifier.spring.beans.interceptor;

import com.baidu.oped.apm.bootstrap.interceptor.SimpleAroundInterceptor;
import com.baidu.oped.apm.bootstrap.logging.PLogger;
import com.baidu.oped.apm.bootstrap.logging.PLoggerFactory;
import com.baidu.oped.apm.profiler.ClassFileRetransformer;
import com.baidu.oped.apm.profiler.ProfilerException;
import com.baidu.oped.apm.profiler.modifier.Modifier;

/**
 * class AbstractSpringBeanCreationInterceptor 
 *
 * @author meidongxu@baidu.com
 */
public abstract class AbstractSpringBeanCreationInterceptor implements SimpleAroundInterceptor {
    private final PLogger logger = PLoggerFactory.getLogger(getClass());
    
    private final ClassFileRetransformer retransformer;
    private final Modifier modifier;
    private final TargetBeanFilter filter;
    
    protected AbstractSpringBeanCreationInterceptor(ClassFileRetransformer retransformer, Modifier modifier, TargetBeanFilter filter) {
        this.retransformer = retransformer;
        this.modifier = modifier;
        this.filter = filter;
    }

    protected final void processBean(String beanName, Object bean) {
        if (bean == null) {
            return;
        }
        
        Class<?> clazz = bean.getClass();
        
        if (!filter.isTarget(beanName, clazz)) {
            return;
        }
        
        // If you want to trace inherited methods, you have to retranform super classes, too.
        
        try {
            retransformer.retransform(clazz, modifier);

            if (logger.isInfoEnabled()) {
                logger.info("Retransform {}", clazz.getName());
            }
        } catch (ProfilerException e) {
            logger.warn("Fail to retransform: {}", clazz.getName(), e);
            return;
        }
        
        filter.addTransformed(clazz);
    }

    @Override
    public final void before(Object target, Object[] args) {
        // do nothing
    }
}
