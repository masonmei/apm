package com.baidu.oped.apm.common.entity;

import com.baidu.oped.apm.common.annotation.JdbcTables;
import com.baidu.oped.apm.common.annotation.Table;

/**
 * ***** description *****
 * Created with IntelliJ IDEA.
 * User: yangbolin
 * Date: 15/8/18
 * Time: 11:33
 * To change this template use File | Settings | File Templates.
 */
@Table(name = JdbcTables.AGENT_STAT_MEMORY_GC)
public class AgentStatMemoryGc extends BaseEntity {

    private String agentId;
    private long startTimestamp;
    private long timestamp;
    private String gcType;
    private long jvmMemoryHeapUsed;
    private long jvmMemoryHeapMax;
    private long jvmMemoryNonHeapUsed;
    private long jvmMemoryNonHeapMax;
    private long jvmGcOldCount;
    private long jvmGcOldTime;

    public AgentStatMemoryGc() {
    }

    public AgentStatMemoryGc(String agentId, long startTimestamp, long timestamp, String gcType, long jvmMemoryHeapUsed, long jvmMemoryHeapMax,
                             long jvmMemoryNonHeapUsed, long jvmMemoryNonHeapMax, long jvmGcOldCount, long jvmGcOldTime) {
        this.agentId = agentId;
        this.startTimestamp = startTimestamp;
        this.timestamp = timestamp;
        this.gcType = gcType;
        this.jvmMemoryHeapUsed = jvmMemoryHeapUsed;
        this.jvmMemoryHeapMax = jvmMemoryHeapMax;
        this.jvmMemoryNonHeapUsed = jvmMemoryNonHeapUsed;
        this.jvmMemoryNonHeapMax = jvmMemoryNonHeapMax;
        this.jvmGcOldCount = jvmGcOldCount;
        this.jvmGcOldTime = jvmGcOldTime;
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

    public String getGcType() {
        return gcType;
    }

    public void setGcType(String gcType) {
        this.gcType = gcType;
    }

    public long getJvmMemoryHeapUsed() {
        return jvmMemoryHeapUsed;
    }

    public void setJvmMemoryHeapUsed(long jvmMemoryHeapUsed) {
        this.jvmMemoryHeapUsed = jvmMemoryHeapUsed;
    }

    public long getJvmMemoryHeapMax() {
        return jvmMemoryHeapMax;
    }

    public void setJvmMemoryHeapMax(long jvmMemoryHeapMax) {
        this.jvmMemoryHeapMax = jvmMemoryHeapMax;
    }

    public long getJvmMemoryNonHeapUsed() {
        return jvmMemoryNonHeapUsed;
    }

    public void setJvmMemoryNonHeapUsed(long jvmMemoryNonHeapUsed) {
        this.jvmMemoryNonHeapUsed = jvmMemoryNonHeapUsed;
    }

    public long getJvmMemoryNonHeapMax() {
        return jvmMemoryNonHeapMax;
    }

    public void setJvmMemoryNonHeapMax(long jvmMemoryNonHeapMax) {
        this.jvmMemoryNonHeapMax = jvmMemoryNonHeapMax;
    }

    public long getJvmGcOldCount() {
        return jvmGcOldCount;
    }

    public void setJvmGcOldCount(long jvmGcOldCount) {
        this.jvmGcOldCount = jvmGcOldCount;
    }

    public long getJvmGcOldTime() {
        return jvmGcOldTime;
    }

    public void setJvmGcOldTime(long jvmGcOldTime) {
        this.jvmGcOldTime = jvmGcOldTime;
    }
}
