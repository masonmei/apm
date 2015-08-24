package com.baidu.oped.apm.common.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * The persistent class for the apm_host_application_map database table.
 * 
 */
@Entity
@Table(name="apm_host_application_map")
public class HostApplicationMap extends AbstractPersistable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="bind_application_name", length=128)
	private String bindApplicationName;

	@Column(name="bind_service_type")
	private short bindServiceType;

	@Column(length=128)
	private String host;

	@Column(name="parent_agent_id", length=128)
	private String parentAgentId;

	@Column(name="parent_application_name", length=128)
	private String parentApplicationName;

	@Column(name="parent_service_type")
	private short parentServiceType;

	@Column(name="statistics_row_slot")
	private long statisticsRowSlot;

	public HostApplicationMap() {
	}

	public String getBindApplicationName() {
		return this.bindApplicationName;
	}

	public void setBindApplicationName(String bindApplicationName) {
		this.bindApplicationName = bindApplicationName;
	}

	public short getBindServiceType() {
		return this.bindServiceType;
	}

	public void setBindServiceType(short bindServiceType) {
		this.bindServiceType = bindServiceType;
	}

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getParentAgentId() {
		return this.parentAgentId;
	}

	public void setParentAgentId(String parentAgentId) {
		this.parentAgentId = parentAgentId;
	}

	public String getParentApplicationName() {
		return this.parentApplicationName;
	}

	public void setParentApplicationName(String parentApplicationName) {
		this.parentApplicationName = parentApplicationName;
	}

	public short getParentServiceType() {
		return this.parentServiceType;
	}

	public void setParentServiceType(short parentServiceType) {
		this.parentServiceType = parentServiceType;
	}

	public long getStatisticsRowSlot() {
		return this.statisticsRowSlot;
	}

	public void setStatisticsRowSlot(long statisticsRowSlot) {
		this.statisticsRowSlot = statisticsRowSlot;
	}

}