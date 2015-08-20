package com.baidu.oped.apm.common.entity;

import com.baidu.oped.apm.common.annotation.JdbcTables;
import com.baidu.oped.apm.common.annotation.Table;

/**
 * Created by mason on 8/18/15.
 */
@Table(name = JdbcTables.APPLICATION_TRACE_INDEX)
public class ApplicationTraceIndex extends BaseEntity {
    private int elapsed;
    private int err;
    private long acceptedTime;
    private String applicationName;


    private String agentId;
    private long agentStartTime;
    private long transactionSequence;

    public int getElapsed() {
        return elapsed;
    }

    public void setElapsed(int elapsed) {
        this.elapsed = elapsed;
    }

    public int getErr() {
        return err;
    }

    public void setErr(int err) {
        this.err = err;
    }

    public long getAcceptedTime() {
        return acceptedTime;
    }

    public void setAcceptedTime(long acceptedTime) {
        this.acceptedTime = acceptedTime;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public long getAgentStartTime() {
        return agentStartTime;
    }

    public void setAgentStartTime(long agentStartTime) {
        this.agentStartTime = agentStartTime;
    }

    public long getTransactionSequence() {
        return transactionSequence;
    }

    public void setTransactionSequence(long transactionSequence) {
        this.transactionSequence = transactionSequence;
    }
}
