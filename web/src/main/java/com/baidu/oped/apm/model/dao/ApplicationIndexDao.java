
package com.baidu.oped.apm.model.dao;

import java.util.List;

import com.baidu.oped.apm.Application;

/**
 * class ApplicationIndexDao 
 *
 * @author meidongxu@baidu.com
 */
public interface ApplicationIndexDao {
    List<Application> selectAllApplicationNames();

    List<String> selectAgentIds(String applicationName);

    void deleteApplicationName(String applicationName);

    void deleteAgentId(String applicationName, String agentId);
}
