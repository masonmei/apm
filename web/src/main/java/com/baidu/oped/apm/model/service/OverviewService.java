package com.baidu.oped.apm.model.service;

import static com.baidu.oped.apm.utils.TimeUtils.toMillisSecond;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.AbstractPersistable;
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
import com.baidu.oped.apm.common.jpa.repository.InstanceRepository;
import com.baidu.oped.apm.common.jpa.repository.InstanceStatisticRepository;
import com.baidu.oped.apm.mvc.vo.TimeRange;
import com.baidu.oped.apm.mvc.vo.TransactionVo;
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

        List<Long> instanceIds = StreamSupport.stream(instances.spliterator(), false).map(AbstractPersistable::getId)
                .collect(Collectors.toList());

        QInstanceStatistic qInstanceStatistic = QInstanceStatistic.instanceStatistic;
        BooleanExpression instanceIdCondition = qInstanceStatistic.instanceId.in(instanceIds);
        BooleanExpression timestampCondition = qInstanceStatistic.timestamp
                .between(toMillisSecond(timeRange.getFrom()), toMillisSecond(timeRange.getTo()));
        BooleanExpression whereCondition = instanceIdCondition.and(timestampCondition);
        return instanceStatisticRepository.findAll(whereCondition);
    }

    /**
     * Get top n transactions of the given app.
     *
     * @param appId     application id.
     * @param timeRange timeRange
     * @param limit     limit
     * @return top transactions of application
     */
    public List<TransactionVo> getTopNWebTransactionOfApp(Long appId, TimeRange timeRange, Integer limit) {
        final long period = 60L;

        Iterable<WebTransaction> webTransactionsForApp = automaticService.getWebTransactionsOfApp(appId);
        Iterable<WebTransactionStatistic> webTransactionStatistics =
                automaticService.getWebTransactionsStatistic(webTransactionsForApp, timeRange, period);
        return WebTransactionUtils
                .topByAvgResponseTime(webTransactionStatistics, webTransactionsForApp, timeRange, limit);
    }

    /**
     * Get the top n transaction of the given instance id.
     *
     * @param instanceId instance id
     * @param timeRange  timeRange
     * @param limit      limit
     * @return top transactions of instance
     */
    public List<TransactionVo> getTopNWebTransactionOfInstance(Long instanceId, TimeRange timeRange, Integer limit) {
        final long period = 60L;

        Iterable<WebTransaction> webTransactionsForApp = automaticService.getWebTransactionsOfInstance(instanceId);
        Iterable<WebTransactionStatistic> webTransactionStatistics =
                automaticService.getWebTransactionsStatistic(webTransactionsForApp, timeRange, period);
        return WebTransactionUtils
                .topByAvgResponseTime(webTransactionStatistics, webTransactionsForApp, timeRange, limit);
    }
}
