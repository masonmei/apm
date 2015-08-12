
package com.baidu.oped.apm.test.junit4;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * class IsRootSpan 
 *
 * @author meidongxu@baidu.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface IsRootSpan {
    boolean value() default true;
}
