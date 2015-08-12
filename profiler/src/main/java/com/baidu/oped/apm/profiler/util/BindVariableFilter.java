
package com.baidu.oped.apm.profiler.util;

import java.lang.reflect.Method;

/**
 * class BindVariableFilter 
 *
 * @author meidongxu@baidu.com
 */
public interface BindVariableFilter {
    boolean filter(Method method);
}
