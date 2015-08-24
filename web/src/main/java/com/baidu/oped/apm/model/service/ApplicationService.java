package com.baidu.oped.apm.model.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.baidu.oped.apm.common.jpa.entity.AgentInfo;
import com.baidu.oped.apm.common.jpa.entity.QAgentInfo;
import com.baidu.oped.apm.common.jpa.repository.AgentInfoRepository;
import com.baidu.oped.apm.mvc.vo.Instance;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by mason on 8/12/15.
 */
@Service
public class ApplicationService {

    @Autowired
    private AgentInfoRepository agentInfoRepository;

    public List<String> selectAllApplicationNames() {
        return agentInfoRepository.listApplicationNames();
    }

    public List<Instance> findApplicationInstanceByApplication(String applicationName, boolean simplify) {
        Assert.hasLength(applicationName, "ApplicationName cannot be empty when finding Instances.");
        QAgentInfo qAgentInfo = QAgentInfo.agentInfo;
        BooleanExpression appNameEqualExp = qAgentInfo.applicationName.eq(applicationName);
        Iterable<AgentInfo> agentInfoIterable = agentInfoRepository.findAll(appNameEqualExp);
        return StreamSupport.stream(agentInfoIterable.spliterator(), false).map(agentInfoBo -> {
            Instance instance = new Instance();
            instance.setName(agentInfoBo.getApplicationName());
            instance.setInstanceId(buildInstanceId(agentInfoBo));
            if (!simplify) {
                buildMetricInfo(instance, agentInfoBo);
            }
            return instance;
        }).collect(Collectors.toList());
    }

    private void buildMetricInfo(final Instance instance, final AgentInfo agentInfoBo) {
        Assert.notNull(instance, "Instance cannot be null while building metric for.");
        Assert.notNull(agentInfoBo, "AgentInfoBo cannot be null while building metric for.");

        instance.setErrorRate(0.0);
        instance.setCountPerMins(10.0);
        instance.setApdex(0.95);
        instance.setCpuUsage(0.032);
        instance.setMemoryUsage(1003.9);
        instance.setResponseTime(231);
    }

    private String buildInstanceId(AgentInfo agentInfoBo) {
        StringBuilder builder = new StringBuilder();
        builder.append("java").append(":").append(agentInfoBo.getApplicationName()).append(":")
                .append(agentInfoBo.getPorts()).append("(").append(agentInfoBo.getHostName()).append(")");
        return builder.toString();
    }
}
