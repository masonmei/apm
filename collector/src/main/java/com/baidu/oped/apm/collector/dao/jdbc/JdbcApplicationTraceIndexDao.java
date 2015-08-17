package com.baidu.oped.apm.collector.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.baidu.oped.apm.collector.dao.ApplicationTraceIndexDao;
import com.baidu.oped.apm.thrift.dto.TSpan;

/**
 * Created by mason on 8/17/15.
 */
@Repository
public class JdbcApplicationTraceIndexDao implements ApplicationTraceIndexDao {
    @Override
    public void insert(TSpan span) {

    }
}
