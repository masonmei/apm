
package com.baidu.oped.apm.profiler.interceptor.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * class PointCut 
 *
 * @author meidongxu@baidu.com
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface PointCut {
}
