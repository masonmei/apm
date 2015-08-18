package com.baidu.oped.apm.collector.dao.jdbc;

import com.baidu.oped.apm.collector.Application;
import com.baidu.oped.apm.thrift.dto.TApiMetaData;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * ***** description *****
 * Created with IntelliJ IDEA.
 * User: yangbolin
 * Date: 15/8/18
 * Time: 14:50
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
@ActiveProfiles("for-mac")
public class JdbcApiMetaDataDaoTest {

    @Autowired
    JdbcApiMetaDataDao dao;

    @org.junit.Test
    public void testInsert () {
        TApiMetaData apiMetaData = this.creeateApiMetaData();
        dao.insert(apiMetaData);
        Assert.assertTrue(true);
    }

    private TApiMetaData creeateApiMetaData() {
        TApiMetaData metaData = new TApiMetaData();
        metaData.setAgentId("aaaa");
        metaData.setAgentStartTime(100000L);
        metaData.setApiId(10);
        metaData.setApiInfo("aaaa");
        metaData.setLine(10);
        return metaData;
    }

}