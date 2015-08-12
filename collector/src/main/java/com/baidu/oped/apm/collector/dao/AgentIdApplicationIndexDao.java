
package com.baidu.oped.apm.collector.dao;

/**
 * class AgentIdApplicationIndexDao 
 *
 * @author meidongxu@baidu.com
 */
@Deprecated
public interface AgentIdApplicationIndexDao {
    void insert(String agentId, String applicationName);

    String selectApplicationName(String agentId);
}
