package com.baidu.oped.apm.common.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * The persistent class for the apm_sql_meta_data database table.
 */
@Entity
@Table(name = "apm_sql_meta_data")
public class SqlMetaData extends AbstractPersistable<Long> {

    @Column(name = "instance_id", nullable = false, insertable = true, updatable = true)
    private Long instanceId;

    @Column(name = "sql_id", nullable = false, insertable = true, updatable = true)
    private int sqlId;

    @Column(name = "`sql`", nullable = true, insertable = true, updatable = true, length = 512)
    private String sql;

    @Column(name = "start_time", nullable = false, insertable = true, updatable = true)
    private long startTime;

    public SqlMetaData() {
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}