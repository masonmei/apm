
package com.baidu.oped.apm.mvc.vo;

import com.baidu.oped.apm.common.bo.AgentStatCpuLoadBo;
import com.baidu.oped.apm.common.bo.AgentStatMemoryGcBo;

/**
 * class AgentStat 
 *
 * @author meidongxu@baidu.com
 */
public class AgentStat {

    private AgentStatMemoryGcBo memoryGc;
    private AgentStatCpuLoadBo cpuLoad;

    public AgentStatMemoryGcBo getMemoryGc() {
        return memoryGc;
    }

    public void setMemoryGc(AgentStatMemoryGcBo memoryGc) {
        this.memoryGc = memoryGc;
    }

    public AgentStatCpuLoadBo getCpuLoad() {
        return cpuLoad;
    }

    public void setCpuLoad(AgentStatCpuLoadBo cpuLoad) {
        this.cpuLoad = cpuLoad;
    }
}
