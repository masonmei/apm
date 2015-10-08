package com.baidu.oped.apm.model.service;

import static com.baidu.oped.apm.utils.TimeUtils.toMillisSecond;

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

    /**
     * Get the metric data of given app.
     *
     * @param appId        the application id
     * @param timeRanges   a set of from ~ to
     * @param period       in Second
     * @param serviceTypes the service types
     *
     * @return
     */
    public TrendContext getMetricDataOfApp(Long appId, List<TimeRange> timeRanges, Long period,
            ServiceType... serviceTypes) {
        TrendContext<ServiceType> trendContext =
                new TrendContext<>(period * 1000, timeRanges.toArray(new TimeRange[timeRanges.size()]));
        Arrays.stream(serviceTypes).forEach(serviceType -> {
            switch (serviceType) {
                case WEB:
                    Map<TimeRange, Iterable<WebTransactionStatistic>> webTransactionMetricDataOfApp =
                            getWebTransactionMetricDataOfApp(appId, timeRanges, period);
                    trendContext.addWebTransactionData(serviceType, webTransactionMetricDataOfApp);
                    break;
                case DB:
                    Map<TimeRange, Iterable<SqlTransactionStatistic>> sqlTransactionMetricDataOfApp =
                            getSqlTransactionMetricDataOfApp(appId, timeRanges, period);
                    trendContext.addDatabaseServiceData(serviceType, sqlTransactionMetricDataOfApp);
                    break;
                case EXTERNAL:
                    Map<TimeRange, Iterable<ExternalServiceStatistic>> externalServiceMetricDataOfApp =
                            getExternalServiceMetricDataOfApp(appId, timeRanges, period);
                    trendContext.addExternalServiceData(serviceType, externalServiceMetricDataOfApp);
                    break;
                case CACHE:
                case BACKGROUND:
                default:
                    throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        return trendContext;
    }

    /**
     * Get the metric data of given instance.
     *
     * @param instanceId   the instance id
     * @param timeRanges   a set of from ~ to
     * @param period       in Second
     * @param serviceTypes the service types
     *
     * @return
     */
    public TrendContext<ServiceType> getMetricDataOfInstance(Long instanceId, List<TimeRange> timeRanges, Long period,
            ServiceType... serviceTypes) {
        TrendContext<ServiceType> trendContext =
                new TrendContext<>(period * 1000, timeRanges.toArray(new TimeRange[timeRanges.size()]));
        Arrays.stream(serviceTypes).forEach(serviceType -> {
            switch (serviceType) {
                case WEB:
                    Map<TimeRange, Iterable<WebTransactionStatistic>> webTransactionMetricDataOfApp =
                            getWebTransactionStatisticOfInstance(instanceId, timeRanges, period);
                    trendContext.addWebTransactionData(serviceType, webTransactionMetricDataOfApp);
                    break;
                case DB:
                    Map<TimeRange, Iterable<SqlTransactionStatistic>> sqlTransactionMetricDataOfApp =
                            getSqlTransactionStatisticOfInstance(instanceId, timeRanges, period);
                    trendContext.addDatabaseServiceData(serviceType, sqlTransactionMetricDataOfApp);
                    break;
                case EXTERNAL:
                    Map<TimeRange, Iterable<ExternalServiceStatistic>> externalServiceMetricDataOfApp =
                            getExternalServiceStatisticOfInstance(instanceId, timeRanges, period);
                    trendContext.addExternalServiceData(serviceType, externalServiceMetricDataOfApp);
                    break;
                case CACHE:
                case BACKGROUND:
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
     * @param period     in Second
     *
     * @return
     */
    public Map<TimeRange, Iterable<WebTransactionStatistic>> getWebTransactionMetricDataOfApp(Long appId,
            List<TimeRange> timeRanges, Long period) {
        Assert.notNull(appId);
        Assert.notEmpty(timeRanges);
        Assert.notNull(period);

        Iterable<WebTransaction> webTransactions = getWebTransactionsWithAppId(appId);

        Map<TimeRange, Iterable<WebTransactionStatistic>> result = new HashMap<>();
        timeRanges.stream().forEach(timeRange -> {
            Iterable<WebTransactionStatistic> currentRangeResult =
                    getWebTransactionsStatistic(webTransactions, timeRange, period);
            result.put(timeRange, currentRangeResult);
        });

        return result;
    }

    /**
     * Get WebTransactions' statistic data of the given timeRanges.
     *
     * @param transactions webTransactions
     * @param timeRanges   timeRanges
     * @param period       periodInSecond
     *
     * @return
     */
    public Map<TimeRange, Iterable<WebTransactionStatistic>> getWebTransactionsStatistic(
            Iterable<WebTransaction> transactions, List<TimeRange> timeRanges, Long period) {
        Assert.notNull(transactions, "Transactions must not be null while getting web transaction statistics.");
        Assert.notNull(timeRanges, "TimeRanges must not be null while getting transaction statistics.");
        Assert.notNull(period, "Period must be null while getting transaction statistics.");
        Assert.state(period % 60 == 0, "Period must be 60 or the times of 60.");

        Map<TimeRange, Iterable<WebTransactionStatistic>> result = new HashMap<>();
        timeRanges.stream().forEach(timeRange -> {
            Iterable<WebTransactionStatistic> currentRangeResult =
                    getWebTransactionsStatistic(transactions, timeRange, period);
            result.put(timeRange, currentRangeResult);
        });

        return result;
    }

    /**
     * Get WebTransactions' statistic of the given timeRange
     *
     * @param webTransactions webTransactions
     * @param timeRange       timeRange
     * @param period          periodInSecond
     *
     * @return
     */
    public Iterable<WebTransactionStatistic> getWebTransactionsStatistic(Iterable<WebTransaction> webTransactions,
            TimeRange timeRange, Long period) {
        final Long periodInMillis = period * 1000;

        List<Long> webTransactionIds =
                StreamSupport.stream(webTransactions.spliterator(), false).map(WebTransaction::getId)
                        .collect(Collectors.toList());

        QWebTransactionStatistic qWebTransactionStatistic = QWebTransactionStatistic.webTransactionStatistic;
        BooleanExpression appIdCondition = qWebTransactionStatistic.transactionId.in(webTransactionIds);
        BooleanExpression periodCondition = qWebTransactionStatistic.period.eq(periodInMillis);
        BooleanExpression timestampCondition = qWebTransactionStatistic.timestamp
                .between(toMillisSecond(timeRange.getFrom()), toMillisSecond(timeRange.getTo()));

        BooleanExpression whereCondition = appIdCondition.and(periodCondition).and(timestampCondition);
        return webTransactionStatisticRepository.findAll(whereCondition);
    }

    /**
     * Get ExternalServices' statistic of the given timeRange in periods
     *
     * @param externalServices externalServices
     * @param timeRanges       timeRanges
     * @param period           periodInSecond
     *
     * @return the externalServices' statistic
     */
    public Map<TimeRange, Iterable<ExternalServiceStatistic>> getExternalServiceStatistic(
            Iterable<ExternalService> externalServices, List<TimeRange> timeRanges, Long period) {
        Assert.notNull(externalServices, "Transactions must not be null while getting external Service statistics.");
        Assert.notNull(timeRanges, "TimeRanges must not be null while getting external Service statistics.");
        Assert.notNull(period, "Period must be null while getting external Service statistics.");
        Assert.state(period % 60 == 0, "Period must be 60 or the times of 60.");

        Map<TimeRange, Iterable<ExternalServiceStatistic>> result = new HashMap<>();
        timeRanges.stream().forEach(timeRange -> {
            Iterable<ExternalServiceStatistic> currentRangeResult =
                    getExternalServiceStatistic(externalServices, timeRange, period);
            result.put(timeRange, currentRangeResult);
        });

        return result;
    }

    /**
     * Get ExternalServices' statistic of the given timeRange in period
     *
     * @param externalServices externalServices
     * @param timeRange        timeRange
     * @param period           periodInSecond
     *
     * @return the externalServices' statistic
     */
    public Iterable<ExternalServiceStatistic> getExternalServiceStatistic(Iterable<ExternalService> externalServices,
            TimeRange timeRange, Long period) {
        final Long periodInMillis = period * 1000;
        List<Long> externalServiceIds =
                StreamSupport.stream(externalServices.spliterator(), false).map(ExternalService::getId)
                        .collect(Collectors.toList());
        QExternalServiceStatistic qExternalServiceStatistic = QExternalServiceStatistic.externalServiceStatistic;
        BooleanExpression externalServiceIdCondition =
                qExternalServiceStatistic.externalServiceId.in(externalServiceIds);
        BooleanExpression periodCondition = qExternalServiceStatistic.period.eq(periodInMillis);
        BooleanExpression timestampCondition = qExternalServiceStatistic.timestamp
                .between(toMillisSecond(timeRange.getFrom()), toMillisSecond(timeRange.getTo()));

        BooleanExpression whereCondition = externalServiceIdCondition.and(periodCondition).and(timestampCondition);

        return externalTransactionStatisticRepository.findAll(whereCondition);
    }

    /**
     * Get DatabaseServices' statistic of the given timeRange in periods
     *
     * @param sqlTransactions sqlTransactions
     * @param timeRanges      timeRanges
     * @param period          periodInSecond
     *
     * @return the externalServices' statistic
     */
    public Map<TimeRange, Iterable<SqlTransactionStatistic>> getDatabaseServiceStatistic(
            Iterable<SqlTransaction> sqlTransactions, List<TimeRange> timeRanges, Long period) {
        Assert.notNull(sqlTransactions, "Transactions must not be null while getting Database Service statistics.");
        Assert.notNull(timeRanges, "TimeRanges must not be null while getting external Service statistics.");
        Assert.notNull(period, "Period must be null while getting external Service statistics.");
        Assert.state(period % 60 == 0, "Period must be 60 or the times of 60.");

        Map<TimeRange, Iterable<SqlTransactionStatistic>> result = new HashMap<>();
        timeRanges.stream().forEach(timeRange -> {
            Iterable<SqlTransactionStatistic> currentRangeResult =
                    getDatabaseServiceStatistic(sqlTransactions, timeRange, period);
            result.put(timeRange, currentRangeResult);
        });

        return result;
    }

    /**
     * Get DatabaseServices' statistic of the given timeRange in period
     *
     * @param sqlTransactions sqlTransactions
     * @param timeRange       timeRange
     * @param period          periodInSecond
     *
     * @return the DatabaseServices' statistic
     */
    public Iterable<SqlTransactionStatistic> getDatabaseServiceStatistic(Iterable<SqlTransaction> sqlTransactions,
            TimeRange timeRange, Long period) {
        final Long periodInMillis = period * 1000;
        List<Long> sqlTransactionIds =
                StreamSupport.stream(sqlTransactions.spliterator(), false).map(SqlTransaction::getId)
                        .collect(Collectors.toList());
        QSqlTransactionStatistic qSqlTransactionStatistic = QSqlTransactionStatistic.sqlTransactionStatistic;
        BooleanExpression sqlTransactionIdCondition = qSqlTransactionStatistic.sqlTransactionId.in(sqlTransactionIds);
        BooleanExpression periodCondition = qSqlTransactionStatistic.period.eq(periodInMillis);
        BooleanExpression timestampCondition = qSqlTransactionStatistic.timestamp
                .between(toMillisSecond(timeRange.getFrom()), toMillisSecond(timeRange.getTo()));

        BooleanExpression whereCondition = sqlTransactionIdCondition.and(periodCondition).and(timestampCondition);

        return sqlTransactionStatisticRepository.findAll(whereCondition);
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
    public Map<TimeRange, Iterable<WebTransactionStatistic>> getWebTransactionStatisticOfInstance(Long instanceId,
            List<TimeRange> timeRanges, Long period) {
        Assert.notNull(instanceId);
        Assert.notEmpty(timeRanges);
        Assert.notNull(period);

        final long periodInMillis = period * 1000;

        Iterable<WebTransaction> webTransactions = getWebTransactionsWithInstanceId(instanceId);
        List<Long> webTransactionIds =
                StreamSupport.stream(webTransactions.spliterator(), false).map(WebTransaction::getId)
                        .collect(Collectors.toList());

        QWebTransactionStatistic qWebTransactionStatistic = QWebTransactionStatistic.webTransactionStatistic;
        BooleanExpression appIdCondition = qWebTransactionStatistic.transactionId.in(webTransactionIds);
        BooleanExpression periodCondition = qWebTransactionStatistic.period.eq(periodInMillis);

        Map<TimeRange, Iterable<WebTransactionStatistic>> result = new HashMap<>();
        timeRanges.stream().forEach(timeRange -> {
            BooleanExpression timestampCondition = qWebTransactionStatistic.timestamp
                    .between(toMillisSecond(timeRange.getFrom()), toMillisSecond(timeRange.getTo()));

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
            List<TimeRange> timeRanges, Long period) {
        Assert.notNull(appId);
        Assert.notEmpty(timeRanges);
        Assert.notNull(period);
        final long periodInMillis = period * 1000;

        Iterable<SqlTransaction> sqlTransactions = getSqlTransactionsWithAppId(appId);
        List<Long> sqlTransactionIds =
                StreamSupport.stream(sqlTransactions.spliterator(), false).map(SqlTransaction::getId)
                        .collect(Collectors.toList());

        QSqlTransactionStatistic qSqlTransactionStatistic = QSqlTransactionStatistic.sqlTransactionStatistic;
        BooleanExpression appIdCondition = qSqlTransactionStatistic.sqlTransactionId.in(sqlTransactionIds);
        BooleanExpression periodCondition = qSqlTransactionStatistic.period.eq(periodInMillis);

        Map<TimeRange, Iterable<SqlTransactionStatistic>> result = new HashMap<>();
        timeRanges.stream().forEach(timeRange -> {
            BooleanExpression timestampCondition = qSqlTransactionStatistic.timestamp
                    .between(toMillisSecond(timeRange.getFrom()), toMillisSecond(timeRange.getTo()));

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
    public Map<TimeRange, Iterable<SqlTransactionStatistic>> getSqlTransactionStatisticOfInstance(Long instanceId,
            List<TimeRange> timeRanges, Long period) {
        Assert.notNull(instanceId);
        Assert.notEmpty(timeRanges);
        Assert.notNull(period);

        final long periodInMillis = period * 1000;

        Iterable<SqlTransaction> webTransactions = getSqlTransactionsWithInstanceId(instanceId);
        List<Long> sqlTransactionIds =
                StreamSupport.stream(webTransactions.spliterator(), false).map(SqlTransaction::getId)
                        .collect(Collectors.toList());

        QSqlTransactionStatistic qSqlTransactionStatistic = QSqlTransactionStatistic.sqlTransactionStatistic;
        BooleanExpression appIdCondition = qSqlTransactionStatistic.sqlTransactionId.in(sqlTransactionIds);
        BooleanExpression periodCondition = qSqlTransactionStatistic.period.eq(periodInMillis);

        Map<TimeRange, Iterable<SqlTransactionStatistic>> result = new HashMap<>();
        timeRanges.stream().forEach(timeRange -> {
            BooleanExpression timestampCondition = qSqlTransactionStatistic.timestamp
                    .between(toMillisSecond(timeRange.getFrom()), toMillisSecond(timeRange.getTo()));

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
            List<TimeRange> timeRanges, Long period) {
        Assert.notNull(appId);
        Assert.notEmpty(timeRanges);
        Assert.notNull(period);

        final long periodInMillis = period * 1000;

        Iterable<ExternalService> externalServices = getExternalServicesWithAppId(appId);
        List<Long> externalServiceIds =
                StreamSupport.stream(externalServices.spliterator(), false).map(ExternalService::getId)
                        .collect(Collectors.toList());

        QExternalServiceStatistic qExternalServiceStatistic = QExternalServiceStatistic.externalServiceStatistic;
        BooleanExpression serviceIdsCondition = qExternalServiceStatistic.externalServiceId.in(externalServiceIds);
        BooleanExpression periodCondition = qExternalServiceStatistic.period.eq(periodInMillis);

        Map<TimeRange, Iterable<ExternalServiceStatistic>> result = new HashMap<>();
        timeRanges.stream().forEach(timeRange -> {
            BooleanExpression timestampCondition = qExternalServiceStatistic.timestamp
                    .between(toMillisSecond(timeRange.getFrom()), toMillisSecond(timeRange.getTo()));

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
    public Map<TimeRange, Iterable<ExternalServiceStatistic>> getExternalServiceStatisticOfInstance(Long instanceId,
            List<TimeRange> timeRanges, Long period) {
        Assert.notNull(instanceId);
        Assert.notEmpty(timeRanges);
        Assert.notNull(period);
        final long periodInMillis = period * 1000;

        Iterable<ExternalService> externalServices = getExternalServicesWithInstanceId(instanceId);
        List<Long> externalServiceIds =
                StreamSupport.stream(externalServices.spliterator(), false).map(ExternalService::getId)
                        .collect(Collectors.toList());

        QExternalServiceStatistic qExternalServiceStatistic = QExternalServiceStatistic.externalServiceStatistic;
        BooleanExpression externalServiceIdsCondition =
                qExternalServiceStatistic.externalServiceId.in(externalServiceIds);
        BooleanExpression periodCondition = qExternalServiceStatistic.period.eq(periodInMillis);

        Map<TimeRange, Iterable<ExternalServiceStatistic>> result = new HashMap<>();
        timeRanges.stream().forEach(timeRange -> {
            BooleanExpression timestampCondition = qExternalServiceStatistic.timestamp
                    .between(toMillisSecond(timeRange.getFrom()), toMillisSecond(timeRange.getTo()));

            BooleanExpression whereCondition = externalServiceIdsCondition.and(periodCondition).and(timestampCondition);
            Iterable<ExternalServiceStatistic> currentRangeResult =
                    externalTransactionStatisticRepository.findAll(whereCondition);
            result.put(timeRange, currentRangeResult);
        });
        return result;
    }

    public Iterable<ExternalService> getExternalServicesWithAppId(Long appId) {
        Assert.notNull(appId, "cannot find ExternalServices with empty appId");
        QExternalService qExternalService = QExternalService.externalService;
        BooleanExpression appIdCondition = qExternalService.appId.eq(appId);
        return externalTransactionRepository.findAll(appIdCondition);
    }

    public Iterable<ExternalService> getExternalServicesWithInstanceId(Long instanceId) {
        Assert.notNull(instanceId, "cannot find ExternalServices with empty appId");
        QExternalService qExternalService = QExternalService.externalService;
        BooleanExpression instanceIdCondition = qExternalService.instanceId.eq(instanceId);
        return externalTransactionRepository.findAll(instanceIdCondition);
    }

    public Iterable<SqlTransaction> getSqlTransactionsWithAppId(Long appId) {
        Assert.notNull(appId, "cannot find SqlTransactions with empty appId");
        QSqlTransaction qSqlTransaction = QSqlTransaction.sqlTransaction;
        BooleanExpression appIdCondition = qSqlTransaction.appId.eq(appId);
        return sqlTransactionRepository.findAll(appIdCondition);
    }

    public Iterable<SqlTransaction> getSqlTransactionsWithInstanceId(Long instanceId) {
        Assert.notNull(instanceId, "cannot find SqlTransactions with empty appId");
        QSqlTransaction qSqlTransaction = QSqlTransaction.sqlTransaction;
        BooleanExpression instanceIdCondition = qSqlTransaction.instanceId.eq(instanceId);
        return sqlTransactionRepository.findAll(instanceIdCondition);
    }

    public Iterable<WebTransaction> getWebTransactionsWithAppId(Long appId) {
        Assert.notNull(appId, "cannot find WebTransactions with empty appId");
        QWebTransaction qWebTransaction = QWebTransaction.webTransaction;
        BooleanExpression appIdCondition = qWebTransaction.appId.eq(appId);
        return webTransactionRepository.findAll(appIdCondition);
    }

    public Iterable<WebTransaction> getWebTransactionsWithInstanceId(Long instanceId) {
        Assert.notNull(instanceId, "cannot find WebTransactions with empty appId");
        QWebTransaction qWebTransaction = QWebTransaction.webTransaction;
        BooleanExpression instanceIdCondition = qWebTransaction.instanceId.eq(instanceId);
        return webTransactionRepository.findAll(instanceIdCondition);
    }

}
