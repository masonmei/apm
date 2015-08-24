package com.baidu.oped.apm.collector.dao.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.oped.apm.collector.dao.AgentStatDao;
import com.baidu.oped.apm.common.jpa.entity.AgentStatCpuLoad;
import com.baidu.oped.apm.common.jpa.entity.AgentStatMemoryGc;
import com.baidu.oped.apm.common.jpa.repository.AgentStatCpuLoadRepository;
import com.baidu.oped.apm.common.jpa.repository.AgentStatMemoryGcRepository;
import com.baidu.oped.apm.thrift.dto.TAgentStat;
import com.baidu.oped.apm.thrift.dto.TCpuLoad;
import com.baidu.oped.apm.thrift.dto.TJvmGc;
import com.baidu.oped.apm.thrift.dto.TJvmGcType;

/**
 * ***** description *****
 * Created with IntelliJ IDEA.
 * User: yangbolin
 * Date: 15/8/18
 * Time: 11:31
 * To change this template use File | Settings | File Templates.
 */
@Component
public class AgentStatMemoryGcDao implements AgentStatDao {

    @Autowired
    AgentStatCpuLoadRepository cpuLoadRepository;

    @Autowired
    AgentStatMemoryGcRepository memoryGcRepository;

    @Override
    public void insert(TAgentStat agentStat) {
        if (agentStat == null) {
            throw new NullPointerException("agentStat must not be null");
        }

        AgentStatMemoryGc memoryGc = this.parseMemoryGc(agentStat);
        AgentStatCpuLoad cpuLoad = this.parseCpuLoad(agentStat);
        memoryGcRepository.save(memoryGc);
        cpuLoadRepository.save(cpuLoad);
    }

    private AgentStatMemoryGc parseMemoryGc(TAgentStat thriftObject) {
        AgentStatMemoryGc memoryGc = new AgentStatMemoryGc();
        memoryGc.setAgentId(thriftObject.getAgentId());
        memoryGc.setStartTimestamp(thriftObject.getStartTimestamp());
        memoryGc.setTimestamp(thriftObject.getTimestamp());

        TJvmGc gc = thriftObject.getGc();
        if (gc != null) {
            memoryGc.setGcType(gc.getType().name());
            memoryGc.setJvmMemoryHeapUsed(gc.getJvmMemoryHeapUsed());
            memoryGc.setJvmMemoryHeapMax(gc.getJvmMemoryHeapMax());
            memoryGc.setJvmMemoryNonHeapMax(gc.getJvmMemoryNonHeapMax());
            memoryGc.setJvmMemoryNonHeapUsed(gc.getJvmMemoryNonHeapUsed());
            memoryGc.setJvmGcOldCount(gc.getJvmGcOldCount());
            memoryGc.setJvmGcOldTime(gc.getJvmGcOldTime());
        } else {
            memoryGc.setGcType(TJvmGcType.UNKNOWN.name());
        }
        return memoryGc;
    }

    private AgentStatCpuLoad parseCpuLoad(TAgentStat thriftObject) {
        AgentStatCpuLoad cpuLoad = new AgentStatCpuLoad();
        cpuLoad.setAgentId(thriftObject.getAgentId());
        cpuLoad.setStartTimestamp(thriftObject.getStartTimestamp());
        cpuLoad.setTimestamp(thriftObject.getTimestamp());
        final TCpuLoad cl = thriftObject.getCpuLoad();
        if (cl != null) {
            // jvmCpuLoad is optional
            if (cl.isSetJvmCpuLoad()) {
                cpuLoad.setJvmCpuLoad(cl.getJvmCpuLoad());
            }
            // systemCpuLoad is optional
            if (cl.isSetSystemCpuLoad()) {
                cpuLoad.setSystemCpuLoad(cl.getSystemCpuLoad());
            }
        }
        return cpuLoad;
    }

}
