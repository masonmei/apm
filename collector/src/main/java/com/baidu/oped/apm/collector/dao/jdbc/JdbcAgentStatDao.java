package com.baidu.oped.apm.collector.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.baidu.oped.apm.collector.dao.AgentStatDao;
import com.baidu.oped.apm.thrift.dto.TAgentStat;

/**
 * Created by mason on 8/17/15.
 */
@Repository
public class JdbcAgentStatDao implements AgentStatDao{
    @Override
    public void insert(TAgentStat agentStat) {

    }
}
