
package com.baidu.oped.apm.bootstrap.interceptor;

/**
 * class StaticAroundInterceptor 
 *
 * @author meidongxu@baidu.com
 */
public interface StaticAroundInterceptor extends Interceptor {

    void before(Object target, String className, String methodName, String parameterDescription, Object[] args);

    void after(Object target, String className, String methodName, String parameterDescription, Object[] args, Object result, Throwable throwable);

}
