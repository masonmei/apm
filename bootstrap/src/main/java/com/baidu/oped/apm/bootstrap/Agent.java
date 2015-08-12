package com.baidu.oped.apm.bootstrap;

import com.baidu.oped.apm.bootstrap.config.ProfilerConfig;
import com.baidu.oped.apm.bootstrap.context.TraceContext;

/**
 * class Agent
 *
 * @author meidongxu@baidu.com
 */
public interface Agent {

    void start();

    void stop();

    TraceContext getTraceContext();

    ProfilerConfig getProfilerConfig();

}
