
package com.baidu.oped.apm.collector.dao;

import com.baidu.oped.apm.thrift.dto.TAgentStat;

/**
 * class AgentStatDao 
 *
 * @author meidongxu@baidu.com
 */
public interface AgentStatDao {
    void insert(TAgentStat agentStat);
}
