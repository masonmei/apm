package com.baidu.oped.apm.common.entity;

import com.baidu.oped.apm.common.annotation.JdbcTables;
import com.baidu.oped.apm.common.annotation.Table;

/**
 * Created by mason on 8/20/15.
 */
@Table(name = JdbcTables.APPLICATION_INDEX)
public class ApplicationIndex extends BaseEntity {
    private String applicationName;
    private String agentId;
    private short serviceType;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public short getServiceType() {
        return serviceType;
    }

    public void setServiceType(short serviceType) {
        this.serviceType = serviceType;
    }
}
