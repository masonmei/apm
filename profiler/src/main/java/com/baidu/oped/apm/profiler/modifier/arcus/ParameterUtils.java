
package com.baidu.oped.apm.profiler.modifier.arcus;

import com.baidu.oped.apm.bootstrap.instrument.MethodInfo;

/**
 * class ParameterUtils 
 *
 * @author meidongxu@baidu.com
 */
public final class ParameterUtils {

    private ParameterUtils() {
    }

    public static int findFirstString(MethodInfo method, int maxIndex) {
        if (method == null) {
            return -1;
        }
        final String[] methodParams = method.getParameterTypes();
        final int minIndex = Math.min(methodParams.length, maxIndex);
        for(int i =0; i < minIndex; i++) {
            if ("java.lang.String".equals(methodParams[i])) {
                return i;
            }
        }
        return -1;
    }
}
