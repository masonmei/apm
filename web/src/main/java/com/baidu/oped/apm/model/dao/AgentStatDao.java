
package com.baidu.oped.apm.model.dao;

import java.util.List;

import com.baidu.oped.apm.mvc.vo.AgentStat;
import com.baidu.oped.apm.mvc.vo.Range;

/**
 * class AgentStatDao 
 *
 * @author meidongxu@baidu.com
 */
public interface AgentStatDao {

    List<AgentStat> scanAgentStatList(String agentId, Range range);

}
