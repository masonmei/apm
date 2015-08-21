package com.baidu.oped.apm.model.dao.jdbc;

import com.baidu.oped.apm.BaseRepository;
import com.baidu.oped.apm.common.entity.Trace;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by mason on 8/16/15.
 */
@Repository
public class JdbcTraceDao extends BaseRepository<Trace> {

    public List<Trace> queryTraceList(String applicationName, String agentId, long from, long to) {
        String sql = "application_id=? and start_time>? and start_time<?";
        if (StringUtils.isEmpty(agentId)) {
            return this.find(sql, applicationName, from, to);
        }
        sql = "agent_id=? " + sql;
        return this.find(sql, agentId, applicationName, from, to);
    }


}
