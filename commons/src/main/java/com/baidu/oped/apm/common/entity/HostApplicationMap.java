package com.baidu.oped.apm.common.entity;

import com.baidu.oped.apm.common.annotation.JdbcTables;
import com.baidu.oped.apm.common.annotation.Table;

/**
 * Created by mason on 8/18/15.
 */
@Table(name = JdbcTables.HOST_APPLICATION_MAP)
public class HostApplicationMap extends BaseEntity {
    private String host;
    private String bindApplicationName;
    private short bindServiceType;
    private long statisticsRowSlot;
    private String parentApplicationName;
    private short parentServiceType;
    private String parentAgentId;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getBindApplicationName() {
        return bindApplicationName;
    }

    public void setBindApplicationName(String bindApplicationName) {
        this.bindApplicationName = bindApplicationName;
    }

    public short getBindServiceType() {
        return bindServiceType;
    }

    public void setBindServiceType(short bindServiceType) {
        this.bindServiceType = bindServiceType;
    }

    public long getStatisticsRowSlot() {
        return statisticsRowSlot;
    }

    public void setStatisticsRowSlot(long statisticsRowSlot) {
        this.statisticsRowSlot = statisticsRowSlot;
    }

    public String getParentApplicationName() {
        return parentApplicationName;
    }

    public void setParentApplicationName(String parentApplicationName) {
        this.parentApplicationName = parentApplicationName;
    }

    public short getParentServiceType() {
        return parentServiceType;
    }

    public void setParentServiceType(short parentServiceType) {
        this.parentServiceType = parentServiceType;
    }

    public String getParentAgentId() {
        return parentAgentId;
    }

    public void setParentAgentId(String parentAgentId) {
        this.parentAgentId = parentAgentId;
    }
}
