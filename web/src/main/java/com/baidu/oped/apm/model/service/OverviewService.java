package com.baidu.oped.apm.model.service;

import static com.baidu.oped.apm.utils.TimeUtils.toMillSecond;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.baidu.oped.apm.common.jpa.entity.ApplicationStatistic;
import com.baidu.oped.apm.common.jpa.entity.InstanceStatistic;
import com.baidu.oped.apm.common.jpa.entity.QApplicationStatistic;
import com.baidu.oped.apm.common.jpa.entity.QInstanceStatistic;
import com.baidu.oped.apm.common.jpa.entity.ServiceType;
import com.baidu.oped.apm.common.jpa.repository.ApplicationStatisticRepository;
import com.baidu.oped.apm.common.jpa.repository.InstanceStatisticRepository;
import com.baidu.oped.apm.mvc.vo.TimeRange;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by mason on 8/27/15.
 */
@Service
public class OverviewService {

    @Autowired
    private ApplicationStatisticRepository applicationStatisticRepository;

    @Autowired
    private InstanceStatisticRepository instanceStatisticRepository;

    /**
     * Return the given metrics of application level.
     *
     * @param timeRanges
     * @param period
     * @param serviceTypes
     *
     * @return
     */
    public Map<TimeRange, Iterable<ApplicationStatistic>> getApplicationMetricData(Long appId,
                                                                                   List<TimeRange> timeRanges,
                                                                                   Integer period,
                                                                                   ServiceType... serviceTypes) {
        Assert.notNull(appId);
        Assert.notEmpty(timeRanges);
        Assert.notNull(period);
        Assert.notEmpty(serviceTypes);
        QApplicationStatistic applicationStatistic = QApplicationStatistic.applicationStatistic;
        BooleanExpression appIdCondition = applicationStatistic.appId.eq(appId);
        BooleanExpression periodCondition = applicationStatistic.period.eq(period);
        BooleanExpression serviceTypeCondition = applicationStatistic.serviceType.in(serviceTypes);

        Map<TimeRange, Iterable<ApplicationStatistic>> result = new HashMap<>();
        timeRanges.stream().forEach(timeRange -> {
            BooleanExpression timestampCondition = applicationStatistic.timestamp
                                                           .between(toMillSecond(timeRange.getFrom()),
                                                                           toMillSecond(timeRange.getTo()));

            BooleanExpression whereCondition =
                    appIdCondition.and(periodCondition).and(serviceTypeCondition).and(timestampCondition);
            Iterable<ApplicationStatistic> currentRangeResult = applicationStatisticRepository.findAll(whereCondition);
            result.put(timeRange, currentRangeResult);
        });

        return result;
    }

    /**
     * Return the given metric of the given instance level.
     *
     * @param instanceId
     * @param timeRanges
     * @param period
     * @param serviceTypes
     *
     * @return
     */
    public Map<TimeRange, Iterable<InstanceStatistic>> getInstanceMetricData(Long instanceId,
                                                                             List<TimeRange> timeRanges, Integer period,
                                                                             ServiceType[] serviceTypes) {
        Assert.notNull(instanceId);
        Assert.notEmpty(timeRanges);
        Assert.notNull(period);
        Assert.notEmpty(serviceTypes);
        QInstanceStatistic instanceStatistic = QInstanceStatistic.instanceStatistic;
        BooleanExpression appIdCondition = instanceStatistic.instanceId.eq(instanceId);
        BooleanExpression periodCondition = instanceStatistic.period.eq(period);
        BooleanExpression serviceTypeCondition = instanceStatistic.serviceType.in(serviceTypes);

        Map<TimeRange, Iterable<InstanceStatistic>> result = new HashMap<>();
        timeRanges.stream().forEach(timeRange -> {
            BooleanExpression timestampCondition = instanceStatistic.timestamp
                                                           .between(toMillSecond(timeRange.getFrom()),
                                                                           toMillSecond(timeRange.getTo()));

            BooleanExpression whereCondition =
                    appIdCondition.and(periodCondition).and(serviceTypeCondition).and(timestampCondition);
            Iterable<InstanceStatistic> currentRangeResult = instanceStatisticRepository.findAll(whereCondition);
            result.put(timeRange, currentRangeResult);
        });

        return result;
    }
}
