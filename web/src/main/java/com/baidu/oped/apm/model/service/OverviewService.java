package com.baidu.oped.apm.model.service;

import static com.baidu.oped.apm.utils.TimeUtils.toMillSecond;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.baidu.oped.apm.common.jpa.entity.Application;
import com.baidu.oped.apm.common.jpa.entity.Instance;
import com.baidu.oped.apm.common.jpa.entity.InstanceStatistic;
import com.baidu.oped.apm.common.jpa.entity.QInstance;
import com.baidu.oped.apm.common.jpa.entity.QInstanceStatistic;
import com.baidu.oped.apm.common.jpa.entity.WebTransaction;
import com.baidu.oped.apm.common.jpa.entity.WebTransactionStatistic;
import com.baidu.oped.apm.common.jpa.repository.ApplicationRepository;
import com.baidu.oped.apm.common.jpa.repository.ApplicationStatisticRepository;
import com.baidu.oped.apm.common.jpa.repository.InstanceRepository;
import com.baidu.oped.apm.common.jpa.repository.InstanceStatisticRepository;
import com.baidu.oped.apm.mvc.vo.TimeRange;
import com.baidu.oped.apm.mvc.vo.Transaction;
import com.baidu.oped.apm.utils.WebTransactionUtils;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by mason on 8/27/15.
 */
@Service
public class OverviewService {

    @Autowired
    private AutomaticService automaticService;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private InstanceRepository instanceRepository;

    @Autowired
    private InstanceStatisticRepository instanceStatisticRepository;

    /**
     * Get the instance statistic of the given instances
     *
     * @param instances instances
     * @param timeRange timeRange
     * @return
     */
    public Iterable<InstanceStatistic> getExistInstanceStatistics(Iterable<Instance> instances, TimeRange timeRange) {
        Assert.notNull(instances, "ApplicationId must not be null while retrieving instances of.");
        Assert.notNull(timeRange, "TimeRange must not be null while retrieving instances of.");

        List<Long> instanceIds = StreamSupport.stream(instances.spliterator(), false).map(instance -> instance.getId())
                                         .collect(Collectors.toList());

        QInstanceStatistic qInstanceStatistic = QInstanceStatistic.instanceStatistic;
        BooleanExpression instanceIdCondition = qInstanceStatistic.instanceId.in(instanceIds);
        BooleanExpression timestampCondition = qInstanceStatistic.timestamp.between(toMillSecond(timeRange.getFrom()),
                                                                                           toMillSecond(timeRange
                                                                                                                .getTo()));
        BooleanExpression whereCondition = instanceIdCondition.and(timestampCondition);
        return instanceStatisticRepository.findAll(whereCondition);
    }

    /**
     * Get the instance of the given app.
     *
     * @param appId the application database id
     * @return
     */
    public Iterable<Instance> getApplicationInstances(Long appId) {
        Assert.notNull(appId, "ApplicationId must not be null while retrieving instances of.");
        QInstance qInstance = QInstance.instance;
        BooleanExpression appIdCondition = qInstance.appId.eq(appId);
        Iterable<Instance> instances = instanceRepository.findAll(appIdCondition);
        return instances;
    }

    /**
     * Get Application with id
     *
     * @param appId app database id
     * @return
     */
    public Application getApplication(Long appId) {
        Assert.notNull(appId, "ApplicationId must not be null while retrieving instances of.");
        return applicationRepository.findOne(appId);
    }

    /**
     * Get top n transactions of the given app.
     *
     * @param appId app database id.
     * @param timeRange time range
     * @param limit limit
     * @return
     */
    public List<Transaction> getWebTransactionStatisticOfApp(Long appId, TimeRange timeRange, Integer limit){
        final long period = 60l;

        Iterable<WebTransaction> webTransactionsForApp = automaticService.findWebTransactionsWithAppId(appId);
        Iterable<WebTransactionStatistic> webTransactionStatistics =
                automaticService.getStatisticsOfWebTransactions(webTransactionsForApp, timeRange, period);
        return WebTransactionUtils.topByAvgResponseTime(webTransactionStatistics, webTransactionsForApp, timeRange,
                                                               limit);
    }

    /**
     * Get the top n transaction of the given instance id.
     *
     * @param instanceId
     * @param timeRange
     * @param limit
     * @return
     */
    public List<Transaction> getWebTransactionStatisticOfInstance(Long instanceId, TimeRange timeRange, Integer limit) {
        final long period = 60l;

        Iterable<WebTransaction> webTransactionsForApp = automaticService.findWebTransactionsWithInstanceId(instanceId);
        Iterable<WebTransactionStatistic> webTransactionStatistics =
                automaticService.getStatisticsOfWebTransactions(webTransactionsForApp, timeRange, period);
        return WebTransactionUtils.topByAvgResponseTime(webTransactionStatistics, webTransactionsForApp, timeRange,
                                                               limit);
    }
}
