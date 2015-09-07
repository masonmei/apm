package com.baidu.oped.apm.model.service;

import static com.baidu.oped.apm.utils.TimeUtils.toMillSecond;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.baidu.oped.apm.common.jpa.entity.ExternalService;
import com.baidu.oped.apm.common.jpa.entity.ExternalServiceStatistic;
import com.baidu.oped.apm.common.jpa.entity.QExternalService;
import com.baidu.oped.apm.common.jpa.entity.QExternalServiceStatistic;
import com.baidu.oped.apm.common.jpa.entity.QSqlTransaction;
import com.baidu.oped.apm.common.jpa.entity.QSqlTransactionStatistic;
import com.baidu.oped.apm.common.jpa.entity.QWebTransaction;
import com.baidu.oped.apm.common.jpa.entity.QWebTransactionStatistic;
import com.baidu.oped.apm.common.jpa.entity.ServiceType;
import com.baidu.oped.apm.common.jpa.entity.SqlTransaction;
import com.baidu.oped.apm.common.jpa.entity.SqlTransactionStatistic;
import com.baidu.oped.apm.common.jpa.entity.WebTransaction;
import com.baidu.oped.apm.common.jpa.entity.WebTransactionStatistic;
import com.baidu.oped.apm.common.jpa.repository.ExternalTransactionRepository;
import com.baidu.oped.apm.common.jpa.repository.ExternalTransactionStatisticRepository;
import com.baidu.oped.apm.common.jpa.repository.SqlTransactionRepository;
import com.baidu.oped.apm.common.jpa.repository.SqlTransactionStatisticRepository;
import com.baidu.oped.apm.common.jpa.repository.WebTransactionRepository;
import com.baidu.oped.apm.common.jpa.repository.WebTransactionStatisticRepository;
import com.baidu.oped.apm.mvc.vo.TimeRange;
import com.baidu.oped.apm.mvc.vo.TrendContext;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by mason on 9/6/15.
 */
@Service
public class AutomaticService {

    @Autowired
    private WebTransactionRepository webTransactionRepository;

    @Autowired
    private WebTransactionStatisticRepository webTransactionStatisticRepository;

    @Autowired
    private SqlTransactionRepository sqlTransactionRepository;

    @Autowired
    private SqlTransactionStatisticRepository sqlTransactionStatisticRepository;

    @Autowired
    private ExternalTransactionRepository externalTransactionRepository;

    @Autowired
    private ExternalTransactionStatisticRepository externalTransactionStatisticRepository;

    public TrendContext getMetricDataOfApp(Long appId, List<TimeRange> timeRanges, Long period,
                                           ServiceType... serviceTypes) {
        TrendContext trendContext = new TrendContext(timeRanges.toArray(new TimeRange[timeRanges.size()]));
        Arrays.stream(serviceTypes).forEach(serviceType -> {
            switch (serviceType) {
                case WEB:
                    Map<TimeRange, Iterable<WebTransactionStatistic>> webTransactionMetricDataOfApp =
                            getWebTransactionMetricDataOfApp(appId, timeRanges, period);
                    trendContext.addServiceData(serviceType, webTransactionMetricDataOfApp);
                    break;
                case DB:
                    Map<TimeRange, Iterable<SqlTransactionStatistic>> sqlTransactionMetricDataOfApp =
                            getSqlTransactionMetricDataOfApp(appId, timeRanges, period);
                    trendContext.addServiceData(serviceType, sqlTransactionMetricDataOfApp);
                    break;
                case EXTERNAL:
                    Map<TimeRange, Iterable<ExternalServiceStatistic>> externalServiceMetricDataOfApp =
                            getExternalServiceMetricDataOfApp(appId, timeRanges, period);
                    trendContext.addServiceData(serviceType, externalServiceMetricDataOfApp);
                    break;
                case CACHE:
                case BACKGROUD:
                default:
                    throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        return trendContext;
    }

    public TrendContext getMetricDataOfInstance(Long instanceId, List<TimeRange> timeRanges, Long period,
                                                ServiceType... serviceTypes) {
        TrendContext trendContext = new TrendContext(timeRanges.toArray(new TimeRange[timeRanges.size()]));
        Arrays.stream(serviceTypes).forEach(serviceType -> {
            switch (serviceType) {
                case WEB:
                    Map<TimeRange, Iterable<WebTransactionStatistic>> webTransactionMetricDataOfApp =
                            getWebTransactionMetricDataOfInstance(instanceId, timeRanges, period);
                    trendContext.addServiceData(serviceType, webTransactionMetricDataOfApp);
                    break;
                case DB:
                    Map<TimeRange, Iterable<SqlTransactionStatistic>> sqlTransactionMetricDataOfApp =
                            getSqlTransactionMetricDataOfInstance(instanceId, timeRanges, period);
                    trendContext.addServiceData(serviceType, sqlTransactionMetricDataOfApp);
                    break;
                case EXTERNAL:
                    Map<TimeRange, Iterable<ExternalServiceStatistic>> externalServiceMetricDataOfApp =
                            getExternalServiceMetricDataOfInstance(instanceId, timeRanges, period);
                    trendContext.addServiceData(serviceType, externalServiceMetricDataOfApp);
                    break;
                case CACHE:
                case BACKGROUD:
                default:
                    throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        return trendContext;
    }

    /**
     * Return the given WebTransactionStatistic of the given app.
     *
     * @param appId
     * @param timeRanges
     * @param period
     *
     * @return
     */
    public Map<TimeRange, Iterable<WebTransactionStatistic>> getWebTransactionMetricDataOfApp(Long appId,
                                                                                              List<TimeRange>
                                                                                                      timeRanges,
                                                                                              Long period) {
        Assert.notNull(appId);
        Assert.notEmpty(timeRanges);
        Assert.notNull(period);

        Iterable<WebTransaction> webTransactions = findWebTransactionsWithAppId(appId);
        List<Long> webTransactionIds =
                StreamSupport.stream(webTransactions.spliterator(), false).map(WebTransaction::getId)
                        .collect(Collectors.toList());

        QWebTransactionStatistic qWebTransactionStatistic = QWebTransactionStatistic.webTransactionStatistic;
        BooleanExpression appIdCondition = qWebTransactionStatistic.transactionId.in(webTransactionIds);
        BooleanExpression periodCondition = qWebTransactionStatistic.period.eq(period);

        Map<TimeRange, Iterable<WebTransactionStatistic>> result = new HashMap<>();
        timeRanges.stream().forEach(timeRange -> {
            BooleanExpression timestampCondition = qWebTransactionStatistic.timestamp
                                                           .between(toMillSecond(timeRange.getFrom()),
                                                                           toMillSecond(timeRange.getTo()));

            BooleanExpression whereCondition = appIdCondition.and(periodCondition).and(timestampCondition);
            Iterable<WebTransactionStatistic> currentRangeResult =
                    webTransactionStatisticRepository.findAll(whereCondition);
            result.put(timeRange, currentRangeResult);
        });

        return result;
    }

    /**
     * Return the given WebTransactionStatistic of the given instance.
     *
     * @param instanceId
     * @param timeRanges
     * @param period
     *
     * @return
     */
    public Map<TimeRange, Iterable<WebTransactionStatistic>> getWebTransactionMetricDataOfInstance(Long instanceId,
                                                                                                   List<TimeRange>
                                                                                                           timeRanges,
                                                                                                   Long period) {
        Assert.notNull(instanceId);
        Assert.notEmpty(timeRanges);
        Assert.notNull(period);

        Iterable<WebTransaction> webTransactions = findWebTransactionsWithInstanceId(instanceId);
        List<Long> webTransactionIds =
                StreamSupport.stream(webTransactions.spliterator(), false).map(WebTransaction::getId)
                        .collect(Collectors.toList());

        QWebTransactionStatistic qWebTransactionStatistic = QWebTransactionStatistic.webTransactionStatistic;
        BooleanExpression appIdCondition = qWebTransactionStatistic.transactionId.in(webTransactionIds);
        BooleanExpression periodCondition = qWebTransactionStatistic.period.eq(period);

        Map<TimeRange, Iterable<WebTransactionStatistic>> result = new HashMap<>();
        timeRanges.stream().forEach(timeRange -> {
            BooleanExpression timestampCondition = qWebTransactionStatistic.timestamp
                                                           .between(toMillSecond(timeRange.getFrom()),
                                                                           toMillSecond(timeRange.getTo()));

            BooleanExpression whereCondition = appIdCondition.and(periodCondition).and(timestampCondition);
            Iterable<WebTransactionStatistic> currentRangeResult =
                    webTransactionStatisticRepository.findAll(whereCondition);
            result.put(timeRange, currentRangeResult);
        });
        return result;
    }

    /**
     * Return the given SqlTransactionStatistic of the given app.
     *
     * @param appId
     * @param timeRanges
     * @param period
     *
     * @return
     */
    public Map<TimeRange, Iterable<SqlTransactionStatistic>> getSqlTransactionMetricDataOfApp(Long appId,
                                                                                              List<TimeRange>
                                                                                                      timeRanges,
                                                                                              Long period) {
        Assert.notNull(appId);
        Assert.notEmpty(timeRanges);
        Assert.notNull(period);

        Iterable<SqlTransaction> sqlTransactions = findSqlTransactionsWithAppId(appId);
        List<Long> sqlTransactionIds =
                StreamSupport.stream(sqlTransactions.spliterator(), false).map(SqlTransaction::getId)
                        .collect(Collectors.toList());

        QSqlTransactionStatistic qSqlTransactionStatistic = QSqlTransactionStatistic.sqlTransactionStatistic;
        BooleanExpression appIdCondition = qSqlTransactionStatistic.sqlTransactionId.in(sqlTransactionIds);
        BooleanExpression periodCondition = qSqlTransactionStatistic.period.eq(period);

        Map<TimeRange, Iterable<SqlTransactionStatistic>> result = new HashMap<>();
        timeRanges.stream().forEach(timeRange -> {
            BooleanExpression timestampCondition = qSqlTransactionStatistic.timestamp
                                                           .between(toMillSecond(timeRange.getFrom()),
                                                                           toMillSecond(timeRange.getTo()));

            BooleanExpression whereCondition = appIdCondition.and(periodCondition).and(timestampCondition);
            Iterable<SqlTransactionStatistic> currentRangeResult =
                    sqlTransactionStatisticRepository.findAll(whereCondition);
            result.put(timeRange, currentRangeResult);
        });

        return result;
    }

    /**
     * Return the given SqlTransactionStatistic of the given instance.
     *
     * @param instanceId
     * @param timeRanges
     * @param period
     *
     * @return
     */
    public Map<TimeRange, Iterable<SqlTransactionStatistic>> getSqlTransactionMetricDataOfInstance(Long instanceId,
                                                                                                   List<TimeRange>
                                                                                                           timeRanges,
                                                                                                   Long period) {
        Assert.notNull(instanceId);
        Assert.notEmpty(timeRanges);
        Assert.notNull(period);

        Iterable<SqlTransaction> webTransactions = findSqlTransactionsWithInstanceId(instanceId);
        List<Long> sqlTransactionIds =
                StreamSupport.stream(webTransactions.spliterator(), false).map(SqlTransaction::getId)
                        .collect(Collectors.toList());

        QSqlTransactionStatistic qSqlTransactionStatistic = QSqlTransactionStatistic.sqlTransactionStatistic;
        BooleanExpression appIdCondition = qSqlTransactionStatistic.sqlTransactionId.in(sqlTransactionIds);
        BooleanExpression periodCondition = qSqlTransactionStatistic.period.eq(period);

        Map<TimeRange, Iterable<SqlTransactionStatistic>> result = new HashMap<>();
        timeRanges.stream().forEach(timeRange -> {
            BooleanExpression timestampCondition = qSqlTransactionStatistic.timestamp
                                                           .between(toMillSecond(timeRange.getFrom()),
                                                                           toMillSecond(timeRange.getTo()));

            BooleanExpression whereCondition = appIdCondition.and(periodCondition).and(timestampCondition);
            Iterable<SqlTransactionStatistic> currentRangeResult =
                    sqlTransactionStatisticRepository.findAll(whereCondition);
            result.put(timeRange, currentRangeResult);
        });
        return result;
    }

    /**
     * Return the given ExternalServiceStatistic of the given app.
     *
     * @param appId
     * @param timeRanges
     * @param period
     *
     * @return
     */
    public Map<TimeRange, Iterable<ExternalServiceStatistic>> getExternalServiceMetricDataOfApp(Long appId,
                                                                                                List<TimeRange>
                                                                                                        timeRanges,
                                                                                                Long period) {
        Assert.notNull(appId);
        Assert.notEmpty(timeRanges);
        Assert.notNull(period);

        Iterable<ExternalService> externalServices = findExternalServicesWithAppId(appId);
        List<Long> externalServiceIds =
                StreamSupport.stream(externalServices.spliterator(), false).map(ExternalService::getId)
                        .collect(Collectors.toList());

        QExternalServiceStatistic qExternalServiceStatistic = QExternalServiceStatistic.externalServiceStatistic;
        BooleanExpression serviceIdsCondition = qExternalServiceStatistic.externalServiceId.in(externalServiceIds);
        BooleanExpression periodCondition = qExternalServiceStatistic.period.eq(period);

        Map<TimeRange, Iterable<ExternalServiceStatistic>> result = new HashMap<>();
        timeRanges.stream().forEach(timeRange -> {
            BooleanExpression timestampCondition = qExternalServiceStatistic.timestamp
                                                           .between(toMillSecond(timeRange.getFrom()),
                                                                           toMillSecond(timeRange.getTo()));

            BooleanExpression whereCondition = serviceIdsCondition.and(periodCondition).and(timestampCondition);
            Iterable<ExternalServiceStatistic> currentRangeResult =
                    externalTransactionStatisticRepository.findAll(whereCondition);
            result.put(timeRange, currentRangeResult);
        });

        return result;
    }

    /**
     * Return the given ExternalServiceStatistic of the given instance.
     *
     * @param instanceId
     * @param timeRanges
     * @param period
     *
     * @return
     */
    public Map<TimeRange, Iterable<ExternalServiceStatistic>> getExternalServiceMetricDataOfInstance(Long instanceId,
                                                                                                     List<TimeRange>
                                                                                                             timeRanges,
                                                                                                     Long period) {
        Assert.notNull(instanceId);
        Assert.notEmpty(timeRanges);
        Assert.notNull(period);

        Iterable<ExternalService> externalServices = findExternalServicesWithInstanceId(instanceId);
        List<Long> externalServiceIds =
                StreamSupport.stream(externalServices.spliterator(), false).map(ExternalService::getId)
                        .collect(Collectors.toList());

        QExternalServiceStatistic qExternalServiceStatistic = QExternalServiceStatistic.externalServiceStatistic;
        BooleanExpression externalServiceIdsCondition =
                qExternalServiceStatistic.externalServiceId.in(externalServiceIds);
        BooleanExpression periodCondition = qExternalServiceStatistic.period.eq(period);

        Map<TimeRange, Iterable<ExternalServiceStatistic>> result = new HashMap<>();
        timeRanges.stream().forEach(timeRange -> {
            BooleanExpression timestampCondition = qExternalServiceStatistic.timestamp
                                                           .between(toMillSecond(timeRange.getFrom()),
                                                                           toMillSecond(timeRange.getTo()));

            BooleanExpression whereCondition = externalServiceIdsCondition.and(periodCondition).and(timestampCondition);
            Iterable<ExternalServiceStatistic> currentRangeResult =
                    externalTransactionStatisticRepository.findAll(whereCondition);
            result.put(timeRange, currentRangeResult);
        });
        return result;
    }

    public Iterable<ExternalService> findExternalServicesWithAppId(Long appId) {
        Assert.notNull(appId, "cannot find ExternalServices with empty appId");
        QExternalService qExternalService = QExternalService.externalService;
        BooleanExpression appIdCondition = qExternalService.appId.eq(appId);
        return externalTransactionRepository.findAll(appIdCondition);
    }

    public Iterable<ExternalService> findExternalServicesWithInstanceId(Long instanceId) {
        Assert.notNull(instanceId, "cannot find ExternalServices with empty appId");
        QExternalService qExternalService = QExternalService.externalService;
        BooleanExpression instanceIdCondition = qExternalService.instanceId.eq(instanceId);
        return externalTransactionRepository.findAll(instanceIdCondition);
    }

    public Iterable<SqlTransaction> findSqlTransactionsWithAppId(Long appId) {
        Assert.notNull(appId, "cannot find SqlTransactions with empty appId");
        QSqlTransaction qSqlTransaction = QSqlTransaction.sqlTransaction;
        BooleanExpression appIdCondition = qSqlTransaction.appId.eq(appId);
        return sqlTransactionRepository.findAll(appIdCondition);
    }

    public Iterable<SqlTransaction> findSqlTransactionsWithInstanceId(Long instanceId) {
        Assert.notNull(instanceId, "cannot find SqlTransactions with empty appId");
        QSqlTransaction qSqlTransaction = QSqlTransaction.sqlTransaction;
        BooleanExpression instanceIdCondition = qSqlTransaction.instanceId.eq(instanceId);
        return sqlTransactionRepository.findAll(instanceIdCondition);
    }

    public Iterable<WebTransaction> findWebTransactionsWithAppId(Long appId) {
        Assert.notNull(appId, "cannot find WebTransactions with empty appId");
        QWebTransaction qWebTransaction = QWebTransaction.webTransaction;
        BooleanExpression appIdCondition = qWebTransaction.appId.eq(appId);
        return webTransactionRepository.findAll(appIdCondition);
    }

    public Iterable<WebTransaction> findWebTransactionsWithInstanceId(Long instanceId) {
        Assert.notNull(instanceId, "cannot find WebTransactions with empty appId");
        QWebTransaction qWebTransaction = QWebTransaction.webTransaction;
        BooleanExpression instanceIdCondition = qWebTransaction.instanceId.eq(instanceId);
        return webTransactionRepository.findAll(instanceIdCondition);
    }

}
