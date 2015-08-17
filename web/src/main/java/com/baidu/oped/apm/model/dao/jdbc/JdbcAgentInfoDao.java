package com.baidu.oped.apm.model.dao.jdbc;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.baidu.oped.apm.common.bo.AgentInfoBo;
import com.baidu.oped.apm.model.dao.AgentInfoDao;
import com.baidu.oped.apm.mvc.vo.Range;

/**
 * Created by mason on 8/16/15.
 */
@Repository
public class JdbcAgentInfoDao implements AgentInfoDao {
    @Override
    public AgentInfoBo findAgentInfoBeforeStartTime(String agentId, long currentTime) {
        return null;
    }

    @Override
    public List<AgentInfoBo> getAgentInfo(String agentId, Range range) {
        return null;
    }

    @Override
    public List<AgentInfoBo> findAgentInfoByApplicationName(String application) {
        return null;
    }
}
