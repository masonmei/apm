package com.baidu.oped.apm.collector.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.baidu.oped.apm.collector.dao.StringMetaDataDao;
import com.baidu.oped.apm.thrift.dto.TStringMetaData;

/**
 * Created by mason on 8/17/15.
 */
@Repository
public class JdbcStringMetaDataDao implements StringMetaDataDao {
    @Override
    public void insert(TStringMetaData stringMetaData) {

    }
}
