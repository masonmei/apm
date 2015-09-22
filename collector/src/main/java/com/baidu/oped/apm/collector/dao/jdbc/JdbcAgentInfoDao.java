package com.baidu.oped.apm.collector.dao.jdbc;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.baidu.oped.apm.collector.dao.AgentInfoDao;
import com.baidu.oped.apm.common.jpa.entity.AgentInstanceMap;
import com.baidu.oped.apm.common.jpa.entity.Application;
import com.baidu.oped.apm.common.jpa.entity.Instance;
import com.baidu.oped.apm.common.jpa.repository.ApplicationRepository;
import com.baidu.oped.apm.common.jpa.repository.InstanceRepository;
import com.baidu.oped.apm.common.types.ApplicationType;
import com.baidu.oped.apm.thrift.dto.TAgentInfo;

/**
 * Created by mason on 8/15/15.
 */
@Component
public class JdbcAgentInfoDao extends BaseService implements AgentInfoDao {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void insert(TAgentInfo agentInfo) {
        if (agentInfo == null) {
            throw new NullPointerException("agentInfo must not be null");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("insert agent info. {}", agentInfo);
        }

        AgentInstanceMap map = findAgentInstanceMap(agentInfo.getAgentId(), agentInfo.getStartTimestamp());

        Application application = findOrCreateApplication(agentInfo);

        Instance instance = findOrCreateInstance(application.getId(), agentInfo);

        mergeInstance(instance, agentInfo);
        instanceRepository.saveAndFlush(instance);

        map.setAppId(application.getId());
        map.setInstanceId(instance.getId());
        agentInstanceMapRepository.saveAndFlush(map);
    }

    private void mergeInstance(Instance existInstance, TAgentInfo agentInfo) {
        Assert.notNull(agentInfo, "cannot merge null agentInfo to an instance");
        Assert.notNull(existInstance, "cannot merge information to a not exist instance");
        existInstance.setHost(agentInfo.getHostname());
        existInstance.setPid(agentInfo.getPid());
        existInstance.setStartTime(agentInfo.getStartTimestamp());
        if (agentInfo.getServerMetaData() != null) {
            final List<String> vmArgs = agentInfo.getServerMetaData().getVmArgs();
            existInstance.setArgs(String.join(",", vmArgs));
        }
    }

    private Application findOrCreateApplication(final TAgentInfo agentInfo) {
        Assert.notNull(agentInfo, "cannot merge null agentInfo to instance");
        return findApplication(agentInfo.getApplicationName(), ApplicationType.JAVA.name(), "testuser");
    }

    private Instance findOrCreateInstance(final Long appId, final TAgentInfo agentInfo) {
        Assert.notNull(agentInfo, "cannot merge null agentInfo to an instance");
        Integer port;

        String ports = agentInfo.getPorts();
        if (!StringUtils.isEmpty(ports)) {
            port = Integer.parseInt(ports);
        } else {
            port = -1;
        }

        int instanceType = agentInfo.getServiceType();
        return findInstance(appId, agentInfo.getIp(), port, instanceType);
    }


}
