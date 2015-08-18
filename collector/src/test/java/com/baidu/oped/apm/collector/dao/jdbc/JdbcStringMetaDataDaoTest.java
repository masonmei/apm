package com.baidu.oped.apm.collector.dao.jdbc;

import com.baidu.oped.apm.collector.Application;
import com.baidu.oped.apm.collector.dao.jdbc.mock.ModelProviders;
import com.baidu.oped.apm.thrift.dto.TStringMetaData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by mason on 8/18/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
public class JdbcStringMetaDataDaoTest {

    @Autowired
    private JdbcStringMetaDataDao metaDataDao;
    @Test
    public void testInsert() throws Exception {
        TStringMetaData metaData = ModelProviders.stringMetaData();
        metaDataDao.insert(metaData);
    }
}