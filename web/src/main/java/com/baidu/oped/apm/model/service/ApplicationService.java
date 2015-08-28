package com.baidu.oped.apm.model.service;

import com.baidu.oped.apm.common.jpa.entity.*;
import com.baidu.oped.apm.common.jpa.repository.ApplicationRepository;
import com.baidu.oped.apm.common.jpa.repository.ApplicationStatisticRepository;
import com.baidu.oped.apm.common.jpa.repository.InstanceRepository;
import com.baidu.oped.apm.common.jpa.repository.InstanceStatisticRepository;
import com.baidu.oped.apm.mvc.vo.ApplicationVo;
import com.baidu.oped.apm.mvc.vo.InstanceVo;
import com.baidu.oped.apm.mvc.vo.QueryResponse;
import com.baidu.oped.apm.utils.Constaints;
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
import java.util.*;

/**
 * Created by mason on 8/12/15.
 */
@Service
public class ApplicationService {

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    ApplicationStatisticRepository applicationStatisticRepository;

    @Autowired
    InstanceRepository instanceRepository;

    @Autowired
    InstanceStatisticRepository instanceStatisticRepository;


//    public List<InstanceVo> findApplicationInstanceByApplication(String applicationName, boolean simplify) {
//        Assert.hasLength(applicationName, "ApplicationName cannot be empty when finding Instances.");
//        QAgentInfo qAgentInfo = QAgentInfo.agentInfo;
//        BooleanExpression appNameEqualExp = qAgentInfo.applicationName.eq(applicationName);
//        Iterable<AgentInfo> agentInfoIterable = agentInfoRepository.findAll(appNameEqualExp);
//        return StreamSupport.stream(agentInfoIterable.spliterator(), false).map(agentInfoBo -> {
//            InstanceVo instance = new InstanceVo();
//            instance.setName(agentInfoBo.getApplicationName());
//            instance.setInstanceId(buildInstanceId(agentInfoBo));
//            if (!simplify) {
//                buildMetricInfo(instance, agentInfoBo);
//            }
//            return instance;
//        }).collect(Collectors.toList());
//    }

//    private void buildMetricInfo(final InstanceVo instance, final AgentInfo agentInfoBo) {
//        Assert.notNull(instance, "InstanceVo cannot be null while building metric for.");
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

    public Page<Application> selectApplctions(String userId, String orderBy, int pageSize, int pageNumber) {
        QApplication application = QApplication.application;
        BooleanExpression userEq = application.userId.eq(Constaints.DEFAULT_USER);

        Sort sort = PageUtils.toSort(orderBy);
        Pageable pageable = new PageRequest(pageNumber, pageSize, sort);
        return applicationRepository.findAll(userEq, pageable);
    }

    public Iterable<ApplicationStatistic> selectApplicationStatistics(LocalDateTime from, LocalDateTime to,
                                                                      List<Application> apps) {

        QApplicationStatistic appStatistic = QApplicationStatistic.applicationStatistic;
        BooleanExpression timeGtFromExpr = appStatistic.timestamp.gt(from.atZone(ZoneId.systemDefault()).
                toInstant().toEpochMilli());
        BooleanExpression timeltToExpr = appStatistic.timestamp.lt(to.atZone(ZoneId.systemDefault()).
                toInstant().toEpochMilli());

        List<Long> appIds = new ArrayList<>();
        for (Application app : apps) {
            appIds.add(app.getId());
        }
        BooleanExpression appIdInExpr = appStatistic.appId.in(appIds);
        BooleanExpression periodEqExpr = appStatistic.period.eq(60);

        BooleanExpression condtions = timeGtFromExpr.and(timeltToExpr).and(appIdInExpr).and(periodEqExpr);

        return applicationStatisticRepository.findAll(condtions);

    }

    public QueryResponse<ApplicationVo> packageApplications(Page<Application> applications,
                                                            Iterable<ApplicationStatistic> applicationStatistics,
                                                            LocalDateTime from, LocalDateTime to) {
        com.baidu.oped.apm.mvc.vo.Page page = new com.baidu.oped.apm.mvc.vo.Page();
        page.setPageSize(applications.getSize());
        page.setPageNumber(applications.getNumber());
        page.setTotal(applications.getTotalElements());

        // appNames
        Map<Long, String> appNames = new HashMap<>();
        for (Application application : applications.getContent()) {
            appNames.put(application.getId(), application.getAppName());
        }

        // mins
        long period = (from.atZone(ZoneId.systemDefault()).
                toInstant().toEpochMilli() - to.atZone(ZoneId.systemDefault()).
                toInstant().toEpochMilli()) / 60;

        Map<Long, Long> pvMap = new HashMap<>();
        Map<Long, Long> errorMap = new HashMap<>();
        Map<Long, Double> rtMap = new HashMap<>();
        Iterator<ApplicationStatistic> statisticIterator = applicationStatistics.iterator();
        while (statisticIterator.hasNext()) {
            ApplicationStatistic statistic = statisticIterator.next();
            long appId = statistic.getAppId();
            long pv = statistic.getPv();
            Long error = statistic.getError();
            Double responseTime =  statistic.getResponseTime();

            if (!pvMap.containsKey(appId)) {
                pvMap.put(appId, 0l);
            }
            if (!errorMap.containsKey(appId)) {
                errorMap.put(appId, 0l);
            }
            if (!rtMap.containsKey(appId)) {
                rtMap.put(appId, .0D);
            }

            long pvValue = pvMap.get(appId) + pv;
            long errorValue = errorMap.get(appId) + error;
            double rtValue = rtMap.get(appId) + pv * responseTime;
            pvMap.put(appId, pvValue);
            errorMap.put(appId, errorValue);
            rtMap.put(appId, rtValue);
        }

        List<ApplicationVo> appVos = new ArrayList<>();
        for (Long appId : pvMap.keySet()) {
            appVos.add(new ApplicationVo(appId, appNames.get(appId),
                    rtMap.get(appId), pvMap.get(appId), errorMap.get(appId), period));
        }


        QueryResponse<ApplicationVo> ret = new QueryResponse<>(page, appVos);
        return ret;
    }

    public String getAppName(long appId) {

        Application app = applicationRepository.findOne(appId);
        if (app == null) {
            return null;
        }
        return app.getAppName();

    }

    public Page<Instance> selectInstances(long appId, String orderBy, int pageSize, int pageNumber) {

        QInstance instance = QInstance.instance;
        Sort sort = PageUtils.toSort(orderBy);
        Pageable pageable = new PageRequest(pageNumber, pageSize, sort);
        return instanceRepository.findAll(instance.appId.eq(appId), pageable);

    }

    public Iterable<InstanceStatistic> selectInstanceStatistics(LocalDateTime from, LocalDateTime to,
                                                                List<Instance> instances) {

        QInstanceStatistic instanceStatistic = QInstanceStatistic.instanceStatistic;
        BooleanExpression tsGtFromExpr = instanceStatistic.timestamp.gt(from.atZone(
                ZoneId.systemDefault()).toInstant().toEpochMilli());
        BooleanExpression tsLtToExpr = instanceStatistic.timestamp.lt(to.atZone(
                ZoneId.systemDefault()).toInstant().toEpochMilli());
        List<Long> instanceIds = new ArrayList<>();
        for (Instance instance : instances) {
            instanceIds.add(instance.getId());
        }
        BooleanExpression inExpr = instanceStatistic.instanceId.in(instanceIds);
        BooleanExpression conditions = tsGtFromExpr.and(tsLtToExpr).and(inExpr);

        return instanceStatisticRepository.findAll(conditions);

    }

    public List<InstanceVo> packageInstances(Page<Instance> instances, Iterable<InstanceStatistic> instanceStatistics,
                                             LocalDateTime from, LocalDateTime to) {

        com.baidu.oped.apm.mvc.vo.Page page = new com.baidu.oped.apm.mvc.vo.Page();
        page.setTotal(instances.getTotalElements());
        page.setPageNumber(instances.getNumber());
        page.setPageSize(instances.getSize());

        // mins
        long period = (from.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                - to.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()) / 60;

        // instanceNames
        Map<Long, String> instanceNames = new HashMap<>();
        for (Instance instance : instances.getContent()) {
            int port = instance.getPort();
            String host = instance.getHost();
            instanceNames.put(instance.getId(), String.format("%s:%d", host, port));
        }

        Map<Long, Long> pvMap = new HashMap<>();
        Map<Long, Long> errorMap = new HashMap<>();
        Map<Long, Long> apdexMap = new HashMap<>();
        Map<Long, Double> rtMap = new HashMap<>();
        Map<Long, Double> cpuMap = new HashMap<>();
        Map<Long, Double> memMap = new HashMap<>();
        Iterator<InstanceStatistic> statisticIterator = instanceStatistics.iterator();
        while (statisticIterator.hasNext()) {
            InstanceStatistic statistic = statisticIterator.next();
            long instanceId = statistic.getInstanceId();
            long pv = statistic.getPv();
            double cpuUsage = statistic.getCpuUsage();
            Double memoryUsage = statistic.getMemoryUsage();
            Long error = statistic.getError();
            Double responseTime =  statistic.getResponseTime();
            Long satisfied = statistic.getSatisfied();

            if (!pvMap.containsKey(instanceId)) {
                pvMap.put(instanceId, 0l);
            }
            if (!errorMap.containsKey(instanceId)) {
                errorMap.put(instanceId, 0l);
            }
            if (!apdexMap.containsKey(instanceId)) {
                apdexMap.put(instanceId, 0l);
            }
            if (!rtMap.containsKey(instanceId)) {
                rtMap.put(instanceId, .0D);
            }
            if (!cpuMap.containsKey(instanceId)) {
                cpuMap.put(instanceId, .0D);
            }
            if (!memMap.containsKey(instanceId)) {
                memMap.put(instanceId, .0D);
            }

            long pvValue = pvMap.get(instanceId) + pv;
            long errorValue = errorMap.get(instanceId) + error;
            long satisfiedValue = apdexMap.get(instanceId) + satisfied;
            double rtValue = rtMap.get(instanceId) + pv * responseTime;
            double cpuValue = cpuMap.get(instanceId) + cpuUsage;
            double memValue = memMap.get(instanceId) + memoryUsage;
            pvMap.put(instanceId, pvValue);
            errorMap.put(instanceId, errorValue);
            apdexMap.put(instanceId, satisfiedValue);
            rtMap.put(instanceId, rtValue);
            cpuMap.put(instanceId, cpuValue);
            memMap.put(instanceId, memValue);
        }

        List<InstanceVo> instanceVos = new ArrayList<>();
        for (long instanceId : pvMap.keySet()) {
            instanceVos.add(new InstanceVo(instanceId, instanceNames.get(instanceId),
                    pvMap.get(instanceId), apdexMap.get(instanceId), rtMap.get(instanceId),
                    errorMap.get(instanceId), period, cpuMap.get(instanceId) / cpuMap.size(),
                    memMap.get(instanceId) / memMap.size()));
        }

        return instanceVos;
    }
}
