package com.baidu.oped.apm.common.entity;

import com.baidu.oped.apm.thrift.dto.TAgentInfo;

/**
 * class AgentInfoBo
 *
 * @author yangbolin@baidu.com
 */
public class AgentInfo extends BaseEntity {

    private String hostName;
    private String ip;
    private String ports;
    private String agentId;
    private String applicationName;
    private short serviceType;
    private int pid;
    private String version;
    private long startTime;
    private long endTimeStamp;
    private int endStatus;

    public AgentInfo() {
    }

    public AgentInfo(TAgentInfo thriftObject) {
        this.hostName = thriftObject.getHostname();
        this.ip = thriftObject.getIp();
        this.ports = thriftObject.getPorts();
        this.agentId = thriftObject.getAgentId();
        this.applicationName = thriftObject.getApplicationName();
        this.serviceType = thriftObject.getServiceType();
        this.pid = thriftObject.getPid();
        this.version = thriftObject.getVersion();
        this.startTime = thriftObject.getStartTimestamp();
        this.endTimeStamp = thriftObject.getEndTimestamp();
        this.endStatus = thriftObject.getEndStatus();
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

    public String getPorts() {
        return ports;
    }

    public void setPorts(String ports) {
        this.ports = ports;
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

    public short getServiceType() {
        return serviceType;
    }

    public void setServiceType(short serviceType) {
        this.serviceType = serviceType;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTimeStamp() {
        return endTimeStamp;
    }

    public void setEndTimeStamp(long endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }

    public int getEndStatus() {
        return endStatus;
    }

    public void setEndStatus(int endStatus) {
        this.endStatus = endStatus;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((agentId == null) ? 0 : agentId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AgentInfo other = (AgentInfo) obj;
        if (agentId == null) {
            if (other.agentId != null)
                return false;
        } else if (!agentId.equals(other.agentId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "AgentInfo{" +
                "hostName='" + hostName + '\'' +
                ", ip='" + ip + '\'' +
                ", ports='" + ports + '\'' +
                ", agentId='" + agentId + '\'' +
                ", applicationName='" + applicationName + '\'' +
                ", serviceType='" + serviceType + '\'' +
                ", pid=" + pid +
                ", version='" + version + '\'' +
                ", startTime=" + startTime +
                ", endTimeStamp=" + endTimeStamp +
                ", endStatus=" + endStatus +
                '}';
    }

}
