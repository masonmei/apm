
package com.baidu.oped.apm.collector.dao;

import com.baidu.oped.apm.thrift.dto.TAgentInfo;

/**
 * class ApplicationIndexDao 
 *
 * @author meidongxu@baidu.com
 */
public interface ApplicationIndexDao {
    void insert(final TAgentInfo agentInfo);
}
