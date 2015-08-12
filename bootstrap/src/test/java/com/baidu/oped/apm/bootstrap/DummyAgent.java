package com.baidu.oped.apm.bootstrap;

import java.lang.instrument.Instrumentation;

import com.baidu.oped.apm.bootstrap.config.ProfilerConfig;
import com.baidu.oped.apm.bootstrap.context.TraceContext;

/**
 * class DummyAgent
 *
 * @author meidongxu@baidu.com
 */
public class DummyAgent implements Agent {

    public DummyAgent(String agentPath, String agentArgs, Instrumentation instrumentation,
                      ProfilerConfig profilerConfig) {

    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @Override
    public TraceContext getTraceContext() {
        return null;
    }

    @Override
    public ProfilerConfig getProfilerConfig() {
        return null;
    }

    // @Override
    // public PLoggerBinder initializeLogger() {
    // return new PLoggerBinder() {
    // @Override
    // public PLogger getLogger(String name) {
    // return new DummyPLogger();
    // }
    //
    // @Override
    // public void shutdown() {
    //
    // }
    // };
    // }
}
