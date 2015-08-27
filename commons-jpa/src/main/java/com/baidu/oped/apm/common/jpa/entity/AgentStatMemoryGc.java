package com.baidu.oped.apm.common.jpa.entity;

import java.io.Serializable;

import javax.persistence.Basic;
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

	@Basic
	@Column(name = "app_id", nullable = false, insertable = true, updatable = true)
	private Long appId;

	@Basic
	@Column(name = "instance_id", nullable = false, insertable = true, updatable = true)
	private Long instanceId;

	@Column(name="gc_type", nullable=false, length=64)
	private String gcType;

	@Column(name="jvm_gc_old_count", nullable=false, insertable = true, updatable = true)
	private long jvmGcOldCount;

	@Column(name="jvm_gc_old_time", nullable=false, insertable = true, updatable = true)
	private long jvmGcOldTime;

	@Column(name="jvm_memory_heap_max", nullable=false, insertable = true, updatable = true)
	private long jvmMemoryHeapMax;

	@Column(name="jvm_memory_heap_used", nullable=false, insertable = true, updatable = true)
	private long jvmMemoryHeapUsed;

	@Column(name="jvm_memory_non_heap_max", nullable=false, insertable = true, updatable = true)
	private long jvmMemoryNonHeapMax;

	@Column(name="jvm_memory_non_heap_used", nullable=false, insertable = true, updatable = true)
	private long jvmMemoryNonHeapUsed;

	@Column(name = "timestamp", nullable=false, updatable = false, insertable = true)
	private long timestamp;

	public AgentStatMemoryGc() {
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
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

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}