package com.baidu.oped.apm.collector.dao.jdbc;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baidu.oped.apm.collector.Application;
import com.baidu.oped.apm.collector.dao.jdbc.mock.ModelProviders;
import com.baidu.oped.apm.thrift.dto.TSpan;

/**
 * Created by mason on 8/17/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
public class JdbcTraceDaoTest {

    @Autowired
    private JdbcTraceDao traceDao;

    @Test
    public void testInsert() throws Exception {
        TSpan tSpan = ModelProviders.tSpan();
        traceDao.insert(tSpan);
    }

    @Test
    public void testInsertSpanChunk() throws Exception {

    }
}