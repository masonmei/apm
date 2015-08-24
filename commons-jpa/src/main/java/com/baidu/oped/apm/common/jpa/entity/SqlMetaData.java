package com.baidu.oped.apm.common.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * The persistent class for the apm_sql_meta_data database table.
 * 
 */
@Entity
@Table(name="apm_sql_meta_data")
public class SqlMetaData extends AbstractPersistable<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="agent_id", nullable=false, length=128)
	private String agentId;

	@Column(name="hash_code", nullable=false)
	private int hashCode;

	@Column(name = "`sql`",length=512)
	private String sql;

	@Column(name="start_time", nullable=false)
	private long startTime;

	public SqlMetaData() {
	}

	public String getAgentId() {
		return this.agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public int getHashCode() {
		return this.hashCode;
	}

	public void setHashCode(int hashCode) {
		this.hashCode = hashCode;
	}

	public String getSql() {
		return this.sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public long getStartTime() {
		return this.startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

}