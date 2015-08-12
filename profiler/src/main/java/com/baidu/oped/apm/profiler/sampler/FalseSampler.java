
package com.baidu.oped.apm.profiler.sampler;

import com.baidu.oped.apm.bootstrap.sampler.Sampler;

/**
 * class FalseSampler 
 *
 * @author meidongxu@baidu.com
 */
public class FalseSampler implements Sampler {
    @Override
    public boolean isSampling() {
        return false;
    }

    @Override
    public String toString() {
        // To fix sampler name even if the class name is changed.
        return "FalseSampler";
    }
}
