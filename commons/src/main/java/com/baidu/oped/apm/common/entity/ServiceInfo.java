
package com.baidu.oped.apm.common.entity;

/**
 * class ServiceInfo
 *
 * @author yangbolin@baidu.com
 */
public class ServiceInfo extends BaseEntity {

    private long serverMetaDataId;
    private String agentId;
    private  String serviceName;
    private  String serviceLibs;

    public ServiceInfo() {
    }

    public ServiceInfo(long serverMetaDataId, String agentId, String serviceName, String serviceLibs) {
        this.serverMetaDataId = serverMetaDataId;
        this.agentId = agentId;
        this.serviceName = serviceName;
        this.serviceLibs = serviceLibs;
    }

    public long getServerMetaDataId() {
        return serverMetaDataId;
    }

    public void setServerMetaDataId(long serverMetaDataId) {
        this.serverMetaDataId = serverMetaDataId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceLibs() {
        return serviceLibs;
    }

    public void setServiceLibs(String serviceLibs) {
        this.serviceLibs = serviceLibs;
    }
}
