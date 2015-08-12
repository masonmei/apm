package com.baidu.oped.apm.bootstrap.context;

import com.baidu.oped.apm.bootstrap.config.ProfilerConfig;
import com.baidu.oped.apm.bootstrap.interceptor.MethodDescriptor;
import com.baidu.oped.apm.common.ServiceType;
import com.baidu.oped.apm.common.util.ParsingResult;

/**
 * class TraceContext
 *
 * @author meidongxu@baidu.com
 */

public interface TraceContext {

    Trace currentTraceObject();

    /**
     * return a trace whose sampling rate should be further verified
     *
     * @return
     */
    Trace currentRawTraceObject();

    Trace continueTraceObject(TraceId traceID);

    Trace newTraceObject();

    void detachTraceObject();

    //    ActiveThreadCounter getActiveThreadCounter();

    String getAgentId();

    String getApplicationName();

    long getAgentStartTime();

    short getServerTypeCode();

    String getServerType();

    int cacheApi(MethodDescriptor methodDescriptor);

    int cacheString(String value);

    ParsingResult parseSql(String sql);

    boolean cacheSql(ParsingResult parsingResult);

    DatabaseInfo parseJdbcUrl(String sql);

    DatabaseInfo createDatabaseInfo(ServiceType type, ServiceType executeQueryType, String url, int port,
                                    String databaseId);

    TraceId createTraceId(String transactionId, long parentSpanID, long spanID, short flags);

    Trace disableSampling();

    ProfilerConfig getProfilerConfig();

    Metric getRpcMetric(ServiceType serviceType);

    void recordContextMetricIsError();

    void recordContextMetric(int elapsedTime);

    void recordAcceptResponseTime(String parentApplicationName, short parentApplicationType, int elapsedTime);

    void recordUserAcceptResponseTime(int elapsedTime);

    ServerMetaDataHolder getServerMetaDataHolder();

}
