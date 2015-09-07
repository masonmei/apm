package com.baidu.oped.apm.collector.dao.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.oped.apm.collector.dao.AgentStatDao;
import com.baidu.oped.apm.common.jpa.entity.AgentInstanceMap;
import com.baidu.oped.apm.common.jpa.entity.InstanceStat;
import com.baidu.oped.apm.common.jpa.repository.InstanceStatRepository;
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
public class AgentStatMemoryGcDao extends BaseService implements AgentStatDao {
    private static final Logger LOG = LoggerFactory.getLogger(AgentStatMemoryGcDao.class);

    @Autowired
    private InstanceStatRepository instanceStatRepository;

    @Override
    public void insert(TAgentStat agentStat) {
        if (agentStat == null) {
            throw new NullPointerException("agentStat must not be null");
        }

        AgentInstanceMap map = findAgentInstanceMap(agentStat.getAgentId(), agentStat.getStartTimestamp());

        if (map == null) {
            LOG.warn("AgentInstanceMap not found for agendId {} and startTime {}, this stat data will be ignored",
                            agentStat.getAgentId(), agentStat.getStartTimestamp());
            return;
        }

        InstanceStat stat = new InstanceStat();
        stat.setAppId(map.getAppId());
        stat.setInstanceId(map.getInstanceId());

        buildStat(stat, agentStat);

        instanceStatRepository.save(stat);
    }

    private void buildStat(final InstanceStat instanceStat, final TAgentStat thriftObject) {

        instanceStat.setTimestamp(thriftObject.getTimestamp());

        TJvmGc gc = thriftObject.getGc();
        if (gc != null) {
            instanceStat.setGcType(gc.getType().name());
            instanceStat.setJvmMemoryHeapUsed(gc.getJvmMemoryHeapUsed());
            instanceStat.setJvmMemoryHeapMax(gc.getJvmMemoryHeapMax());
            instanceStat.setJvmMemoryNonHeapMax(gc.getJvmMemoryNonHeapMax());
            instanceStat.setJvmMemoryNonHeapUsed(gc.getJvmMemoryNonHeapUsed());
            instanceStat.setJvmGcOldCount(gc.getJvmGcOldCount());
            instanceStat.setJvmGcOldTime(gc.getJvmGcOldTime());
        } else {
            instanceStat.setGcType(TJvmGcType.UNKNOWN.name());
        }

        final TCpuLoad cl = thriftObject.getCpuLoad();
        if (cl != null) {
            // jvmCpuLoad is optional
            if (cl.isSetJvmCpuLoad()) {
                instanceStat.setJvmCpuLoad(cl.getJvmCpuLoad());
            }
            // systemCpuLoad is optional
            if (cl.isSetSystemCpuLoad()) {
                instanceStat.setSystemCpuLoad(cl.getSystemCpuLoad());
            }
        }
    }

}
