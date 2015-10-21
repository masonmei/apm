package com.baidu.oped.apm.common.jpa.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Created by mason on 8/27/15.
 */
@Entity
@Table(name = "apm_sql_transaction", indexes = {
        @Index(name = "sql_transaction_unique", columnList = "app_id,instance_id,end_point,sql_value", unique = true),
        @Index(name = "sql_transaction_group", columnList = "database_type,table_name,operation")})
public class SqlTransaction extends AbstractPersistable<Long> {

    private static final long serialVersionUID = -1503264333172908446L;
    @Basic
    @Column(name = "app_id", nullable = false, insertable = true, updatable = true)
    private Long appId;

    @Basic
    @Column(name = "instance_id", nullable = false, insertable = true, updatable = true)
    private Long instanceId;

    @Basic
    @Column(name = "end_point", nullable = false, insertable = true, updatable = true, length = 256)
    private String endPoint;

    @Basic
    @Column(name = "sql_value", nullable = false, insertable = true, updatable = true, length = 512)
    private String sql;

    @Basic
    @Enumerated(value = EnumType.STRING)
    @Column(name = "database_type", nullable = true, insertable = true, updatable = true, length = 64)
    private DatabaseType databaseType;

    @Basic
    @Column(name = "table_name", nullable = true, insertable = true, updatable = true, length = 128)
    private String tableName;

    @Basic
    @Column(name = "operation", nullable = true, insertable = true, updatable = true, length = 64)
    @Enumerated(value = EnumType.STRING)
    private StatementType statementType;

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

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public StatementType getStatementType() {
        return statementType;
    }

    public void setStatementType(StatementType statementType) {
        this.statementType = statementType;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("appId", appId)
                .add("instanceId", instanceId)
                .add("endPoint", endPoint)
                .add("sql", sql)
                .add("databaseType", databaseType)
                .add("tableName", tableName)
                .add("statementType", statementType)
                .toString();
    }
}
