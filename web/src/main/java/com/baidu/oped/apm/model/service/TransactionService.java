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

import com.baidu.oped.apm.common.jpa.entity.ApplicationStatistic;
import com.baidu.oped.apm.common.jpa.entity.InstanceStatistic;
import com.baidu.oped.apm.common.jpa.entity.QTransaction;
import com.baidu.oped.apm.common.jpa.entity.QTransactionStatistic;
import com.baidu.oped.apm.common.jpa.entity.ServiceType;
import com.baidu.oped.apm.common.jpa.entity.Transaction;
import com.baidu.oped.apm.common.jpa.entity.TransactionStatistic;
import com.baidu.oped.apm.common.jpa.repository.TransactionRepository;
import com.baidu.oped.apm.common.jpa.repository.TransactionStatisticRepository;
import com.baidu.oped.apm.mvc.vo.TimeRange;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by mason on 8/13/15.
 */
@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionStatisticRepository transactionStatisticRepository;

    public Map<TimeRange, Iterable<TransactionStatistic>> getApplicationLevelTransactionMetricData(
            Long appId, List<TimeRange> timeRanges, Integer period) {
        Assert.notNull(appId);
        Assert.notNull(period);

        Iterable<Transaction> transactions = findTransactionWithAppId(appId);
        List<Long> transactionIds = StreamSupport.stream(transactions.spliterator(), false)
                        .map(AbstractPersistable::getId)
                        .collect(Collectors.toList());

        QTransactionStatistic applicationStatistic = QTransactionStatistic.transactionStatistic;
        BooleanExpression transactionIdCondition = applicationStatistic.transactionId.in(transactionIds);
        BooleanExpression periodCondition = applicationStatistic.period.eq(period);

        Map<TimeRange, Iterable<TransactionStatistic>> result = new HashMap<>();
        timeRanges.stream().forEach(timeRange -> {
            BooleanExpression timestampCondition = applicationStatistic.timestamp
                                                           .between(toMillSecond(timeRange.getFrom()),
                                                                           toMillSecond(timeRange.getTo()));

            BooleanExpression whereCondition =
                    transactionIdCondition.and(periodCondition).and(timestampCondition);
            Iterable<TransactionStatistic> currentRangeResult = transactionStatisticRepository.findAll(whereCondition);
            result.put(timeRange, currentRangeResult);
        });


        return result;
    }

    public Map<TimeRange, Iterable<TransactionStatistic>> getInstanceLevelTransactionMetricData(
            Long instanceId, List<TimeRange> timeRanges, Integer period) {

        Assert.notNull(instanceId);
        Assert.notNull(period);

        Iterable<Transaction> transactions = findTransactionWithInstanceId(instanceId);
        List<Long> transactionIds = StreamSupport.stream(transactions.spliterator(), false)
                                            .map(AbstractPersistable::getId)
                                            .collect(Collectors.toList());

        QTransactionStatistic applicationStatistic = QTransactionStatistic.transactionStatistic;
        BooleanExpression transactionIdCondition = applicationStatistic.transactionId.in(transactionIds);
        BooleanExpression periodCondition = applicationStatistic.period.eq(period);

        Map<TimeRange, Iterable<TransactionStatistic>> result = new HashMap<>();
        timeRanges.stream().forEach(timeRange -> {
            BooleanExpression timestampCondition = applicationStatistic.timestamp
                                                           .between(toMillSecond(timeRange.getFrom()),
                                                                           toMillSecond(timeRange.getTo()));

            BooleanExpression whereCondition =
                    transactionIdCondition.and(periodCondition).and(timestampCondition);
            Iterable<TransactionStatistic> currentRangeResult = transactionStatisticRepository.findAll(whereCondition);
            result.put(timeRange, currentRangeResult);
        });


        return result;
    }

    public Iterable<Transaction> findTransactionWithAppId(Long appId){
        Assert.notNull(appId, "ApplicationId must not be null while retrieving transactions with appId");
        QTransaction qTransaction = QTransaction.transaction;
        BooleanExpression appIdCondition = qTransaction.appId.eq(appId);
        return transactionRepository.findAll(appIdCondition);
    }

    public Iterable<Transaction> findTransactionWithInstanceId(Long instanceId){
        Assert.notNull(instanceId, "ApplicationId must not be null while retrieving transactions with appId");
        QTransaction qTransaction = QTransaction.transaction;
        BooleanExpression instanceIdCondition = qTransaction.instanceId.eq(instanceId);
        return transactionRepository.findAll(instanceIdCondition);
    }
}
