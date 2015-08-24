package com.baidu.oped.apm.common.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * The persistent class for the apm_api_meta_data database table.
 * 
 */
@Entity
@Table(name="apm_api_meta_data")
public class ApiMetaData extends AbstractPersistable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="agent_id", nullable=false, length=128)
	private String agentId;

	@Column(name="api_id", nullable=false)
	private int apiId;

	@Column(name="api_info", length=512)
	private String apiInfo;

	@Column(name="line_number")
	private int lineNumber;

	@Column(name="start_time", nullable=false)
	private long startTime;

	public ApiMetaData() {
	}

	public String getAgentId() {
		return this.agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public int getApiId() {
		return this.apiId;
	}

	public void setApiId(int apiId) {
		this.apiId = apiId;
	}

	public String getApiInfo() {
		return this.apiInfo;
	}

	public void setApiInfo(String apiInfo) {
		this.apiInfo = apiInfo;
	}

	public int getLineNumber() {
		return this.lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public long getStartTime() {
		return this.startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

}