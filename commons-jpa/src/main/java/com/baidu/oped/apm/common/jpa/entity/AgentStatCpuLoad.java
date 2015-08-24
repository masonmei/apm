package com.baidu.oped.apm.common.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * The persistent class for the apm_agent_stat_cpu_load database table.
 * 
 */
@Entity
@Table(name="apm_agent_stat_cpu_load")
public class AgentStatCpuLoad extends AbstractPersistable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="agent_id", nullable=false, length=128)
	private String agentId;

	@Column(name="jvm_cpu_load", nullable=false)
	private double jvmCpuLoad;

	@Column(name="start_timestamp", nullable=false)
	private long startTimestamp;

	@Column(name="system_cpu_load", nullable=false)
	private double systemCpuLoad;

	@Column(nullable=false)
	private long timestamp;

	public AgentStatCpuLoad() {
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public double getJvmCpuLoad() {
		return jvmCpuLoad;
	}

	public void setJvmCpuLoad(double jvmCpuLoad) {
		this.jvmCpuLoad = jvmCpuLoad;
	}

	public long getStartTimestamp() {
		return startTimestamp;
	}

	public void setStartTimestamp(long startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	public double getSystemCpuLoad() {
		return systemCpuLoad;
	}

	public void setSystemCpuLoad(double systemCpuLoad) {
		this.systemCpuLoad = systemCpuLoad;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}