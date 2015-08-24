package com.baidu.oped.apm.common.jpa.repository;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.CriteriaQuery;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baidu.oped.apm.CommonsJpaApplication;
import com.baidu.oped.apm.common.jpa.entity.AgentInfo;
import com.baidu.oped.apm.common.jpa.entity.QAgentInfo;
import com.baidu.oped.apm.common.jpa.entity.ServerMetaData;
import com.baidu.oped.apm.common.jpa.entity.ServiceInfo;
import com.mysema.query.jpa.JPAExpressions;
import com.mysema.query.jpa.JPASubQuery;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.Projections;
import com.mysema.query.types.QBean;
import com.mysema.query.types.path.StringPath;
import com.mysema.query.types.query.StringSubQuery;

/**
 * Created by mason on 8/23/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CommonsJpaApplication.class)
public class AgentInfoRepositoryTest {

    @Autowired
    private AgentInfoRepository agentInfoRepository;

    @Test
    public void testCreate() {
        AgentInfo agentInfo = new AgentInfo();
        agentInfo.setHostName("tc-oped-dev03.tc");
        agentInfo.setIp("10.20.30.111");
        agentInfo.setPorts("80,90");
        agentInfo.setAgentId("aaaaaaaa");
        agentInfo.setApplicationName("testApp");
        agentInfo.setVersion("aaa");
        agentInfo.setServiceType(Short.parseShort("1"));
        agentInfo.setPid(1);
        agentInfo.setStartTime(1000000000L);
        agentInfo.setEndTimeStamp(2000000000L);
        agentInfo.setEndStatus(1);
        agentInfo.setServerMetaData(this.createMetaData());
        AgentInfo save = agentInfoRepository.save(agentInfo);
        assertNotNull(save.getId());
        AgentInfo one = agentInfoRepository.findOne(save.getId());
        one.setApplicationName("fsfasfdsa");
        AgentInfo info = agentInfoRepository.saveAndFlush(one);

        QAgentInfo qAgentInfo = QAgentInfo.agentInfo;
        Predicate predicate = qAgentInfo.serverMetaData.isNotNull();
        Iterable<AgentInfo> all = agentInfoRepository.findAll(predicate);
        assertNotNull(all.iterator().next());

        agentInfoRepository.delete(info.getId());
    }

    @Test
    public void testDelete() {
        //        agentInfoRepository.delete(3l);
    }

    @Test
    public void testFindDistinct() {

        List<String> distinctApplicationName = agentInfoRepository.listApplicationNames();

        assertNotNull(distinctApplicationName);
    }

    private ServerMetaData createMetaData() {
        ServerMetaData metaData = new ServerMetaData();
        List<ServiceInfo> serverInfoList = createServerInfoList();
        serverInfoList.stream().forEach(serviceInfo -> serviceInfo.setServerMetaData(metaData));
        metaData.setServiceInfos(serverInfoList);
        metaData.setVmArgs(String.join(",", Arrays.asList(new String[] {"cpu", "memory"})));
        return metaData;
    }

    private List<ServiceInfo> createServerInfoList() {
        List<ServiceInfo> serviceInfos = new ArrayList<ServiceInfo>();
        ServiceInfo serviceInfo1 = new ServiceInfo();
        serviceInfo1.setServiceName("serviceName1");
        serviceInfo1.setServiceLibs(String.join(",", Arrays.asList(new String[] {"path1", "path2"})));
        ServiceInfo serviceInfo2 = new ServiceInfo();
        serviceInfo2.setServiceName("serviceName1");
        serviceInfo2.setServiceLibs(String.join(",", Arrays.asList(new String[] {"path1", "path2"})));
        serviceInfos.add(serviceInfo1);
        serviceInfos.add(serviceInfo2);
        return serviceInfos;
    }
}