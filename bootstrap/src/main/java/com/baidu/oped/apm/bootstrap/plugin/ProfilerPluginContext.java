package com.baidu.oped.apm.bootstrap.plugin;

import com.baidu.oped.apm.bootstrap.config.ProfilerConfig;
import com.baidu.oped.apm.bootstrap.context.TraceContext;
import com.baidu.oped.apm.bootstrap.instrument.ByteCodeInstrumentor;

/**
 * class ProfilerPluginContext
 *
 * @author meidongxu@baidu.com
 */
public class ProfilerPluginContext {
    private final ByteCodeInstrumentor instrumentor;
    private final TraceContext traceContext;

    public ProfilerPluginContext(ByteCodeInstrumentor instrumentor, TraceContext traceContext) {
        this.instrumentor = instrumentor;
        this.traceContext = traceContext;
    }

    public ClassEditorBuilder newClassEditorBuilder() {
        return new ClassEditorBuilder(instrumentor, traceContext);
    }

    public ProfilerConfig getConfig() {
        return traceContext.getProfilerConfig();
    }

}
