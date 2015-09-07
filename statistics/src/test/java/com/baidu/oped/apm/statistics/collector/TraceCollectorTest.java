package com.baidu.oped.apm.statistics.collector;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baidu.oped.apm.statistics.Application;

/**
 * Created by mason on 9/4/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class TraceCollectorTest {

    @Autowired
    private TraceCollector traceCollector;

    @Test
    public void testCollect() throws Exception {
        traceCollector.collect(1441343234250l, 3600000l);
    }
}