package com.baidu.oped.apm.model.service;

import static com.baidu.oped.apm.utils.TimeUtils.toMillSecond;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.baidu.oped.apm.common.jpa.entity.Application;
import com.baidu.oped.apm.common.jpa.entity.ApplicationStatistic;
import com.baidu.oped.apm.common.jpa.entity.Instance;
import com.baidu.oped.apm.common.jpa.entity.InstanceStatistic;
import com.baidu.oped.apm.common.jpa.entity.QApplication;
import com.baidu.oped.apm.common.jpa.entity.QApplicationStatistic;
import com.baidu.oped.apm.common.jpa.entity.QInstance;
import com.baidu.oped.apm.common.jpa.entity.QInstanceStatistic;
import com.baidu.oped.apm.common.jpa.repository.ApplicationRepository;
import com.baidu.oped.apm.common.jpa.repository.ApplicationStatisticRepository;
import com.baidu.oped.apm.common.jpa.repository.InstanceRepository;
import com.baidu.oped.apm.common.jpa.repository.InstanceStatisticRepository;
import com.baidu.oped.apm.mvc.vo.TimeRange;
import com.baidu.oped.apm.utils.PageUtils;
import com.mysema.query.types.expr.BooleanExpression;

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

    public Page<Application> selectApplications(String userId, String orderBy, int pageSize, int pageNumber) {
        QApplication application = QApplication.application;
        BooleanExpression userEq = application.userId.eq(userId);

        Sort sort = PageUtils.toSort(orderBy);
        Pageable pageable = new PageRequest(pageNumber, pageSize, sort);
        return applicationRepository.findAll(userEq, pageable);
    }

    /**
     * @param timeRange
     * @param apps
     * @param period    in Second
     *
     * @return
     */
    public Iterable<ApplicationStatistic> selectApplicationStatistics(TimeRange timeRange, Iterable<Application> apps,
            Long period) {
        final long periodInMillis = period * 1000;
        List<Long> appIds =
                StreamSupport.stream(apps.spliterator(), false).map(Application::getId).collect(Collectors.toList());

        QApplicationStatistic appStatistic = QApplicationStatistic.applicationStatistic;
        BooleanExpression timestampCondition =
                appStatistic.timestamp.between(toMillSecond(timeRange.getFrom()), toMillSecond(timeRange.getTo()));
        BooleanExpression appIdCondition = appStatistic.appId.in(appIds);
        BooleanExpression periodCondition = appStatistic.period.eq(periodInMillis);

        BooleanExpression whereCondition = timestampCondition.and(appIdCondition).and(periodCondition);

        return applicationStatisticRepository.findAll(whereCondition);
    }

    public Page<Instance> selectInstances(long appId, String orderBy, int pageSize, int pageNumber) {
        QInstance instance = QInstance.instance;
        Sort sort = PageUtils.toSort(orderBy);
        Pageable pageable = new PageRequest(pageNumber, pageSize, sort);
        return instanceRepository.findAll(instance.appId.eq(appId), pageable);
    }

    /**
     * @param timeRange
     * @param instances
     * @param period    in Second
     *
     * @return
     */
    public Iterable<InstanceStatistic> selectInstanceStatistics(TimeRange timeRange, List<Instance> instances,
            Long period) {
        final long periodInMillis = period * 1000;

        List<Long> instanceIds =
                StreamSupport.stream(instances.spliterator(), false).map(Instance::getId).collect(Collectors.toList());

        QInstanceStatistic instanceStatistic = QInstanceStatistic.instanceStatistic;
        BooleanExpression timestampCondition =
                instanceStatistic.timestamp.between(toMillSecond(timeRange.getFrom()), toMillSecond(timeRange.getTo()));
        BooleanExpression instanceIdCondition = instanceStatistic.instanceId.in(instanceIds);
        BooleanExpression periodCondition = instanceStatistic.period.eq(periodInMillis);
        BooleanExpression whereCondition = instanceIdCondition.and(timestampCondition).and(periodCondition);

        return instanceStatisticRepository.findAll(whereCondition);

    }

}
