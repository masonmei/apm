
package com.baidu.oped.apm.profiler.modifier.spring.beans.interceptor;

import java.lang.reflect.Method;

import com.baidu.oped.apm.bootstrap.logging.PLogger;
import com.baidu.oped.apm.bootstrap.logging.PLoggerFactory;
import com.baidu.oped.apm.profiler.ClassFileRetransformer;
import com.baidu.oped.apm.profiler.modifier.Modifier;

/**
 * class CreateBeanInstanceInterceptor 
 *
 * @author meidongxu@baidu.com
 */
public class CreateBeanInstanceInterceptor extends AbstractSpringBeanCreationInterceptor {
    private final PLogger logger = PLoggerFactory.getLogger(getClass());
    
    public CreateBeanInstanceInterceptor(ClassFileRetransformer retransformer, Modifier modifier, TargetBeanFilter filter) {
        super(retransformer, modifier, filter);
    }

    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
        try {
            if (result == null) {
                return;
            }
            
            Object bean;
            
            try {
                Method getter = result.getClass().getMethod("getWrappedInstance"); 
                bean = getter.invoke(result);
            } catch (Exception e) {
                logger.warn("Fail to get create bean instance", e);
                return;
            }
            
            String beanName = (String)args[0];

            processBean(beanName, bean);
        } catch (Throwable t) {
            logger.warn("Unexpected exception", t);
        }
    }
}
