package com.baidu.oped.apm.collector.dao.jdbc;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.baidu.oped.apm.collector.dao.MapResponseTimeDao;

/**
 * Created by mason on 8/17/15.
 */
@Component
public class JdbcMapResponseTimeDao implements MapResponseTimeDao {
    @Override
    public void received(String applicationName, short serviceType, String agentId, int elapsed, boolean isError) {

    }

    @Override
    public void flushAll() {

    }
}
