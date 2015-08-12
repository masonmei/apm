
package com.baidu.oped.apm.bootstrap.interceptor;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * class LoggingInterceptor 
 *
 * @author meidongxu@baidu.com
 */
public class LoggingInterceptor implements StaticAroundInterceptor, SimpleAroundInterceptor, Interceptor {

    private final Logger logger;

    public LoggingInterceptor(String loggerName) {
        this.logger = Logger.getLogger(loggerName);
    }

    @Override
    public void before(Object target, String className, String methodName, String parameterDescription, Object[] args) {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("before " + defaultString(target) + " " + className + "." + methodName + parameterDescription + " args:" + Arrays.toString(args));
        }
    }

    @Override
    public void after(Object target, String className, String methodName, String parameterDescription, Object[] args, Object result, Throwable throwable) {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("after " + defaultString(target) + " " + className + "." + methodName + parameterDescription + " args:" + Arrays.toString(args) + " result:" + result + " Throwable:" + throwable);
        }
    }

    @Override
    public void before(Object target, Object[] args) {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("before " + defaultString(target) + " args:" + Arrays.toString(args) );
        }
    }

    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("after " + defaultString(target) + " args:" + Arrays.toString(args) + " result:" + result + " Throwable:" + throwable);
        }
    }

    public static String defaultString(final Object object) {
        return String.valueOf(object);
    }

}
