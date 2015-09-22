package com.baidu.oped.apm.common.jpa.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * The persistent class for the apm_sql_meta_data database table.
 */
@Entity
@Table(name = "apm_sql_meta_data", indexes = {
        @Index(name = "sql_meta_unique", columnList = "agent_id,sql_id", unique = true)})
public class SqlMetaData extends AbstractPersistable<Long> implements ClearableAgentInfo {

    @Basic
    @Column(name = "agent_id", nullable = true, insertable = true, updatable = true)
    private Long agentId;

    @Column(name = "sql_id", nullable = false, insertable = true, updatable = true)
    private int sqlId;

    @Column(name = "`sql`", nullable = true, insertable = true, updatable = true, length = 512)
    private String sql;

    public SqlMetaData() {
    }

    @Override
    public Long getAgentId() {
        return agentId;
    }

    @Override
    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public int getSqlId() {
        return sqlId;
    }

    public void setSqlId(int sqlId) {
        this.sqlId = sqlId;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

}