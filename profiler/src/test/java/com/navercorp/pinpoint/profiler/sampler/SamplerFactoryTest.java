
package com.baidu.oped.apm.profiler.sampler;

import com.baidu.oped.apm.bootstrap.sampler.Sampler;
import com.baidu.oped.apm.profiler.sampler.SamplerFactory;

import junit.framework.Assert;

import org.junit.Test;

/**
 * class SamplerFactoryTest 
 *
 * @author meidongxu@baidu.com
 */
public class SamplerFactoryTest {
    @Test
    public void createSamplerSamplingRate0() {
        SamplerFactory samplerFactory = new SamplerFactory();
        Sampler sampler = samplerFactory.createSampler(true, 0);
        boolean sampling = sampler.isSampling();
        Assert.assertFalse(sampling);
    }

    @Test
    public void createSamplerSamplingRate_Negative() {
        SamplerFactory samplerFactory = new SamplerFactory();
        Sampler sampler = samplerFactory.createSampler(true, -1);
        boolean sampling = sampler.isSampling();
        Assert.assertFalse(sampling);
    }
}
