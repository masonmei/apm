package com.baidu.oped.apm.collector.dao.jdbc;

import com.baidu.oped.apm.collector.Application;
import com.baidu.oped.apm.thrift.dto.TAgentInfo;
import com.baidu.oped.apm.thrift.dto.TServerMetaData;
import com.baidu.oped.apm.thrift.dto.TServiceInfo;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by mason on 8/15/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
public class JdbcAgentInfoDaoTest {

    @Autowired
    private JdbcAgentInfoDao agentInfoDao;

    @org.junit.Test
    public void testInsert() throws Exception {
        TAgentInfo agentInfo = this.createAgent();
        agentInfoDao.insert(agentInfo);
        assertTrue(true);
    }

    private TAgentInfo createAgent() {
        TAgentInfo agentInfo = new TAgentInfo();
        agentInfo.setHostname("tc-oped-dev03.tc");
        agentInfo.setIp("10.20.30.111");
        agentInfo.setPorts("80,90");
        agentInfo.setAgentId("aaaaaaaa");
        agentInfo.setApplicationName("testApp");
        agentInfo.setVmVersion("aaa");
        agentInfo.setServiceType(Short.parseShort("1"));
        agentInfo.setPid(1);
        agentInfo.setStartTimestamp(1000000000L);
        agentInfo.setEndTimestamp(2000000000L);
        agentInfo.setEndStatus(1);
        agentInfo.setServerMetaData(this.createMetaData());
        return  agentInfo;
    }

    private TServerMetaData createMetaData() {
        TServerMetaData metaData = new TServerMetaData();
        metaData.setServerInfo("server1");
        metaData.setVmArgs(Arrays.asList(new String[]{"cpu", "memory"}));
        metaData.setServiceInfos(this.createServerInfoList());
        return metaData;
    }

    private List<TServiceInfo> createServerInfoList() {
        List<TServiceInfo> serviceInfos = new ArrayList<TServiceInfo>();
        TServiceInfo serviceInfo1 = new TServiceInfo();
        serviceInfo1.setServiceName("serviceName1");
        serviceInfo1.setServiceLibs(Arrays.asList(new String[]{"path1", "path2"}));
        TServiceInfo serviceInfo2 = new TServiceInfo();
        serviceInfo2.setServiceName("serviceName1");
        serviceInfo2.setServiceLibs(Arrays.asList(new String[]{"path1", "path2"}));
        serviceInfos.add(serviceInfo1);
        serviceInfos.add(serviceInfo2);
        return serviceInfos;
    }

}