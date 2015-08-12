package com.baidu.oped.apm.bootstrap.instrument;

/**
 * class MethodFilter
 *
 * @author meidongxu@baidu.com
 */
public interface MethodFilter {
    boolean filter(MethodInfo method);

}
