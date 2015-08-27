package com.baidu.oped.apm.model.service;

import com.baidu.oped.apm.common.jpa.entity.ApplicationStatistic;
import com.baidu.oped.apm.common.jpa.entity.QApplicationStatistic;
import com.baidu.oped.apm.common.jpa.repository.ApplicationStatisticRepository;
import com.baidu.oped.apm.utils.PageUtils;
import com.mysema.query.types.expr.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Created by mason on 8/12/15.
 */
@Service
public class ApplicationService {

    @Autowired
    ApplicationStatisticRepository applicationStatisticRepository;


//    public List<Instance> findApplicationInstanceByApplication(String applicationName, boolean simplify) {
//        Assert.hasLength(applicationName, "ApplicationName cannot be empty when finding Instances.");
//        QAgentInfo qAgentInfo = QAgentInfo.agentInfo;
//        BooleanExpression appNameEqualExp = qAgentInfo.applicationName.eq(applicationName);
//        Iterable<AgentInfo> agentInfoIterable = agentInfoRepository.findAll(appNameEqualExp);
//        return StreamSupport.stream(agentInfoIterable.spliterator(), false).map(agentInfoBo -> {
//            Instance instance = new Instance();
//            instance.setName(agentInfoBo.getApplicationName());
//            instance.setInstanceId(buildInstanceId(agentInfoBo));
//            if (!simplify) {
//                buildMetricInfo(instance, agentInfoBo);
//            }
//            return instance;
//        }).collect(Collectors.toList());
//    }

//    private void buildMetricInfo(final Instance instance, final AgentInfo agentInfoBo) {
//        Assert.notNull(instance, "Instance cannot be null while building metric for.");
//        Assert.notNull(agentInfoBo, "AgentInfoBo cannot be null while building metric for.");
//
//        instance.setErrorRate(0.0);
//        instance.setCountPerMins(10.0);
//        instance.setApdex(0.95);
//        instance.setCpuUsage(0.032);
//        instance.setMemoryUsage(1003.9);
//        instance.setResponseTime(231);
//    }
//
//    private String buildInstanceId(AgentInfo agentInfoBo) {
//        StringBuilder builder = new StringBuilder();
//        builder.append("java").append(":").append(agentInfoBo.getApplicationName()).append(":")
//                .append(agentInfoBo.getPorts()).append("(").append(agentInfoBo.getHostName()).append(")");
//        return builder.toString();
//    }

    public Page<ApplicationStatistic> selectApplications(LocalDateTime from, LocalDateTime to,
                                                         String orderBy, int pageSize, int pageNumber) {

        QApplicationStatistic appStatistic = QApplicationStatistic.applicationStatistic;
        BooleanExpression timeGtFrom = appStatistic.timestamp.gt(from.atZone(ZoneId.systemDefault()).
                toInstant().toEpochMilli());
        BooleanExpression timeltTo = appStatistic.timestamp.lt(to.atZone(ZoneId.systemDefault()).
                toInstant().toEpochMilli());
        BooleanExpression condtions = timeGtFrom.and(timeltTo);

        Sort sort = PageUtils.toSort(orderBy);
        Pageable pageable = new PageRequest(pageNumber, pageSize, sort);

        return applicationStatisticRepository.findAll(condtions, pageable);

    }

}
