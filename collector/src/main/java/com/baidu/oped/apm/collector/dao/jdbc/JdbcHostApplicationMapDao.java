package com.baidu.oped.apm.collector.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.baidu.oped.apm.collector.dao.HostApplicationMapDao;

/**
 * Created by mason on 8/17/15.
 */
@Repository
public class JdbcHostApplicationMapDao implements HostApplicationMapDao {
    @Override
    public void insert(String host, String bindApplicationName, short bindServiceType, String parentApplicationName,
                       short parentServiceType) {

    }
}
