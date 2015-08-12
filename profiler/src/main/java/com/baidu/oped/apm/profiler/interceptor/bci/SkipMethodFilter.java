
package com.baidu.oped.apm.profiler.interceptor.bci;

import com.baidu.oped.apm.bootstrap.instrument.MethodFilter;
import com.baidu.oped.apm.bootstrap.instrument.MethodInfo;

/**
 * class SkipMethodFilter 
 *
 * @author meidongxu@baidu.com
 */
public class SkipMethodFilter implements MethodFilter {
    public static final MethodFilter FILTER = new SkipMethodFilter();

    @Override
    public boolean filter(MethodInfo ctMethod) {
        return false;
    }
}
