package com.baidu.oped.apm.common.jpa.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.google.common.base.MoreObjects;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * The persistent class for the apm_agent_stat_cpu_load database table.
 */
@Entity
@Table(name = "apm_agent_instance_map", indexes = {
        @Index(name = "agent_instance_unique", columnList = "agent_id,agent_start_time", unique = true)})
public class AgentInstanceMap extends AbstractPersistable<Long> {

    private static final long serialVersionUID = 1124238086865438716L;
    @Basic
    @Column(name = "agent_id", nullable = false, insertable = true, updatable = true)
    private String agentId;

    @Basic
    @Column(name = "agent_start_time", nullable = false, insertable = true, updatable = true)
    private Long agentStartTime;

    @Basic
    @Column(name = "instance_id", nullable = true, insertable = true, updatable = true)
    private Long instanceId;

    @Basic
    @Column(name = "app_id", nullable = true, insertable = true, updatable = true)
    private Long appId;

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public Long getAgentStartTime() {
        return agentStartTime;
    }

    public void setAgentStartTime(Long agentStartTime) {
        this.agentStartTime = agentStartTime;
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("agentId", agentId)
                .add("agentStartTime", agentStartTime)
                .add("instanceId", instanceId)
                .add("appId", appId)
                .toString();
    }
}
