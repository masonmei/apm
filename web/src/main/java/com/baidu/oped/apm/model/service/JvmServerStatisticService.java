package com.baidu.oped.apm.model.service;

import static com.baidu.oped.apm.utils.TimeUtils.toMillisSecond;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.baidu.oped.apm.common.jpa.entity.ApplicationServerStatistic;
import com.baidu.oped.apm.common.jpa.entity.InstanceServerStatistic;
import com.baidu.oped.apm.common.jpa.entity.MetricData;
import com.baidu.oped.apm.common.jpa.entity.QApplicationServerStatistic;
import com.baidu.oped.apm.common.jpa.entity.QInstanceServerStatistic;
import com.baidu.oped.apm.common.jpa.repository.ApplicationServerStatisticRepository;
import com.baidu.oped.apm.common.jpa.repository.InstanceServerStatisticRepository;
import com.baidu.oped.apm.common.jpa.repository.MetricDataRepository;
import com.baidu.oped.apm.mvc.vo.TimeRange;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by mason on 9/29/15.
 */
@Service
public class JvmServerStatisticService {

    @Autowired
    private InstanceServerStatisticRepository instanceServerStatisticRepository;

    @Autowired
    private ApplicationServerStatisticRepository applicationServerStatisticRepository;

    @Autowired
    private MetricDataRepository metricDataRepository;

    /**
     * Get the MetricDatas with ids.
     *
     * @param ids the metric data's id
     *
     * @return the metric data
     */
    public Iterable<MetricData> getMetricData(Iterable<Long> ids) {
        Assert.notNull(ids, "MetricData ids must not be null.");
        return metricDataRepository.findAll(ids);
    }

    /**
     * Get the ApplicationServerStatistics' of the given application.
     *
     * @param appId     the application id
     * @param period    the period In Second
     * @param timeRange get the time range.
     *
     * @return the Application Server Statistics
     */
    public Iterable<ApplicationServerStatistic> getAppServerStatistics(Long appId, Long period, TimeRange timeRange) {
        Assert.notNull(appId);
        Assert.notNull(period);
        Assert.notNull(timeRange);

        final Long periodInMillis = period * 1000;

        QApplicationServerStatistic qApplicationServerStatistic =
                QApplicationServerStatistic.applicationServerStatistic;
        BooleanExpression applicationIdCondition = qApplicationServerStatistic.appId.eq(appId);
        BooleanExpression periodCondition = qApplicationServerStatistic.period.eq(periodInMillis);
        BooleanExpression timestampCondition = qApplicationServerStatistic.timestamp
                .between(toMillisSecond(timeRange.getFrom()), toMillisSecond(timeRange.getTo()));
        BooleanExpression whereCondition = applicationIdCondition.and(periodCondition).and(timestampCondition);

        return applicationServerStatisticRepository.findAll(whereCondition);
    }

    /**
     * Get the InstanceServerStatistics' of the given application.
     *
     * @param instanceId the instance id
     * @param period     the period In Second
     * @param timeRange  get the time range.
     *
     * @return the Instance Server Statistics
     */
    public Iterable<InstanceServerStatistic> getInstanceServerStatistics(Long instanceId, Long period,
            TimeRange timeRange) {
        Assert.notNull(instanceId);
        Assert.notNull(period);
        Assert.notNull(timeRange);

        final Long periodInMillis = period * 1000;

        QInstanceServerStatistic qInstanceServerStatistic = QInstanceServerStatistic.instanceServerStatistic;
        BooleanExpression instanceIdCondition = qInstanceServerStatistic.instanceId.eq(instanceId);
        BooleanExpression periodCondition = qInstanceServerStatistic.period.eq(periodInMillis);
        BooleanExpression timestampCondition = qInstanceServerStatistic.timestamp
                .between(toMillisSecond(timeRange.getFrom()), toMillisSecond(timeRange.getTo()));
        BooleanExpression whereCondition = instanceIdCondition.and(periodCondition).and(timestampCondition);

        return instanceServerStatisticRepository.findAll(whereCondition);
    }

}
