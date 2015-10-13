package com.baidu.oped.apm.mvc.controller;

import static com.baidu.oped.apm.common.utils.Constraints.StatisticMetricValue.CPM;
import static com.baidu.oped.apm.common.utils.Constraints.StatisticMetricValue.PV;
import static com.baidu.oped.apm.common.utils.Constraints.StatisticMetricValue.RESPONSE_TIME;
import static java.lang.String.format;

import java.time.LocalDateTime;
import java.util.List;

import com.baidu.oped.apm.common.jpa.entity.*;
import com.baidu.oped.apm.common.jpa.entity.Trace;
import com.baidu.oped.apm.mvc.vo.*;
import com.baidu.oped.apm.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baidu.oped.apm.common.utils.Constraints;
import com.baidu.oped.apm.common.utils.Constraints.StatisticMetricValue;
import com.baidu.oped.apm.model.service.AutomaticService;
import com.baidu.oped.apm.model.service.OverviewService;

/**
 * Web服务接口
 *
 * @author mason(meidongxu@baidu.com)
 */
@RestController
@RequestMapping("transactions/applications")
public class WebTransactionController extends BaseController {
    private static final String RETRIEVE_TOP_N_TRANS = "retrieving %s level top n web transactions";
    private static final String RETRIEVE_RT_TREND = "retrieving %s level web transactions response time trend";
    private static final String RETRIEVE_CPM_TREND = "retrieving %s level web transactions cpm trend";
    private static final String RETRIEVE_SLOW_TRANS = "retrieving %s level slow web transactions";

    private static final String RETRIEVE_TRACE_STACK = "retrieving transaction call stack";

    private static final String RETRIEVE_APP_TOP_N_TRANS = format(RETRIEVE_TOP_N_TRANS, "application");
    private static final String RETRIEVE_APP_RT_TREND = format(RETRIEVE_RT_TREND, "application");
    private static final String RETRIEVE_APP_CPM_TREND = format(RETRIEVE_CPM_TREND, "application");
    private static final String RETRIEVE_APP_SLOW_TRANS = format(RETRIEVE_SLOW_TRANS, "application");

    private static final String RETRIEVE_INSTANCE_TOP_N_TRANS = format(RETRIEVE_TOP_N_TRANS, "instance");
    private static final String RETRIEVE_INSTANCE_RT_TREND = format(RETRIEVE_RT_TREND, "instance");
    private static final String RETRIEVE_INSTANCE_CPM_TREND = format(RETRIEVE_CPM_TREND, "instance");
    private static final String RETRIEVE_INSTANCE_SLOW_TRANS = format(RETRIEVE_SLOW_TRANS, "instance");

    @Autowired
    private AutomaticService automaticService;

    @Autowired
    private OverviewService overviewService;

    /**
     * List Given application transactions.
     *
     * @param appId the application identification
     * @param from  time range begin
     * @param to    time range end
     * @param limit top n limit
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<TransactionVo> transactions(@RequestParam(value = "appId") Long appId,
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

        TimeRange timeRange = TimeUtils.createTimeRange(from, to);
        return overviewService.getTopNWebTransactionOfApp(appId, timeRange, limit);
    }

    /**
     * Get top n transaction response time trend data.
     *
     * @param appId  the application identification
     * @param time   time range, only one
     * @param period period in second
     * @param limit  top n limit
     * @return
     */
    @RequestMapping(value = {"trend/rt"})
    public TrendResponse responseTime(@RequestParam(value = "appId") Long appId,
                                      @RequestParam(value = "time[]") String[] time, @RequestParam(value = "period") Long period,
                                      @RequestParam(value = "limit", defaultValue = "5") Integer limit) {
        validApplicationId(appId, RETRIEVE_APP_RT_TREND);
        validSingleTimeRanges(time, RETRIEVE_APP_RT_TREND);
        validPeriod(period, RETRIEVE_APP_RT_TREND);
        validLimit(limit, RETRIEVE_APP_RT_TREND);

        final StatisticMetricValue[] metricName = new StatisticMetricValue[]{RESPONSE_TIME, PV, CPM};
        List<TimeRange> timeRanges = TimeUtils.convertToRange(time);
        final TimeRange timeRange = timeRanges.get(0);

        Iterable<WebTransaction> transactions = automaticService.getWebTransactionsOfApp(appId);

        Iterable<WebTransactionStatistic> transactionsStatistic =
                automaticService.getWebTransactionsStatistic(transactions, timeRange, period);

        TrendContext<String> trendContext =
                TrendContextUtils.topByAvgResponseTime(period, limit, timeRange, transactions, transactionsStatistic);

        return TrendUtils.toTrendResponse(trendContext, metricName);
    }

    /**
     * Get all transaction cpm of the application.
     *
     * @param appId  the application identification
     * @param time   time range, only one
     * @param period period in second
     */
    @RequestMapping(value = {"trend/cpm"})
    public TrendResponse cpm(@RequestParam(value = "appId") Long appId, @RequestParam(value = "time[]") String[] time,
                             @RequestParam(value = "period") Long period) {
        validApplicationId(appId, RETRIEVE_APP_CPM_TREND);
        validTimeRanges(time, RETRIEVE_APP_CPM_TREND);
        validPeriod(period, RETRIEVE_APP_CPM_TREND);

        final StatisticMetricValue[] metricName = new StatisticMetricValue[]{CPM, RESPONSE_TIME, PV};
        final ServiceType serviceType = ServiceType.WEB;
        List<TimeRange> timeRanges = TimeUtils.convertToRange(time);

        TrendContext trendContext = automaticService.getMetricDataOfApp(appId, timeRanges, period, serviceType);

        return TrendUtils.toTrendResponse(trendContext, metricName);
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
    @RequestMapping(value = {"traces"})
    public List<TraceVo> slowTransactions(@RequestParam(value = "appId") Long appId,
                                          @RequestParam(value = "from", required = false)
                                          @DateTimeFormat(pattern = Constraints.TIME_PATTERN) LocalDateTime from,
                                          @RequestParam(value = "to", required = false)
                                          @DateTimeFormat(pattern = Constraints.TIME_PATTERN) LocalDateTime to,
                                          @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageCount,
                                          @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                          @RequestParam(value = "orderBy", required = false) String orderBy) {
        validApplicationId(appId, RETRIEVE_APP_SLOW_TRANS);
        validTimeRange(from, to, RETRIEVE_APP_SLOW_TRANS);
        validPageInfo(pageCount, pageSize, RETRIEVE_APP_SLOW_TRANS);
        validSort(orderBy, RETRIEVE_APP_SLOW_TRANS);

        TimeRange timeRange = TimeUtils.createTimeRange(from, to);

        Iterable<Trace> trances = automaticService.getTraceOfApp(appId, timeRange, pageCount, pageSize, orderBy);

        return TraceUtils.toResponse(trances);
    }

    /**
     * List transactions of given instance.
     *
     * @param appId      the application identification
     * @param instanceId the instance identification, need to check relation with app
     * @param from       time range begin
     * @param to         time range end
     * @param limit      top n limit
     * @return
     */
    @RequestMapping(value = {"instances"}, method = RequestMethod.GET)
    public List<TransactionVo> instanceTransactions(@RequestParam(value = "appId") Long appId,
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

        TimeRange timeRange = TimeUtils.createTimeRange(from, to);
        return overviewService.getTopNWebTransactionOfInstance(instanceId, timeRange, limit);
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

        Iterable<WebTransaction> transactions = automaticService.getWebTransactionsOfInstance(instanceId);

        Iterable<WebTransactionStatistic> transactionsStatistic =
                automaticService.getWebTransactionsStatistic(transactions, timeRange, period);

        TrendContext<String> trendContext =
                TrendContextUtils.topByAvgResponseTime(period, limit, timeRange, transactions, transactionsStatistic);

        return TrendUtils.toTrendResponse(trendContext, metricName);
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
        final ServiceType serviceType = ServiceType.WEB;
        List<TimeRange> timeRanges = TimeUtils.convertToRange(time);
        TrendContext trendContext =
                automaticService.getMetricDataOfInstance(instanceId, timeRanges, period, serviceType);

        return TrendUtils.toTrendResponse(trendContext, metricName);
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
    public List<TraceVo> instanceSlowTransactions(
            @RequestParam(value = "appId") Long appId,
            @RequestParam(value = "instanceId") Long instanceId,
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(pattern = Constraints.TIME_PATTERN) LocalDateTime from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(pattern = Constraints.TIME_PATTERN) LocalDateTime to,
            @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageCount,
            @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
            @RequestParam(value = "orderBy", required = false) String orderBy) {
        validApplicationId(appId, RETRIEVE_INSTANCE_SLOW_TRANS);
        validInstanceId(instanceId, RETRIEVE_INSTANCE_SLOW_TRANS);
        validTimeRange(from, to, RETRIEVE_INSTANCE_SLOW_TRANS);
        validPageInfo(pageCount, pageSize, RETRIEVE_INSTANCE_SLOW_TRANS);
        validSort(orderBy, RETRIEVE_INSTANCE_SLOW_TRANS);

        TimeRange timeRange = TimeUtils.createTimeRange(from, to);

        Iterable<Trace> trances =
                automaticService.getTranceOfInstance(instanceId, timeRange, pageCount, pageSize, orderBy);

        return TraceUtils.toResponse(trances);
    }

    @RequestMapping(value = {"instances/traces/stack"})
    public CallStackVo transactionCallStack(@RequestParam(value = "appId") Long appId,
                                            @RequestParam(value = "instanceId") Long instanceId,
                                            @RequestParam("traceId") Long traceId){
        validApplicationId(appId, RETRIEVE_TRACE_STACK);
        validInstanceId(instanceId, RETRIEVE_TRACE_STACK);
        validTraceId(traceId, RETRIEVE_TRACE_STACK);

        return automaticService.getCallStackContext(traceId).build();
    }


    //
    //    @RequestMapping(value = {"${transactionId}/trend/execute"})
    //    public void test5(@PathVariable String transactionId) {
    //        return;
    //    }
    //
    //    @RequestMapping(value = {"${transactionId}/trend/cpm"})
    //    public void test6(@PathVariable String transactionId) {
    //        return;
    //    }
    //
    //    @RequestMapping(value = {"${transactionId}/breakdown"})
    //    public void test7(@PathVariable String transactionId) {
    //        return;
    //    }
    //
    //    @RequestMapping(value = {"${transactionId}/slow"})
    //    public void test8(@PathVariable String transactionId) {
    //        return;
    //    }
    //
    //    @RequestMapping(value = {"${transactionId}/details"})
    //    public void test9(@PathVariable String transactionId) {
    //        return;
    //    }
    //
    //    @RequestMapping(value = {"${transactionId}/details/trend/rt"})
    //    public void test10(@PathVariable String transactionId) {
    //        return;
    //    }
    //
    //    @RequestMapping(value = {"${transactionId}/details/trend/error"})
    //    public void test11(@PathVariable String transactionId) {
    //        return;
    //    }
    //
    //    @RequestMapping(value = {"${transactionId}/details/trend/apdex"})
    //    public void test12() {
    //        return;
    //    }
    //
    //    @RequestMapping(value = {"${transactionId}/details/trend/slow"})
    //    public void test13() {
    //        return;
    //    }
}
