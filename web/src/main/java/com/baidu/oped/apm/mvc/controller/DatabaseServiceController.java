package com.baidu.oped.apm.mvc.controller;

import com.baidu.oped.apm.common.jpa.entity.ServiceType;
import com.baidu.oped.apm.common.jpa.entity.SqlTransaction;
import com.baidu.oped.apm.common.jpa.entity.SqlTransactionStatistic;
import com.baidu.oped.apm.common.utils.Constraints;
import com.baidu.oped.apm.common.utils.Constraints.StatisticMetricValue;
import com.baidu.oped.apm.model.service.AutomaticService;
import com.baidu.oped.apm.mvc.vo.*;
import com.baidu.oped.apm.utils.DatabaseServiceUtils;
import com.baidu.oped.apm.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static com.baidu.oped.apm.common.utils.Constraints.StatisticMetricValue.*;
import static com.baidu.oped.apm.utils.TrendUtils.toTrendResponse;
import static java.lang.String.format;

/**
 * 数据库服务对应接口
 *
 * @author mason(meidongxu@baidu.com)
 */
@RestController
@RequestMapping("database/applications")
public class DatabaseServiceController extends BaseController {
    private static final String RETRIEVE_TOP_N_TRANS = "retrieving %s level top n database service";
    private static final String RETRIEVE_RT_TREND = "retrieving %s level database service response time trend";
    private static final String RETRIEVE_CPM_TREND = "retrieving %s level database service cpm trend";

    private static final String RETRIEVE_APP_TOP_N_TRANS = format(RETRIEVE_TOP_N_TRANS, "application");
    private static final String RETRIEVE_APP_RT_TREND = format(RETRIEVE_RT_TREND, "application");
    private static final String RETRIEVE_APP_CPM_TREND = format(RETRIEVE_CPM_TREND, "application");

    private static final String RETRIEVE_INSTANCE_TOP_N_TRANS = format(RETRIEVE_TOP_N_TRANS, "instance");
    private static final String RETRIEVE_INSTANCE_RT_TREND = format(RETRIEVE_RT_TREND, "instance");
    private static final String RETRIEVE_INSTANCE_CPM_TREND = format(RETRIEVE_CPM_TREND, "instance");

    @Autowired
    private AutomaticService automaticService;

    /**
     * List Given application database service.
     *
     * @param appId the application identification
     * @param from  time range begin
     * @param to    time range end
     * @param limit top n limit
     * @return the database services
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<DatabaseServiceVo> transactions(@RequestParam(value = "appId") Long appId,
                                                @RequestParam(value = "from", required = false)
                                                @DateTimeFormat(pattern = Constraints.TIME_PATTERN)
                                                LocalDateTime from,
                                                @RequestParam(value = "to", required = false)
                                                @DateTimeFormat(pattern = Constraints.TIME_PATTERN)
                                                LocalDateTime to,
                                                @RequestParam(value = "limit") Integer limit) {
        validApplicationId(appId, RETRIEVE_APP_TOP_N_TRANS);
        validTimeRange(from, to, RETRIEVE_APP_TOP_N_TRANS);
        validLimit(limit, RETRIEVE_APP_TOP_N_TRANS);

        final Long period = DEFAULT_PERIOD;

        TimeRange timeRange = TimeUtils.createTimeRange(from, to);

        Iterable<SqlTransaction> sqlTransactions = automaticService.getSqlTransactionsOfApp(appId);
        Iterable<SqlTransactionStatistic> statistic =
                automaticService.getSqlTransactionStatistic(sqlTransactions, timeRange, period);

        return DatabaseServiceUtils.topByAvgResponseTime(statistic, sqlTransactions, timeRange, limit);
    }

    /**
     * Get top n transaction response time trend data.
     *
     * @param appId  the application identification
     * @param time   time range, only one
     * @param period period in second
     * @param limit  top n limit
     * @return the top n response time trend data
     */
    @RequestMapping(value = {"trend/rt"})
    public TrendResponse responseTime(@RequestParam(value = "appId") Long appId,
                                      @RequestParam(value = "time[]") String[] time,
                                      @RequestParam(value = "period") Long period,
                                      @RequestParam(value = "limit", defaultValue = "5") Integer limit) {
        validApplicationId(appId, RETRIEVE_APP_RT_TREND);
        validSingleTimeRanges(time, RETRIEVE_APP_RT_TREND);
        validPeriod(period, RETRIEVE_APP_RT_TREND);
        validLimit(limit, RETRIEVE_APP_RT_TREND);

        final StatisticMetricValue[] metricName = new StatisticMetricValue[]{RESPONSE_TIME, PV, CPM};
        List<TimeRange> timeRanges = TimeUtils.convertToRange(time);

        final TimeRange timeRange = timeRanges.get(0);

        Iterable<SqlTransaction> transactions = automaticService.getSqlTransactionsOfApp(appId);

        Iterable<SqlTransactionStatistic> transactionsStatistic =
                automaticService.getSqlTransactionStatistic(transactions, timeRange, period);

        TrendContext<String> trendContext = DatabaseServiceUtils
                .topByAvgResponseTime(period, limit, timeRange, transactions, transactionsStatistic);

        return toTrendResponse(trendContext, metricName);
    }

    /**
     * Get all transaction cpm of the application.
     *
     * @param appId  the application identification
     * @param time   time range, only one
     * @param period period in second
     * @return the cpm trend data
     */
    @RequestMapping(value = {"trend/cpm"})
    public TrendResponse cpm(@RequestParam(value = "appId") Long appId,
                             @RequestParam(value = "time[]") String[] time,
                             @RequestParam(value = "period") Long period) {
        validApplicationId(appId, RETRIEVE_APP_CPM_TREND);
        validTimeRanges(time, RETRIEVE_APP_CPM_TREND);
        validPeriod(period, RETRIEVE_APP_CPM_TREND);

        final StatisticMetricValue[] metricName = new StatisticMetricValue[]{CPM, RESPONSE_TIME, PV};
        final ServiceType serviceType = ServiceType.DB;
        List<TimeRange> timeRanges = TimeUtils.convertToRange(time);

        TrendContext trendContext = automaticService.getMetricDataOfApp(appId, timeRanges, period, serviceType);

        return toTrendResponse(trendContext, metricName);
    }

    /**
     * List transactions of given instance.
     *
     * @param appId      the application identification
     * @param instanceId the instance identification, need to check relation with app
     * @param from       time range begin
     * @param to         time range end
     * @param limit      top n limit
     * @return the top n transactions
     */
    @RequestMapping(value = {"instances"}, method = RequestMethod.GET)
    public List<DatabaseServiceVo> instanceTransactions(@RequestParam(value = "appId") Long appId,
                                                        @RequestParam(value = "instanceId") Long instanceId,
                                                        @RequestParam(value = "from", required = false)
                                                        @DateTimeFormat(pattern = Constraints.TIME_PATTERN)
                                                        LocalDateTime from,
                                                        @RequestParam(value = "to", required = false)
                                                        @DateTimeFormat(pattern = Constraints.TIME_PATTERN)
                                                        LocalDateTime to,
                                                        @RequestParam(value = "limit") Integer limit) {
        validApplicationId(appId, RETRIEVE_INSTANCE_TOP_N_TRANS);
        validInstanceId(instanceId, RETRIEVE_INSTANCE_TOP_N_TRANS);
        validTimeRange(from, to, RETRIEVE_INSTANCE_TOP_N_TRANS);
        validLimit(limit, RETRIEVE_INSTANCE_TOP_N_TRANS);

        final Long period = DEFAULT_PERIOD;
        TimeRange timeRange = TimeUtils.createTimeRange(from, to);

        Iterable<SqlTransaction> sqlTransactions = automaticService.getSqlTransactionsOfApp(appId);
        Iterable<SqlTransactionStatistic> statistic =
                automaticService.getSqlTransactionStatistic(sqlTransactions, timeRange, period);

        return DatabaseServiceUtils.topByAvgResponseTime(statistic, sqlTransactions, timeRange, limit);
    }

    /**
     * Get top n transaction response time trend data of given instance.
     *
     * @param appId      the application identification
     * @param instanceId the instance identification, need to check relation with app
     * @param time       time range, only one
     * @param period     period in second
     * @param limit      top n limit
     * @return
     */
    @RequestMapping(value = {"instances/trend/rt"})
    public TrendResponse instanceResponseTime(@RequestParam(value = "appId") Long appId,
                                              @RequestParam(value = "instanceId") Long instanceId,
                                              @RequestParam(value = "time[]") String[] time,
                                              @RequestParam(value = "period") Long period,
                                              @RequestParam(value = "limit") Integer limit) {
        validApplicationId(appId, RETRIEVE_INSTANCE_RT_TREND);
        validInstanceId(instanceId, RETRIEVE_INSTANCE_RT_TREND);
        validSingleTimeRanges(time, RETRIEVE_INSTANCE_RT_TREND);
        validPeriod(period, RETRIEVE_INSTANCE_RT_TREND);
        validLimit(limit, RETRIEVE_INSTANCE_RT_TREND);

        final StatisticMetricValue[] metricName = new StatisticMetricValue[]{RESPONSE_TIME, PV, CPM};
        List<TimeRange> timeRanges = TimeUtils.convertToRange(time);

        final TimeRange timeRange = timeRanges.get(0);

        Iterable<SqlTransaction> transactions = automaticService.getSqlTransactionsOfInstance(instanceId);

        Iterable<SqlTransactionStatistic> transactionsStatistic =
                automaticService.getSqlTransactionStatistic(transactions, timeRange, period);

        TrendContext<String> trendContext = DatabaseServiceUtils
                .topByAvgResponseTime(period, limit, timeRange, transactions, transactionsStatistic);

        return toTrendResponse(trendContext, metricName);
    }

    /**
     * Get all transaction cpm of given instance.
     *
     * @param appId      the application identification
     * @param instanceId the instance identification, need to check relation with app
     * @param time       time range, only one
     * @param period     period in second
     * @return
     */
    @RequestMapping(value = {"instances/trend/cpm"})
    public TrendResponse instanceCpm(@RequestParam(value = "appId") Long appId,
                                     @RequestParam(value = "instanceId") Long instanceId,
                                     @RequestParam(value = "time[]") String[] time,
                                     @RequestParam(value = "period") Long period) {
        validApplicationId(appId, RETRIEVE_INSTANCE_CPM_TREND);
        validInstanceId(instanceId, RETRIEVE_INSTANCE_CPM_TREND);
        validTimeRanges(time, RETRIEVE_INSTANCE_CPM_TREND);
        validPeriod(period, RETRIEVE_INSTANCE_CPM_TREND);

        final StatisticMetricValue[] metricName = new StatisticMetricValue[]{CPM, RESPONSE_TIME, PV};
        final ServiceType serviceType = ServiceType.DB;
        List<TimeRange> timeRanges = TimeUtils.convertToRange(time);
        TrendContext trendContext =
                automaticService.getMetricDataOfInstance(instanceId, timeRanges, period, serviceType);

        return toTrendResponse(trendContext, metricName);
    }

    /**
     * Get all transactions order by response time desc.
     *
     * @param appId
     * @param from
     * @param to
     * @param pageCount
     * @param pageSize
     * @param orderBy
     * @return
     */
    @RequestMapping(value = {"instances/traces"})
    public List<TransactionVo> instanceSlowTransactions(@RequestParam(value = "appId") Long appId,
                                                        @RequestParam(value = "instanceId") Long instanceId,
                                                        @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = Constraints.TIME_PATTERN)
                                                        LocalDateTime from,
                                                        @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = Constraints.TIME_PATTERN)
                                                        LocalDateTime to, @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageCount,
                                                        @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
                                                        @RequestParam(value = "orderBy", required = false) String orderBy) {

        return null;
    }


}
