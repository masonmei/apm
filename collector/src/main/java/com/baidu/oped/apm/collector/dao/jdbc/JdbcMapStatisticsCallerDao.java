package com.baidu.oped.apm.collector.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.baidu.oped.apm.collector.dao.MapStatisticsCallerDao;

/**
 * Created by mason on 8/17/15.
 */
@Repository
public class JdbcMapStatisticsCallerDao implements MapStatisticsCallerDao {
    @Override
    public void update(String callerApplicationName, short callerServiceType, String callerAgentId,
                       String calleeApplicationName, short calleeServiceType, String calleeHost, int elapsed,
                       boolean isError) {

    }

    @Override
    public void flushAll() {

    }
}
