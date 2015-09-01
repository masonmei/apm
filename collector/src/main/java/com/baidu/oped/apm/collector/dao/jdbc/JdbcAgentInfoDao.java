package com.baidu.oped.apm.collector.dao.jdbc;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.baidu.oped.apm.collector.dao.AgentInfoDao;
import com.baidu.oped.apm.common.ApplicationType;
import com.baidu.oped.apm.common.jpa.entity.AgentInstanceMap;
import com.baidu.oped.apm.common.jpa.entity.Application;
import com.baidu.oped.apm.common.jpa.entity.Instance;
import com.baidu.oped.apm.common.jpa.repository.ApplicationRepository;
import com.baidu.oped.apm.common.jpa.repository.InstanceRepository;
import com.baidu.oped.apm.thrift.dto.TAgentInfo;

/**
 * Created by mason on 8/15/15.
 */
@Component
public class JdbcAgentInfoDao extends BaseService implements AgentInfoDao {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private InstanceRepository instanceRepository;

    @Override
    public void insert(TAgentInfo agentInfo) {
        if (agentInfo == null) {
            throw new NullPointerException("agentInfo must not be null");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("insert agent info. {}", agentInfo);
        }

        AgentInstanceMap map = findAgentInstanceMap(agentInfo.getAgentId(), agentInfo.getStartTimestamp());

        Application existApplication = applicationRepository.findOne(map.getAppId());
        if (existApplication != null) {
            mergeApplication(existApplication, agentInfo);
            applicationRepository.saveAndFlush(existApplication);
        }

        Instance existInstance = instanceRepository.findOne(map.getInstanceId());
        if (existInstance != null) {
            mergeInstance(existInstance, agentInfo);
            instanceRepository.saveAndFlush(existInstance);
        }
    }

    private void mergeInstance(Instance existInstance, TAgentInfo agentInfo) {
        Assert.notNull(agentInfo, "cannot merge null agentInfo to an instance");
        Assert.notNull(existInstance, "cannot merge information to a not exist instance");
        existInstance.setHost(agentInfo.getHostname());
        int serviceType = agentInfo.getServiceType();
        existInstance.setInstanceType(serviceType);
        existInstance.setPid(agentInfo.getPid());
        existInstance.setIp(agentInfo.getIp());

        String ports = agentInfo.getPorts();
        if (!StringUtils.isEmpty(ports)) {
            Integer port = Integer.parseInt(ports);
            existInstance.setPort(port);
        } else {
            existInstance.setPort(-1);
        }
        existInstance.setStartTime(agentInfo.getStartTimestamp());

        if (agentInfo.getServerMetaData() != null) {
            final List<String> vmArgs = agentInfo.getServerMetaData().getVmArgs();
            existInstance.setArgs(String.join(",", vmArgs));
        }
    }

    private void mergeApplication(final Application existApplication, final TAgentInfo agentInfo) {
        Assert.notNull(agentInfo, "cannot merge null agentInfo to instance");
        Assert.notNull(existApplication, "cannot merge information to unExistApplication");
        existApplication.setAppName(agentInfo.getApplicationName());
        existApplication.setAppType(ApplicationType.JAVA.name());
        existApplication.setUserId("testuser");
    }
}
