package com.baidu.oped.apm.bootstrap.interceptor;

import com.baidu.oped.apm.bootstrap.config.ProfilerConfig;
import com.baidu.oped.apm.bootstrap.context.DatabaseInfo;
import com.baidu.oped.apm.bootstrap.context.Metric;
import com.baidu.oped.apm.bootstrap.context.ServerMetaDataHolder;
import com.baidu.oped.apm.bootstrap.context.Trace;
import com.baidu.oped.apm.bootstrap.context.TraceContext;
import com.baidu.oped.apm.bootstrap.context.TraceId;
import com.baidu.oped.apm.common.ServiceType;
import com.baidu.oped.apm.common.util.ParsingResult;

/**
 * class MockTraceContext
 *
 * @author meidongxu@baidu.com
 */

public class MockTraceContext implements TraceContext {

    private Trace trace;

    public void setTrace(Trace trace) {
        this.trace = trace;
    }

    @Override
    public Trace currentTraceObject() {
        if (trace == null) {
            return null;
        }
        if (trace.canSampled()) {
            return null;
        }
        return trace;
    }

    @Override
    public Trace currentRawTraceObject() {
        return trace;
    }

    @Override
    public Trace continueTraceObject(TraceId traceID) {
        return trace;
    }

    @Override
    public Trace newTraceObject() {
        return trace;
    }

    @Override
    public void detachTraceObject() {
        trace = null;
    }

    @Override
    public String getAgentId() {
        return null;
    }

    @Override
    public String getApplicationName() {
        return null;
    }

    @Override
    public long getAgentStartTime() {
        return 0;
    }

    @Override
    public short getServerTypeCode() {
        return 0;
    }

    @Override
    public String getServerType() {
        return null;
    }

    @Override
    public int cacheApi(MethodDescriptor methodDescriptor) {
        return 0;
    }

    @Override
    public int cacheString(String value) {
        return 0;
    }

    @Override
    public ParsingResult parseSql(String sql) {
        return null;
    }

    @Override
    public boolean cacheSql(ParsingResult parsingResult) {
        return false;
    }

    @Override
    public DatabaseInfo parseJdbcUrl(String sql) {
        return null;
    }

    @Override
    public DatabaseInfo createDatabaseInfo(ServiceType type, ServiceType executeQueryType, String url, int port,
                                           String databaseId) {
        return null;
    }

    @Override
    public TraceId createTraceId(String transactionId, long parentSpanID, long spanID, short flags) {
        return null;
    }

    @Override
    public Trace disableSampling() {
        return null;
    }

    @Override
    public ProfilerConfig getProfilerConfig() {
        return null;
    }

    @Override
    public Metric getRpcMetric(ServiceType serviceType) {
        return null;
    }

    @Override
    public void recordContextMetricIsError() {

    }

    @Override
    public void recordContextMetric(int elapsedTime) {

    }

    @Override
    public void recordAcceptResponseTime(String parentApplicationName, short parentApplicationType, int elapsedTime) {

    }

    @Override
    public void recordUserAcceptResponseTime(int elapsedTime) {

    }

    @Override
    public ServerMetaDataHolder getServerMetaDataHolder() {
        return null;
    }
}
