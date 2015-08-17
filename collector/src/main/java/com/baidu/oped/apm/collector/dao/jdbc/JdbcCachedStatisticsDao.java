package com.baidu.oped.apm.collector.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.baidu.oped.apm.collector.dao.CachedStatisticsDao;

/**
 * Created by mason on 8/17/15.
 */
@Repository
public class JdbcCachedStatisticsDao implements CachedStatisticsDao {
    @Override
    public void flushAll() {

    }
}
