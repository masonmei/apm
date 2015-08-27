package com.baidu.oped.apm.common.jpa.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Created by mason on 8/27/15.
 */
@Entity
@Table(name = "apm_sql_transaction")
public class SqlTransaction extends AbstractPersistable<Long> {

    @Basic
    @Column(name = "app_id", nullable = false, insertable = true, updatable = true)
    private Long appId;

    @Basic
    @Column(name = "instance_id", nullable = false, insertable = true, updatable = true)
    private Long instanceId;

    @Basic
    @Column(name = "sql_id", nullable = false, insertable = true, updatable = true, length = 512)
    private String sqlId;

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

    public String getSqlId() {
        return sqlId;
    }

    public void setSqlId(String sqlId) {
        this.sqlId = sqlId;
    }
}
