package com.baidu.oped.apm.collector.dao.jdbc;

import com.baidu.oped.apm.collector.Application;
import com.baidu.oped.apm.thrift.dto.TSqlMetaData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.baidu.oped.apm.collector.dao.jdbc.mock.ModelProviders;

import static org.junit.Assert.*;

/**
 * Created by mason on 8/18/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
public class JdbcSqlMetaDataDaoTest {

    @Autowired
    private JdbcSqlMetaDataDao metaDataDao;

    @Test
    public void testInsert() throws Exception {
        TSqlMetaData metaData = ModelProviders.sqlMetaData();
        metaDataDao.insert(metaData);
    }
}