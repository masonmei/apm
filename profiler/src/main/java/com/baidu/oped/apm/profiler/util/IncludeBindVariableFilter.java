
package com.baidu.oped.apm.profiler.util;

import java.lang.reflect.Method;

/**
 * class IncludeBindVariableFilter 
 *
 * @author meidongxu@baidu.com
 */
public class IncludeBindVariableFilter implements BindVariableFilter {
    private String[] includes;

    public IncludeBindVariableFilter(String[] includes) {
        if (includes == null) {
            throw new NullPointerException("includes must not be null");
        }
        this.includes = includes;
    }

    @Override
    public boolean filter(Method method) {
        if (method == null) {
            throw new NullPointerException("method must not be null");
        }
        for (String include: includes) {
            if(method.getName().equals(include)) {
                return true;
            }
        }
        return false;
    }
}
