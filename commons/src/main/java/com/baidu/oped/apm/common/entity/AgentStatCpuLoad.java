package com.baidu.oped.apm.common.entity;

import com.baidu.oped.apm.common.annotation.JdbcTables;
import com.baidu.oped.apm.common.annotation.Table;

/**
 * ***** description *****
 * Created with IntelliJ IDEA.
 * User: yangbolin
 * Date: 15/8/18
 * Time: 11:35
 * To change this template use File | Settings | File Templates.
 */
@Table(name = JdbcTables.AGENT_CPU_LOAD)
public class AgentStatCpuLoad extends BaseEntity {

    private static final double UNSUPPORTED = -1.0D;

    private String agentId;
    private long startTimestamp;
    private long timestamp;
    private double jvmCpuLoad = UNSUPPORTED;    // range is  1 >= X >=0,  ex) if 25%  then save 0.25
    private double systemCpuLoad = UNSUPPORTED; // range is  1 >= X >=0,  ex) if 25%  then save 0.25

    public AgentStatCpuLoad() {}

    public AgentStatCpuLoad(String agentId, long startTimestamp, long timestamp, double jvmCpuLoad, double systemCpuLoad) {
        this.agentId = agentId;
        this.startTimestamp = startTimestamp;
        this.timestamp = timestamp;
        this.jvmCpuLoad = jvmCpuLoad;
        this.systemCpuLoad = systemCpuLoad;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getJvmCpuLoad() {
        return jvmCpuLoad;
    }

    public void setJvmCpuLoad(double jvmCpuLoad) {
        this.jvmCpuLoad = jvmCpuLoad;
    }

    public double getSystemCpuLoad() {
        return systemCpuLoad;
    }

    public void setSystemCpuLoad(double systemCpuLoad) {
        this.systemCpuLoad = systemCpuLoad;
    }
}
