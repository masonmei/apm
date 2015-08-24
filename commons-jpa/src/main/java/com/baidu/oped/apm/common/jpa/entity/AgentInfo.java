package com.baidu.oped.apm.common.jpa.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * The persistent class for the apm_agent_info database table.
 */
@Entity
@Table(name = "apm_agent_info")
public class AgentInfo extends AbstractPersistable<Long> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "agent_id", nullable = false, length = 128)
    private String agentId;

    @Column(name = "application_name", nullable = false, length = 255)
    private String applicationName;

    @Column(name = "end_status", nullable = false)
    private int endStatus;

    @Column(name = "end_time_stamp", nullable = false)
    private long endTimeStamp;

    @Column(name = "host_name", nullable = false, length = 255)
    private String hostName;

    @Column(nullable = false, length = 255)
    private String ip;

    @Column(nullable = false)
    private int pid;

    @Column(nullable = false, length = 512)
    private String ports;

    @Column(name = "service_type", nullable = false, length = 255)
    private short serviceType;

    @Column(name = "start_time", nullable = false)
    private long startTime;

    @Column(nullable = false, length = 255)
    private String version;

    //bi-directional one-to-one association to ServerMetaData
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "server_meta_data_id")
    private ServerMetaData serverMetaData;

    public AgentInfo() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public int getEndStatus() {
        return endStatus;
    }

    public void setEndStatus(int endStatus) {
        this.endStatus = endStatus;
    }

    public long getEndTimeStamp() {
        return endTimeStamp;
    }

    public void setEndTimeStamp(long endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getPorts() {
        return ports;
    }

    public void setPorts(String ports) {
        this.ports = ports;
    }

    public short getServiceType() {
        return serviceType;
    }

    public void setServiceType(short serviceType) {
        this.serviceType = serviceType;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ServerMetaData getServerMetaData() {
        return serverMetaData;
    }

    public void setServerMetaData(ServerMetaData serverMetaData) {
        this.serverMetaData = serverMetaData;
    }
}