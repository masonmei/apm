
package com.baidu.oped.apm.profiler.sampler;

import com.baidu.oped.apm.bootstrap.sampler.Sampler;

/**
 * class SamplerFactory 
 *
 * @author meidongxu@baidu.com
 */
public class SamplerFactory {
    public Sampler createSampler(boolean sampling, int samplingRate) {
        if (!sampling || samplingRate <= 0) {
            return new FalseSampler();
        }
        if (samplingRate == 1) {
            return new TrueSampler();
        }
        return new SamplingRateSampler(samplingRate);
    }
}
