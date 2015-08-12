
package com.baidu.oped.apm.profiler.modifier.arcus.interceptor;

import com.baidu.oped.apm.bootstrap.interceptor.ParameterExtractor;

/**
 * class IndexParameterExtractor 
 *
 * @author meidongxu@baidu.com
 */
public class IndexParameterExtractor implements ParameterExtractor {

    private final int index;

    public IndexParameterExtractor(int index) {
        this.index = index;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public Object extractObject(Object[] parameterList) {
        if (parameterList == null) {
            return NULL;
        }
        return parameterList[index];
    }


}
