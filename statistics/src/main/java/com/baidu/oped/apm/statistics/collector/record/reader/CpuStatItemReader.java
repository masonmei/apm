package com.baidu.oped.apm.statistics.collector.record.reader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.oped.apm.common.jpa.entity.AgentStatCpuLoad;
import com.baidu.oped.apm.common.jpa.entity.QAgentStatCpuLoad;
import com.baidu.oped.apm.common.jpa.repository.AgentStatCpuLoadRepository;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by mason on 8/31/15.
 */
@Component
public class CpuStatItemReader extends BaseReader<AgentStatCpuLoad> {

    @Autowired
    private AgentStatCpuLoadRepository agentStatCpuLoadRepository;

    public CpuStatItemReader(long periodStart, long periodInMills) {
        super(periodStart, periodInMills);
    }

    public Iterable<AgentStatCpuLoad> readItems() {
        QAgentStatCpuLoad qAgentStatCpuLoad = QAgentStatCpuLoad.agentStatCpuLoad;
        BooleanExpression condition = qAgentStatCpuLoad.timestamp.between(getPeriodStart(), getPeriodEnd());
        return agentStatCpuLoadRepository.findAll(condition);
    }
}
