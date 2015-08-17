
package com.baidu.oped.apm.common.entity;

/**
 * class ServerMetaData
 *
 * @author yangbolin@baidu.com
 */
public class ServerMetaData extends BaseEntity {

    private long agentInfoId;
    private String agentId;
    private String serverInfo;
    private String vmArgs; // 逗号分割

    public ServerMetaData() {
    }

    public ServerMetaData(long agentInfoId, String agentId, String serverInfo, String vmArgs) {
        this.agentInfoId = agentInfoId;
        this.agentId = agentId;
        this.serverInfo = serverInfo;
        this.vmArgs = vmArgs;
    }

    public long getAgentInfoId() {
        return agentInfoId;
    }

    public void setAgentInfoId(long agentInfoId) {
        this.agentInfoId = agentInfoId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(String serverInfo) {
        this.serverInfo = serverInfo;
    }

    public String getVmArgs() {
        return vmArgs;
    }

    public void setVmArgs(String vmArgs) {
        this.vmArgs = vmArgs;
    }
}
