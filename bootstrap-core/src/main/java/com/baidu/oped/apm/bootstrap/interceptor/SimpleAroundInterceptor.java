
package com.baidu.oped.apm.bootstrap.interceptor;

/**
 * class SimpleAroundInterceptor 
 *
 * @author meidongxu@baidu.com
 */
public interface SimpleAroundInterceptor extends Interceptor {

    void before(Object target, Object[] args);

    void after(Object target, Object[] args, Object result, Throwable throwable);
}
