package com.baidu.oped.apm.collector.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.baidu.oped.apm.collector.dao.MapStatisticsCalleeDao;

/**
 * Created by mason on 8/17/15.
 */
@Repository
public class JdbcMapStatisticsCalleeDao implements MapStatisticsCalleeDao {
    @Override
    public void update(String calleeApplicationName, short calleeServiceType, String callerApplicationName,
                       short callerServiceType, String callerHost, int elapsed, boolean isError) {

    }

    @Override
    public void flushAll() {

    }
}
