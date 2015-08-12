
package com.baidu.oped.apm.test.fork;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * class WithLibraries 
 *
 * @author meidongxu@baidu.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WithLibraries {
    String[] value() default {};
}
