package com.baidu.oped.apm.statistics.collector.record.reader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.oped.apm.common.jpa.entity.AgentStatMemoryGc;
import com.baidu.oped.apm.common.jpa.entity.QAgentStatMemoryGc;
import com.baidu.oped.apm.common.jpa.repository.AgentStatMemoryGcRepository;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by mason on 8/31/15.
 */
@Component
public class MemoryStatItemReader extends BaseReader<AgentStatMemoryGc> {
    @Autowired
    private AgentStatMemoryGcRepository agentStatMemoryGcRepository;

    protected MemoryStatItemReader(long periodStart, long periodInMills) {
        super(periodStart, periodInMills);
    }

    @Override
    public Iterable<AgentStatMemoryGc> readItems() {
        QAgentStatMemoryGc qAgentStatMemoryGc = QAgentStatMemoryGc.agentStatMemoryGc;
        BooleanExpression whereCondition = qAgentStatMemoryGc.timestamp.between(getPeriodStart(), getPeriodEnd());
        return agentStatMemoryGcRepository.findAll(whereCondition);
    }
}
