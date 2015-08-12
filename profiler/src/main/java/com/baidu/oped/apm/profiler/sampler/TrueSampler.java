
package com.baidu.oped.apm.profiler.sampler;

import com.baidu.oped.apm.bootstrap.sampler.Sampler;

/**
 * class TrueSampler 
 *
 * @author meidongxu@baidu.com
 */
public class TrueSampler implements Sampler {

    @Override
    public boolean isSampling() {
        return true;
    }

    @Override
    public String toString() {
        // To fix sampler name even if the class name is changed.
        return "TrueSampler";
    }
}
