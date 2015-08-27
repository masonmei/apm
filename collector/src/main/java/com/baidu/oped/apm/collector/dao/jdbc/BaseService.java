package com.baidu.oped.apm.collector.dao.jdbc;

import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.oped.apm.common.jpa.entity.AgentInstanceMap;
import com.baidu.oped.apm.common.jpa.entity.QAgentInstanceMap;
import com.baidu.oped.apm.common.jpa.repository.AgentInstanceMapRepository;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by mason on 8/27/15.
 */
public abstract class BaseService {

    @Autowired
    protected AgentInstanceMapRepository agentInstanceMapRepository;

    protected AgentInstanceMap findAgentInstanceMap(String agentId, long startTimestamp) {
        QAgentInstanceMap qAgentInstanceMap = QAgentInstanceMap.agentInstanceMap;
        BooleanExpression agentIdCondition = qAgentInstanceMap.agentId.eq(agentId);
        BooleanExpression startTimeCondition = qAgentInstanceMap.agentStartTime.eq(startTimestamp);
        BooleanExpression whereCondition = agentIdCondition.and(startTimeCondition);

        return agentInstanceMapRepository.findOne(whereCondition);
    }
}
