package com.baidu.oped.apm.collector.dao.jdbc;

import com.baidu.oped.apm.collector.Application;
import com.baidu.oped.apm.thrift.dto.TAgentStat;
import com.baidu.oped.apm.thrift.dto.TCpuLoad;
import com.baidu.oped.apm.thrift.dto.TJvmGc;
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
 * Time: 14:23
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
@ActiveProfiles("for-mac")
public class AgentStatMemoryGcDaoTest {

    @Autowired
    private AgentStatMemoryGcDao dao;

    @org.junit.Test
    public void testInsert() {
        TAgentStat agentStat = this.createAgentStat();
        dao.insert(agentStat);
        Assert.assertTrue(true);
    }

    private TAgentStat createAgentStat() {
        TAgentStat agentStat = new TAgentStat();

        agentStat.setAgentId("aaaaaaa");
        agentStat.setStartTimestamp(100000000L);
        agentStat.setTimestamp(100000L);
        TJvmGc gc = new TJvmGc();
        gc.setJvmMemoryHeapUsed(50l);
        gc.setJvmMemoryHeapMax(50l);
        gc.setJvmMemoryNonHeapUsed(50l);
        gc.setJvmMemoryNonHeapMax(50l);
        gc.setJvmGcOldCount(20l);
        gc.setJvmGcOldTime(20l);
        agentStat.setGc(gc);

        TCpuLoad cpuLoad = new TCpuLoad();
        cpuLoad.setJvmCpuLoad(20l);
        cpuLoad.setSystemCpuLoad(20l);
        agentStat.setCpuLoad(cpuLoad);

        return agentStat;
    }

}