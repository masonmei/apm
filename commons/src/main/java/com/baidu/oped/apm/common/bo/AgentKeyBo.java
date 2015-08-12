
package com.baidu.oped.apm.common.bo;

/**
 * class AgentKeyBo 
 *
 * @author meidongxu@baidu.com
 */
public class AgentKeyBo {
    private String agentId;
    private String applicationName;
    private long agentStartTime;

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

    public long getAgentStartTime() {
        return agentStartTime;
    }

    public void setAgentStartTime(long agentStartTime) {
        this.agentStartTime = agentStartTime;
    }
}
