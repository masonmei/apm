
package com.baidu.oped.apm.mvc.vo;

import com.baidu.oped.apm.common.bo.AgentInfoBo;

/**
 * class AgentStatus 
 *
 * @author meidongxu@baidu.com
 */
public class AgentStatus {

    private final boolean exists;
    private final long checkTime;
    private final AgentInfoBo agentInfo;

    public AgentStatus(AgentInfoBo agentInfoBo) {
        this.exists = agentInfoBo != null;
        this.agentInfo = agentInfoBo;
        this.checkTime = System.currentTimeMillis();
    }

    public boolean isExists() {
        return exists;
    }

    public AgentInfoBo getAgentInfo() {
        return agentInfo;
    }

    public long getCheckTime() {
        return checkTime;
    }
}
