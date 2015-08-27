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
import com.baidu.oped.apm.common.jpa.entity.QApplication;
import com.baidu.oped.apm.common.jpa.entity.QInstance;
import com.baidu.oped.apm.common.jpa.repository.ApplicationRepository;
import com.baidu.oped.apm.common.jpa.repository.InstanceRepository;
import com.baidu.oped.apm.thrift.dto.TAgentInfo;
import com.mysema.query.types.expr.BooleanExpression;

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

        Application existApplication = findExistApplication(agentInfo);
        if (existApplication == null) {
            existApplication = applicationRepository.save(buildApplication(agentInfo));
        }

        Instance existInstance = findExistInstance(existApplication, agentInfo);
        if (existInstance == null) {
            existInstance = instanceRepository.save(buildInstance(existApplication, agentInfo));
        } else {
            existInstance.setPid(agentInfo.getPid());
            existInstance.setStartTime(agentInfo.getStartTimestamp());

            if (agentInfo.getServerMetaData() != null) {
                final List<String> vmArgs = agentInfo.getServerMetaData().getVmArgs();
                existInstance.setArgs(String.join(",", vmArgs));
            }
            instanceRepository.saveAndFlush(existInstance);
        }

        agentInstanceMapRepository.save(buildAgentInstanceMap(existInstance, agentInfo));
    }

    private Application findExistApplication(TAgentInfo agentInfo) {
        Assert.notNull(agentInfo, "cannot build instance with empty agentInfo");

        QApplication qApplication = QApplication.application;
        BooleanExpression appNameCondition = qApplication.appName.eq(agentInfo.getApplicationName());
        BooleanExpression userIdCondition = qApplication.userId.eq("testuser");
        BooleanExpression appTypeCondition = qApplication.appType.eq(ApplicationType.JAVA.name());
        BooleanExpression whereCondition = appNameCondition.and(userIdCondition).and(appTypeCondition);
        Application application = applicationRepository.findOne(whereCondition);
        return application;
    }

    private Application buildApplication(TAgentInfo agentInfo) {
        Assert.notNull(agentInfo, "cannot build instance with empty agentInfo");

        Application application = new Application();
        application.setAppName(agentInfo.getApplicationName());
        application.setAppType(ApplicationType.JAVA.name());
        application.setUserId("testuser");
        return application;
    }

    private Instance buildInstance(Application application, TAgentInfo agentInfo) {
        Assert.notNull(application, "cannot build instance for not exist application");
        Assert.notNull(agentInfo, "cannot build instance with empty agentInfo");
        Assert.notNull(application.getId(), "cannot find instance for non-persist application");

        Instance instance = new Instance();
        instance.setAppId(application.getId());
        instance.setHost(agentInfo.getHostname());
        int serviceType = agentInfo.getServiceType();
        instance.setInstanceType(serviceType);
        instance.setPid(agentInfo.getPid());
        instance.setIp(agentInfo.getIp());

        String ports = agentInfo.getPorts();
        if (!StringUtils.isEmpty(ports)) {
            Integer port = Integer.parseInt(ports);
            instance.setPort(port);
        } else {
            instance.setPort(-1);
        }
        instance.setStartTime(agentInfo.getStartTimestamp());

        if (agentInfo.getServerMetaData() != null) {
            final List<String> vmArgs = agentInfo.getServerMetaData().getVmArgs();
            instance.setArgs(String.join(",", vmArgs));
        }

        return instance;
    }

    private Instance findExistInstance(Application application, TAgentInfo agentInfo) {
        Assert.notNull(application, "cannot find instance for not exist application");
        Assert.notNull(agentInfo, "cannot find instance with empty agentInfo");
        Assert.notNull(application.getId(), "cannot find instance for non-persist application");
        QInstance qInstance = QInstance.instance;
        BooleanExpression appIdCondition = qInstance.appId.eq(application.getId());
        int serviceType = agentInfo.getServiceType();
        BooleanExpression instanceTypeCondition = qInstance.instanceType.eq(serviceType);
        BooleanExpression hostCondition = qInstance.host.eq(agentInfo.getHostname());

        String ports = agentInfo.getPorts();
        BooleanExpression portCondition;
        if (!StringUtils.isEmpty(ports)) {
            Integer port = Integer.parseInt(ports);
            portCondition = qInstance.port.eq(port);
        } else {
            portCondition = qInstance.port.eq(-1);
        }
        BooleanExpression whereCondition =
                appIdCondition.and(instanceTypeCondition).and(hostCondition).and(portCondition);

        return instanceRepository.findOne(whereCondition);
    }

    private AgentInstanceMap buildAgentInstanceMap(Instance instance, TAgentInfo agentInfo) {
        Assert.notNull(instance, "cannot build agent instance map for not exist instance");
        Assert.notNull(agentInfo, "cannot build agent instance map with empty agentInfo");
        Assert.notNull(instance.getId(), "cannot build agent instance map for non-persist instance");

        AgentInstanceMap map = new AgentInstanceMap();
        map.setAgentId(agentInfo.getAgentId());
        map.setAgentStartTime(agentInfo.getStartTimestamp());
        map.setAppId(instance.getAppId());
        map.setInstanceId(instance.getId());
        return map;
    }

}
