package com.baidu.oped.apm.collector.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.baidu.oped.apm.collector.dao.ApiMetaDataDao;
import com.baidu.oped.apm.thrift.dto.TApiMetaData;

/**
 * Created by mason on 8/17/15.
 */
@Repository
public class JdbcApiMetaDataDao implements ApiMetaDataDao {
    @Override
    public void insert(TApiMetaData apiMetaData) {

    }
}
