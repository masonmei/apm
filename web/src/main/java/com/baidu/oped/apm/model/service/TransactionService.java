package com.baidu.oped.apm.model.service;

import static com.baidu.oped.apm.utils.TimeUtils.toMillSecond;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.baidu.oped.apm.common.jpa.entity.QTransaction;
import com.baidu.oped.apm.common.jpa.entity.QTransactionStatistic;
import com.baidu.oped.apm.common.jpa.entity.WebTransaction;
import com.baidu.oped.apm.common.jpa.entity.WebTransactionStatistic;
import com.baidu.oped.apm.common.jpa.repository.WebTransactionRepository;
import com.baidu.oped.apm.common.jpa.repository.WebTransactionStatisticRepository;
import com.baidu.oped.apm.mvc.vo.TimeRange;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by mason on 8/13/15.
 */
@Service
public class TransactionService {
    @Autowired
    private WebTransactionRepository webTransactionRepository;

    @Autowired
    private WebTransactionStatisticRepository webTransactionStatisticRepository;

    public Map<TimeRange, Iterable<WebTransactionStatistic>> getApplicationLevelTransactionMetricData(
            Long appId, List<TimeRange> timeRanges, Integer period) {
        Assert.notNull(appId);
        Assert.notNull(period);

        Iterable<WebTransaction> transactions = findTransactionWithAppId(appId);
        List<Long> transactionIds = StreamSupport.stream(transactions.spliterator(), false)
                        .map(AbstractPersistable::getId)
                        .collect(Collectors.toList());

        QTransactionStatistic applicationStatistic = QTransactionStatistic.transactionStatistic;
        BooleanExpression transactionIdCondition = applicationStatistic.transactionId.in(transactionIds);
        BooleanExpression periodCondition = applicationStatistic.period.eq(period);

        Map<TimeRange, Iterable<WebTransactionStatistic>> result = new HashMap<>();
        timeRanges.stream().forEach(timeRange -> {
            BooleanExpression timestampCondition = applicationStatistic.timestamp
                                                           .between(toMillSecond(timeRange.getFrom()),
                                                                           toMillSecond(timeRange.getTo()));

            BooleanExpression whereCondition =
                    transactionIdCondition.and(periodCondition).and(timestampCondition);
            Iterable<WebTransactionStatistic> currentRangeResult = webTransactionStatisticRepository.findAll(whereCondition);
            result.put(timeRange, currentRangeResult);
        });


        return result;
    }

    public Map<TimeRange, Iterable<WebTransactionStatistic>> getInstanceLevelTransactionMetricData(
            Long instanceId, List<TimeRange> timeRanges, Integer period) {

        Assert.notNull(instanceId);
        Assert.notNull(period);

        Iterable<WebTransaction> transactions = findTransactionWithInstanceId(instanceId);
        List<Long> transactionIds = StreamSupport.stream(transactions.spliterator(), false)
                                            .map(AbstractPersistable::getId)
                                            .collect(Collectors.toList());

        QTransactionStatistic applicationStatistic = QTransactionStatistic.transactionStatistic;
        BooleanExpression transactionIdCondition = applicationStatistic.transactionId.in(transactionIds);
        BooleanExpression periodCondition = applicationStatistic.period.eq(period);

        Map<TimeRange, Iterable<WebTransactionStatistic>> result = new HashMap<>();
        timeRanges.stream().forEach(timeRange -> {
            BooleanExpression timestampCondition = applicationStatistic.timestamp
                                                           .between(toMillSecond(timeRange.getFrom()),
                                                                           toMillSecond(timeRange.getTo()));

            BooleanExpression whereCondition =
                    transactionIdCondition.and(periodCondition).and(timestampCondition);
            Iterable<WebTransactionStatistic> currentRangeResult = webTransactionStatisticRepository.findAll(whereCondition);
            result.put(timeRange, currentRangeResult);
        });


        return result;
    }

    public Iterable<WebTransaction> findTransactionWithAppId(Long appId){
        Assert.notNull(appId, "ApplicationId must not be null while retrieving transactions with appId");
        QTransaction qTransaction = QTransaction.transaction;
        BooleanExpression appIdCondition = qTransaction.appId.eq(appId);
        return webTransactionRepository.findAll(appIdCondition);
    }

    public Iterable<WebTransaction> findTransactionWithInstanceId(Long instanceId){
        Assert.notNull(instanceId, "ApplicationId must not be null while retrieving transactions with appId");
        QTransaction qTransaction = QTransaction.transaction;
        BooleanExpression instanceIdCondition = qTransaction.instanceId.eq(instanceId);
        return webTransactionRepository.findAll(instanceIdCondition);
    }
}
