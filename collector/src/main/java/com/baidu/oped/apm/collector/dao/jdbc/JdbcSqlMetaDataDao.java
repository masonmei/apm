package com.baidu.oped.apm.collector.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.baidu.oped.apm.collector.dao.SqlMetaDataDao;
import com.baidu.oped.apm.thrift.dto.TSqlMetaData;

/**
 * Created by mason on 8/17/15.
 */
@Repository
public class JdbcSqlMetaDataDao implements SqlMetaDataDao {
    @Override
    public void insert(TSqlMetaData sqlMetaData) {

    }
}
