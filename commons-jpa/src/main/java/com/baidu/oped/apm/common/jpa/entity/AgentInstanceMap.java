package com.baidu.oped.apm.common.jpa.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * The persistent class for the apm_agent_stat_cpu_load database table.
 */
@Entity
@Table(name = "apm_agent_instance_map")
public class AgentInstanceMap extends AbstractPersistable<Long> {

    @Basic
    @Column(name = "agent_id", nullable = false, insertable = true, updatable = true)
    private String agentId;

    @Basic
    @Column(name = "agent_start_time", nullable = false, insertable = true, updatable = true)
    private Long agentStartTime;

    @Basic
    @Column(name = "instance_id", nullable = false, insertable = true, updatable = true)
    private Long instanceId;

    @Basic
    @Column(name = "app_id", nullable = false, insertable = true, updatable = true)
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
}
