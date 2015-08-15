package com.baidu.oped.apm.collector.dao.jdbc;

import static org.junit.Assert.assertEquals;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baidu.oped.apm.collector.Application;

/**
 * Created by mason on 8/15/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
@ActiveProfiles("for-mac")
public class JdbcAgentInfoDaoTest {

    @Autowired
    private JdbcAgentInfoDao agentInfoDao;

    @org.junit.Test
    public void testInsert() throws Exception {

        assertEquals(1, 1);
    }
}