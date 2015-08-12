
package com.baidu.oped.apm.collector.dao;

import com.baidu.oped.apm.thrift.dto.TAgentInfo;

/**
 * class AgentInfoDao 
 *
 * @author meidongxu@baidu.com
 */
public interface AgentInfoDao {
    void insert(TAgentInfo agentInfo);
}
