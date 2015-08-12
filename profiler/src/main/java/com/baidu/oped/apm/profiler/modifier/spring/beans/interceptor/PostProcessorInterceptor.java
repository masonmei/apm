
package com.baidu.oped.apm.profiler.modifier.spring.beans.interceptor;

import com.baidu.oped.apm.bootstrap.logging.PLogger;
import com.baidu.oped.apm.bootstrap.logging.PLoggerFactory;
import com.baidu.oped.apm.profiler.ClassFileRetransformer;
import com.baidu.oped.apm.profiler.modifier.Modifier;

/**
 * class PostProcessorInterceptor 
 *
 * @author meidongxu@baidu.com
 */
public class PostProcessorInterceptor extends AbstractSpringBeanCreationInterceptor {
    private final PLogger logger = PLoggerFactory.getLogger(getClass());
    
    public PostProcessorInterceptor(ClassFileRetransformer retransformer, Modifier modifier, TargetBeanFilter filter) {
        super(retransformer, modifier, filter);
    }

    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
        try {
            Object bean = result;
            String beanName = (String)args[1];
            
            processBean(beanName, bean);
        } catch (Throwable t) {
            logger.warn("Unexpected exception", t);
        }
    }
}
