package com.baidu.oped.apm.collector.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by mason on 8/5/15.
 */
@ConfigurationProperties(prefix = "collector")
public class MxCollectorProperties {
    private long flushPeriod;
    private int agentEventWorkerThread;
    private int agentEventWorkerQueueSize;
    private TcpConfig tcpConfig;
    private StatConfig statConfig;
    private SpanConfig spanConfig;

    public long getFlushPeriod() {
        return flushPeriod;
    }

    public void setFlushPeriod(long flushPeriod) {
        this.flushPeriod = flushPeriod;
    }

    public int getAgentEventWorkerThread() {
        return agentEventWorkerThread;
    }

    public void setAgentEventWorkerThread(int agentEventWorkerThread) {
        this.agentEventWorkerThread = agentEventWorkerThread;
    }

    public int getAgentEventWorkerQueueSize() {
        return agentEventWorkerQueueSize;
    }

    public void setAgentEventWorkerQueueSize(int agentEventWorkerQueueSize) {
        this.agentEventWorkerQueueSize = agentEventWorkerQueueSize;
    }

    public TcpConfig getTcpConfig() {
        return tcpConfig;
    }


    public void setTcpConfig(TcpConfig tcpConfig) {
        this.tcpConfig = tcpConfig;
    }

    public StatConfig getStatConfig() {
        return statConfig;
    }

    public void setStatConfig(StatConfig statConfig) {
        this.statConfig = statConfig;
    }

    public SpanConfig getSpanConfig() {
        return spanConfig;
    }

    public void setSpanConfig(SpanConfig spanConfig) {
        this.spanConfig = spanConfig;
    }

}
