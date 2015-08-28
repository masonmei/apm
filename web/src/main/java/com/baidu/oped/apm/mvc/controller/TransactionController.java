package com.baidu.oped.apm.mvc.controller;

import static com.baidu.oped.apm.utils.Constaints.MetricName.APDEX;
import static com.baidu.oped.apm.utils.Constaints.MetricName.CPM;
import static com.baidu.oped.apm.utils.Constaints.MetricName.FRUSTRATED;
import static com.baidu.oped.apm.utils.Constaints.MetricName.PV;
import static com.baidu.oped.apm.utils.Constaints.MetricName.RESPONSE_TIME;
import static com.baidu.oped.apm.utils.Constaints.MetricName.SATISFIED;
import static com.baidu.oped.apm.utils.Constaints.MetricName.TOLERATED;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baidu.oped.apm.common.jpa.entity.ApplicationStatistic;
import com.baidu.oped.apm.common.jpa.entity.InstanceStatistic;
import com.baidu.oped.apm.common.jpa.entity.ServiceType;
import com.baidu.oped.apm.common.jpa.entity.TransactionStatistic;
import com.baidu.oped.apm.model.service.TransactionService;
import com.baidu.oped.apm.mvc.vo.TimeRange;
import com.baidu.oped.apm.mvc.vo.Transaction;
import com.baidu.oped.apm.mvc.vo.TrendResponse;
import com.baidu.oped.apm.utils.Constaints;
import com.baidu.oped.apm.utils.MetricUtils;
import com.baidu.oped.apm.utils.PageUtils;
import com.baidu.oped.apm.utils.TimeUtils;

/**
 * Created by mason on 8/13/15.
 */
@RestController
@RequestMapping("transactions/applications")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    /**
     * List Given application transactions.
     *
     * @param appId
     * @param from
     * @param to
     * @param limit
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<Transaction> transactions(
            @RequestParam(value = "appId") Long appId,
            @RequestParam(value = "instanceId") Long instanceId,
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(pattern = Constaints.TIME_PATTERN) LocalDateTime from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(pattern = Constaints.TIME_PATTERN) LocalDateTime to,
            @RequestParam(value = "limit") Integer limit){

        return null;
    }

    /**
     * Get top n transaction response time trend data.
     *
     * @param appId
     * @param time
     * @param period
     * @param limit
     * @return
     */
    @RequestMapping(value = {"trend/rt"})
    public TrendResponse responseTime(
            @RequestParam(value = "appId") Long appId,
            @RequestParam(value = "time") String[] time,
            @RequestParam(value = "period") Integer period,
            @RequestParam(value = "limit", defaultValue = "5") Integer limit) {
        Assert.notNull(appId, "ApplicationId must not be null while retrieve web transaction response time trend data");
        Assert.notEmpty(time, "Time ranges must not be null while retrieve application response time trend data.");
        Assert.notNull(period, "Period must be provided while retrieve application response time trend data.");
        Assert.state(period / 60 == 0, "Period must be 60 or the times of 60.");
        Assert.state(limit > 0, "Limit must bigger that 0.");

        final Constaints.MetricName[] metricName = new Constaints.MetricName[] {RESPONSE_TIME, PV, CPM};
        final ServiceType serviceType = ServiceType.WEB;
        List<TimeRange> timeRanges = TimeUtils.convertToRange(time);

        Map<TimeRange, Iterable<TransactionStatistic>> applicationMetricData =
                transactionService.getApplicationLevelTransactionMetricData(appId, timeRanges, period);

        return MetricUtils.transactionMetricToTrendResponse(applicationMetricData, metricName, serviceType);
    }

    /**
     *  Get all transaction cpm of the application.
     *
     * @param appId
     * @param time
     * @param period
     */
    @RequestMapping(value = {"trend/cpm"})
    public TrendResponse cpm(
            @RequestParam(value = "appId") Long appId,
            @RequestParam(value = "time") String[] time,
            @RequestParam(value = "period") Integer period) {
        Assert.notNull(appId, "ApplicationId must not be null while retrieve web transaction response time trend data");
        Assert.notEmpty(time, "Time ranges must not be null while retrieve application response time trend data.");
        Assert.notNull(period, "Period must be provided while retrieve application response time trend data.");
        Assert.state(period / 60 == 0, "Period must be 60 or the times of 60.");

        final Constaints.MetricName[] metricName = new Constaints.MetricName[] {APDEX, SATISFIED, TOLERATED, FRUSTRATED};
        final ServiceType serviceType = ServiceType.WEB;
        List<TimeRange> timeRanges = TimeUtils.convertToRange(time);

        Map<TimeRange, Iterable<TransactionStatistic>> instanceMetricData =
                transactionService.getApplicationLevelTransactionMetricData(appId, timeRanges, period);

        return MetricUtils.transactionMetricToTrendResponse(instanceMetricData, metricName, serviceType);
    }

    /**
     * Get all transactions order by response time desc.
     *
     * @param appId
     * @param time
     * @param pageCount
     * @param pageSize
     * @param orderby
     * @return
     */
    @RequestMapping(value = {"traces"})
    public List<Transaction> slowTransactions(
            @RequestParam(value = "appId") Long appId,
            @RequestParam(value = "time") String[] time,
            @RequestParam(value = "pageCount", defaultValue = "1") Integer pageCount,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "orderBy", required = false) String orderby) {

        return null;
    }

    /**
     * List transactions of given instance.
     *
     * @param appId
     * @param instanceId
     * @param from
     * @param to
     * @param limit
     * @return
     */
    @RequestMapping(value = {"instances"}, method = RequestMethod.GET)
    public List<Transaction> instanceTransactions(
            @RequestParam(value = "appId") Long appId,
            @RequestParam(value = "instanceId") Long instanceId,
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(pattern = Constaints.TIME_PATTERN) LocalDateTime from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(pattern = Constaints.TIME_PATTERN) LocalDateTime to,
            @RequestParam(value = "limit") Integer limit){

        return null;
    }

    /**
     * Get top n transaction response time trend data of given instance.
     *
     * @param appId
     * @param instanceId
     * @param time
     * @param period
     * @param limit
     * @return
     */
    @RequestMapping(value = {"instances/trend/rt"})
    public TrendResponse instanceResponseTime(
            @RequestParam(value = "appId") Long appId,
            @RequestParam(value = "instanceId") Long instanceId,
            @RequestParam(value = "time") String[] time,
            @RequestParam(value = "period") Integer period,
            @RequestParam(value = "limit") Integer limit) {
        Assert.notNull(appId, "ApplicationId must not be null while retrieve web transaction response time trend data");
        Assert.notNull(instanceId, "InstanceId must not be null while retireving web transaction response time trend"
                                           + " data.");
        Assert.notEmpty(time, "Time ranges must not be null while retrieve application response time trend data.");
        Assert.notNull(period, "Period must be provided while retrieve application response time trend data.");
        Assert.state(period / 60 == 0, "Period must be 60 or the times of 60.");
        Assert.state(limit > 0, "Limit must bigger that 0.");

        final Constaints.MetricName[] metricName = new Constaints.MetricName[] {RESPONSE_TIME, PV, CPM};
        final ServiceType serviceType = ServiceType.WEB;
        List<TimeRange> timeRanges = TimeUtils.convertToRange(time);

        Map<TimeRange, Iterable<TransactionStatistic>> instanceMetricData =
                transactionService.getInstanceLevelTransactionMetricData(instanceId, timeRanges, period);

        return MetricUtils.transactionMetricToTrendResponse(instanceMetricData, metricName, serviceType);
    }

    /**
     * Get all transaction cpm of given instance.
     *
     * @param appId
     * @param instanceId
     * @param time
     * @param period
     * @return
     */
    @RequestMapping(value = {"instances/trend/cpm"})
    public TrendResponse instanceCpm(
            @RequestParam(value = "appId") Long appId,
            @RequestParam(value = "instanceId") Long instanceId,
            @RequestParam(value = "time") String[] time,
            @RequestParam(value = "period") Integer period) {
        Assert.notNull(appId, "ApplicationId must not be null while retrieve web transaction response time trend data");
        Assert.notNull(instanceId, "InstanceId must not be null while retireving web transaction response time trend"
                                           + " data.");
        Assert.notEmpty(time, "Time ranges must not be null while retrieve application response time trend data.");
        Assert.notNull(period, "Period must be provided while retrieve application response time trend data.");
        Assert.state(period / 60 == 0, "Period must be 60 or the times of 60.");

        final Constaints.MetricName[] metricName = new Constaints.MetricName[] {APDEX, SATISFIED, TOLERATED, FRUSTRATED};
        final ServiceType serviceType = ServiceType.WEB;
        List<TimeRange> timeRanges = TimeUtils.convertToRange(time);

        Map<TimeRange, Iterable<TransactionStatistic>> instanceMetricData =
                transactionService.getInstanceLevelTransactionMetricData(instanceId, timeRanges, period);

        return MetricUtils.transactionMetricToTrendResponse(instanceMetricData, metricName, serviceType);
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
    public List<Transaction> instanceSlowTransactions(
            @RequestParam(value = "appId") Long appId,
            @RequestParam(value = "instanceId") Long instanceId,
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(pattern = Constaints.TIME_PATTERN) LocalDateTime from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(pattern = Constaints.TIME_PATTERN) LocalDateTime to,
            @RequestParam(value = "pageCount", defaultValue = "1") Integer pageCount,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
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
