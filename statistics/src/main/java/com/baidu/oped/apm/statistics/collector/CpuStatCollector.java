package com.baidu.oped.apm.statistics.collector;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.util.Assert;

import com.baidu.oped.apm.common.jpa.entity.AgentStatCpuLoad;
import com.baidu.oped.apm.common.jpa.entity.ApplicationStatistic;
import com.baidu.oped.apm.common.jpa.entity.QAgentStatCpuLoad;
import com.baidu.oped.apm.common.jpa.repository.AgentStatCpuLoadRepository;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by mason on 8/29/15.
 */
public class CpuStatCollector extends BaseCollector<AgentStatCpuLoad, ApplicationStatistic> {
    @Autowired
    private AgentStatCpuLoadRepository agentStatCpuLoadRepository;

    @Override
    public Iterable<AgentStatCpuLoad> getPeriodData(){
        QAgentStatCpuLoad qAgentStatCpuLoad = QAgentStatCpuLoad.agentStatCpuLoad;
        BooleanExpression condition = qAgentStatCpuLoad.timestamp.between(getPeriodStart(), getPeriodEnd());
        return agentStatCpuLoadRepository.findAll(condition);
    }

    @Override
    protected void saveOrUpdate(ApplicationStatistic result) {

    }

    @Override
    public ApplicationStatistic calculator(Long id, List<AgentStatCpuLoad> list) {
        Assert.notNull(list);

        return null;
    }

}
