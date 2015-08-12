
package com.baidu.oped.apm.test;

import com.baidu.oped.apm.bootstrap.config.ProfilerConfig;
import com.baidu.oped.apm.bootstrap.context.TraceContext;
import com.baidu.oped.apm.profiler.context.DefaultTraceContext;

/**
 * class MockTraceContextFactory 
 *
 * @author meidongxu@baidu.com
 */
public class MockTraceContextFactory {
    public TraceContext create() {
        DefaultTraceContext traceContext = new DefaultTraceContext() ;
        ProfilerConfig profilerConfig = new ProfilerConfig();
        traceContext.setProfilerConfig(profilerConfig);
        return traceContext;
    }
}
