package com.baidu.oped.apm.common.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * The persistent class for the apm_agent_stat_memory_gc database table.
 * 
 */
@Entity
@Table(name="apm_agent_stat_memory_gc")
public class AgentStatMemoryGc extends AbstractPersistable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="agent_id", nullable=false, length=128)
	private String agentId;

	@Column(name="gc_type", nullable=false, length=255)
	private String gcType;

	@Column(name="jvm_gc_old_count", nullable=false)
	private long jvmGcOldCount;

	@Column(name="jvm_gc_old_time", nullable=false)
	private long jvmGcOldTime;

	@Column(name="jvm_memory_heap_max", nullable=false)
	private long jvmMemoryHeapMax;

	@Column(name="jvm_memory_heap_used", nullable=false)
	private long jvmMemoryHeapUsed;

	@Column(name="jvm_memory_non_heap_max", nullable=false)
	private long jvmMemoryNonHeapMax;

	@Column(name="jvm_memory_non_heap_used", nullable=false)
	private long jvmMemoryNonHeapUsed;

	@Column(name="start_timestamp", nullable=false)
	private long startTimestamp;

	@Column(nullable=false)
	private long timestamp;

	public AgentStatMemoryGc() {
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

	public String getGcType() {
		return gcType;
	}

	public void setGcType(String gcType) {
		this.gcType = gcType;
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

	public long getJvmMemoryHeapMax() {
		return jvmMemoryHeapMax;
	}

	public void setJvmMemoryHeapMax(long jvmMemoryHeapMax) {
		this.jvmMemoryHeapMax = jvmMemoryHeapMax;
	}

	public long getJvmMemoryHeapUsed() {
		return jvmMemoryHeapUsed;
	}

	public void setJvmMemoryHeapUsed(long jvmMemoryHeapUsed) {
		this.jvmMemoryHeapUsed = jvmMemoryHeapUsed;
	}

	public long getJvmMemoryNonHeapMax() {
		return jvmMemoryNonHeapMax;
	}

	public void setJvmMemoryNonHeapMax(long jvmMemoryNonHeapMax) {
		this.jvmMemoryNonHeapMax = jvmMemoryNonHeapMax;
	}

	public long getJvmMemoryNonHeapUsed() {
		return jvmMemoryNonHeapUsed;
	}

	public void setJvmMemoryNonHeapUsed(long jvmMemoryNonHeapUsed) {
		this.jvmMemoryNonHeapUsed = jvmMemoryNonHeapUsed;
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
}