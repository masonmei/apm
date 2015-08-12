
package com.baidu.oped.apm.profiler.interceptor.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * class Aspect 
 *
 * @author meidongxu@baidu.com
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Aspect {
}
