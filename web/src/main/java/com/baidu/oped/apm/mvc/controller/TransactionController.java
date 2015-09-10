package com.baidu.oped.apm.mvc.controller;

import static com.baidu.oped.apm.common.utils.Constraints.MetricName.CPM;
import static com.baidu.oped.apm.common.utils.Constraints.MetricName.PV;
import static com.baidu.oped.apm.common.utils.Constraints.MetricName.RESPONSE_TIME;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baidu.oped.apm.common.jpa.entity.ServiceType;
import com.baidu.oped.apm.common.jpa.entity.WebTransaction;
import com.baidu.oped.apm.common.jpa.entity.WebTransactionStatistic;
import com.baidu.oped.apm.common.utils.Constraints;
import com.baidu.oped.apm.model.service.AutomaticService;
import com.baidu.oped.apm.model.service.OverviewService;
import com.baidu.oped.apm.mvc.vo.TimeRange;
import com.baidu.oped.apm.mvc.vo.Transaction;
import com.baidu.oped.apm.mvc.vo.TrendContext;
import com.baidu.oped.apm.mvc.vo.TrendResponse;
import com.baidu.oped.apm.utils.TimeUtils;
import com.baidu.oped.apm.utils.TrendContextUtils;
import com.baidu.oped.apm.utils.TrendUtils;

/**
 * Created by mason on 8/13/15.
 */
@RestController
@RequestMapping("transactions/applications")
public class TransactionController {

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
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<Transaction> transactions(@RequestParam(value = "appId") Long appId,
            @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = Constraints.TIME_PATTERN)
            LocalDateTime from,
            @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = Constraints.TIME_PATTERN)
            LocalDateTime to, @RequestParam(value = "limit") Integer limit) {
        Assert.notNull(appId, "ApplicationId must not be null while retrieving top n transactions.");
        Assert.notNull(from, "Time range start must not be null while retrieving top n transactions.");
        Assert.notNull(to, "Time range end must not be null while retrieving top n transactions.");
        Assert.state(from.isBefore(to), "Time range start must before than end.");
        Assert.state(limit > 0, "The limit must be greater than 0.");

        TimeRange timeRange = TimeUtils.createTimeRange(from, to);
        return overviewService.getWebTransactionStatisticOfApp(appId, timeRange, limit);
    }

    /**
     * Get top n transaction response time trend data.
     *
     * @param appId  the application identification
     * @param time   time range, only one
     * @param period period in second
     * @param limit  top n limit
     *
     * @return
     */
    @RequestMapping(value = {"trend/rt"})
    public TrendResponse responseTime(@RequestParam(value = "appId") Long appId,
            @RequestParam(value = "time[]") String[] time, @RequestParam(value = "period") Long period,
            @RequestParam(value = "limit", defaultValue = "5") Integer limit) {
        Assert.notNull(appId, "ApplicationId must not be null while retrieve web transaction response time trend data");
        Assert.notEmpty(time, "Time ranges must not be null while retrieve application response time trend data.");
        Assert.notNull(period, "Period must be provided while retrieve application response time trend data.");
        Assert.state(period % 60 == 0, "Period must be 60 or the times of 60.");
        Assert.state(limit > 0, "Limit must bigger that 0.");

        final Constraints.MetricName[] metricName = new Constraints.MetricName[] {RESPONSE_TIME, PV, CPM};
        List<TimeRange> timeRanges = TimeUtils.convertToRange(time);
        Assert.state(timeRanges.size() == 1, "Time param must be only one range.");

        final TimeRange timeRange = timeRanges.get(0);

        Iterable<WebTransaction> transactions = automaticService.getWebTransactionsWithAppId(appId);

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
        Assert.notNull(appId, "ApplicationId must not be null while retrieve web transaction response time trend data");
        Assert.notEmpty(time, "Time ranges must not be null while retrieve application response time trend data.");
        Assert.notNull(period, "Period must be provided while retrieve application response time trend data.");
        Assert.state(period % 60 == 0, "Period must be 60 or the times of 60.");

        final Constraints.MetricName[] metricName = new Constraints.MetricName[] {RESPONSE_TIME, PV, CPM};
        final ServiceType serviceType = ServiceType.WEB;
        List<TimeRange> timeRanges = TimeUtils.convertToRange(time);

        TrendContext trendContext = automaticService.getMetricDataOfApp(appId, timeRanges, period, serviceType);

        return TrendUtils.toTrendResponse(trendContext, metricName);
    }

    /**
     * Get all transactions order by response time desc.
     *
     * @param appId
     * @param time
     * @param pageCount
     * @param pageSize
     * @param orderby
     *
     * @return
     */
    @RequestMapping(value = {"traces"})
    public List<Transaction> slowTransactions(@RequestParam(value = "appId") Long appId,
            @RequestParam(value = "time[]") String[] time,
            @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageCount,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "orderBy", required = false) String orderby) {

        return null;
    }

    /**
     * List transactions of given instance.
     *
     * @param appId      the application identification
     * @param instanceId the instance identification, need to check relation with app
     * @param from       time range begin
     * @param to         time range end
     * @param limit      top n limit
     *
     * @return
     */
    @RequestMapping(value = {"instances"}, method = RequestMethod.GET)
    public List<Transaction> instanceTransactions(@RequestParam(value = "appId") Long appId,
            @RequestParam(value = "instanceId") Long instanceId,
            @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = Constraints.TIME_PATTERN)
            LocalDateTime from,
            @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = Constraints.TIME_PATTERN)
            LocalDateTime to, @RequestParam(value = "limit") Integer limit) {
        Assert.notNull(appId, "ApplicationId must not be null while retrieving top n transactions.");
        Assert.notNull(instanceId, "InstanceId must not be null while retrieving top n transactions.");
        Assert.notNull(from, "Time range start must not be null while retrieving top n transactions.");
        Assert.notNull(to, "Time range end must not be null while retrieving top n transactions.");
        Assert.state(from.isBefore(to), "Time range start must before than end.");
        Assert.state(limit > 0, "The limit must be greater than 0.");

        TimeRange timeRange = TimeUtils.createTimeRange(from, to);
        return overviewService.getWebTransactionStatisticOfInstance(instanceId, timeRange, limit);
    }

    /**
     * Get top n transaction response time trend data of given instance.
     *
     * @param appId      the application identification
     * @param instanceId the instance identification, need to check relation with app
     * @param time       time range, only one
     * @param period     period in second
     * @param limit      top n limit
     *
     * @return
     */
    @RequestMapping(value = {"instances/trend/rt"})
    public TrendResponse instanceResponseTime(@RequestParam(value = "appId") Long appId,
            @RequestParam(value = "instanceId") Long instanceId, @RequestParam(value = "time[]") String[] time,
            @RequestParam(value = "period") Long period, @RequestParam(value = "limit") Integer limit) {
        Assert.notNull(appId, "ApplicationId must not be null while retrieve web transaction response time trend data");
        Assert.notNull(instanceId,
                       "InstanceId must not be null while retireving web transaction response time trend" + " data.");
        Assert.notEmpty(time, "Time ranges must not be null while retrieve application response time trend data.");
        Assert.notNull(period, "Period must be provided while retrieve application response time trend data.");
        Assert.state(period % 60 == 0, "Period must be 60 or the times of 60.");
        Assert.state(limit > 0, "Limit must bigger that 0.");

        final Constraints.MetricName[] metricName = new Constraints.MetricName[] {RESPONSE_TIME, PV, CPM};
        List<TimeRange> timeRanges = TimeUtils.convertToRange(time);
        Assert.state(timeRanges.size() == 1, "Time param must be only one range.");

        final TimeRange timeRange = timeRanges.get(0);

        Iterable<WebTransaction> transactions = automaticService.getWebTransactionsWithInstanceId(instanceId);

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
     *
     * @return
     */
    @RequestMapping(value = {"instances/trend/cpm"})
    public TrendResponse instanceCpm(@RequestParam(value = "appId") Long appId,
            @RequestParam(value = "instanceId") Long instanceId, @RequestParam(value = "time[]") String[] time,
            @RequestParam(value = "period") Long period) {
        Assert.notNull(appId, "ApplicationId must not be null while retrieve web transaction response time trend data");
        Assert.notNull(instanceId,
                       "InstanceId must not be null while retireving web transaction response time trend" + " data.");
        Assert.notEmpty(time, "Time ranges must not be null while retrieve application response time trend data.");
        Assert.notNull(period, "Period must be provided while retrieve application response time trend data.");
        Assert.state(period % 60 == 0, "Period must be 60 or the times of 60.");

        final Constraints.MetricName[] metricName = new Constraints.MetricName[] {RESPONSE_TIME, PV, CPM};
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
     *
     * @return
     */
    @RequestMapping(value = {"instances/traces"})
    public List<Transaction> instanceSlowTransactions(@RequestParam(value = "appId") Long appId,
            @RequestParam(value = "instanceId") Long instanceId,
            @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = Constraints.TIME_PATTERN)
            LocalDateTime from,
            @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = Constraints.TIME_PATTERN)
            LocalDateTime to, @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageCount,
            @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
            @RequestParam(value = "orderBy", required = false) String orderBy) {

        return null;
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
