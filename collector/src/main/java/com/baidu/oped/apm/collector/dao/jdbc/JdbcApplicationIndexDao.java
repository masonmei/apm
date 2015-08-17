package com.baidu.oped.apm.collector.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.baidu.oped.apm.collector.dao.ApplicationIndexDao;
import com.baidu.oped.apm.thrift.dto.TAgentInfo;

/**
 * Created by mason on 8/17/15.
 */
@Repository
public class JdbcApplicationIndexDao implements ApplicationIndexDao{
    @Override
    public void insert(TAgentInfo agentInfo) {

    }
}
