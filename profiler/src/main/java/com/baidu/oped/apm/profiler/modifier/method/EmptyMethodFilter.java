
package com.baidu.oped.apm.profiler.modifier.method;

import com.baidu.oped.apm.bootstrap.instrument.MethodFilter;
import com.baidu.oped.apm.bootstrap.instrument.MethodInfo;

/**
 * class EmptyMethodFilter 
 *
 * @author meidongxu@baidu.com
 */
public class EmptyMethodFilter implements MethodFilter {
    public static final MethodFilter FILTER = new EmptyMethodFilter();

    @Override
    public boolean filter(MethodInfo ctMethod) {
        return false;//ctMethod.isEmpty();
    }
}
