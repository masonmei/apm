package com.baidu.oped.apm.model.service;

import com.baidu.oped.apm.common.bo.AgentInfoBo;
import com.baidu.oped.apm.model.dao.AgentInfoDao;
import com.baidu.oped.apm.model.dao.ApplicationIndexDao;
import com.baidu.oped.apm.mvc.vo.Application;
import com.baidu.oped.apm.mvc.vo.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mason on 8/12/15.
 */
@Service
public class ApplicationService {
    @Autowired
    private AgentInfoDao agentInfoDao;

    @Autowired
    private ApplicationIndexDao applicationIndexDao;

    public List<Application> selectAllApplicationNames() {
        return applicationIndexDao.selectAllApplicationNames();
    }

    public List<Instance> findApplicationInstanceByApplication(String applicationName, boolean simplify) {
        Assert.hasLength(applicationName, "ApplicationName cannot be empty when finding Instances.");
        List<AgentInfoBo> agentInfoBos = agentInfoDao.findAgentInfoByApplicationName(applicationName);

        List<Instance> instanceList = agentInfoBos.stream()
                .filter(agentInfoBo ->
                                agentInfoBo.getApplicationName()!=null
                                        && applicationName.equals(agentInfoBo.getApplicationName())
                ).map(agentInfoBo -> {
                    Instance instance = new Instance();
                    instance.setName(agentInfoBo.getApplicationName());
                    instance.setInstanceId(buildInstanceId(agentInfoBo));
                    if(!simplify){
                        buildMetricInfo(instance, agentInfoBo);
                    }
                    return instance;
                }).collect(Collectors.toList());
        return instanceList;
    }

    private void buildMetricInfo(final Instance instance, final AgentInfoBo agentInfoBo) {
        Assert.notNull(instance, "Instance cannot be null while building metric for.");
        Assert.notNull(agentInfoBo, "AgentInfoBo cannot be null while building metric for.");

        instance.setErrorRate(0.0);
        instance.setCountPerMins(10.0);
        instance.setApdex(0.95);
        instance.setCpuUsage(0.032);
        instance.setMemoryUsage(1003.9);
        instance.setResponseTime(231);
    }

    private String buildInstanceId(AgentInfoBo agentInfoBo) {
        StringBuilder builder = new StringBuilder();
        builder.append("java")
                .append(":")
                .append(agentInfoBo.getApplicationName())
                .append(":")
                .append(agentInfoBo.getPorts())
                .append("(")
                .append(agentInfoBo.getHostName())
                .append(")");
        return builder.toString();
    }
}
