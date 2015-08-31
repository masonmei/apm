package com.baidu.oped.apm.mvc.controller;

import static com.baidu.oped.apm.utils.Constaints.MetricName.APDEX;
import static com.baidu.oped.apm.utils.Constaints.MetricName.CPM;
import static com.baidu.oped.apm.utils.Constaints.MetricName.ERROR;
import static com.baidu.oped.apm.utils.Constaints.MetricName.ERROR_RATE;
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

import com.baidu.oped.apm.common.jpa.entity.Application;
import com.baidu.oped.apm.common.jpa.entity.ApplicationStatistic;
import com.baidu.oped.apm.common.jpa.entity.Instance;
import com.baidu.oped.apm.common.jpa.entity.InstanceStatistic;
import com.baidu.oped.apm.common.jpa.entity.ServiceType;
import com.baidu.oped.apm.model.service.OverviewService;
import com.baidu.oped.apm.mvc.vo.InstanceVo;
import com.baidu.oped.apm.mvc.vo.TimeRange;
import com.baidu.oped.apm.mvc.vo.Transaction;
import com.baidu.oped.apm.mvc.vo.TrendResponse;
import com.baidu.oped.apm.utils.Constaints;
import com.baidu.oped.apm.utils.InstanceUtils;
import com.baidu.oped.apm.utils.MetricUtils;
import com.baidu.oped.apm.utils.TimeUtils;

/**
 * Created by mason on 8/25/15.
 */
@RestController
@RequestMapping("overview/applications")
public class OverviewController {

    @Autowired
    private OverviewService overviewService;

    /**
     * Get Application Response Time trend data
     *
     * @param appId
     * @param time
     * @param period
     */
    @RequestMapping(value = {"trend/rt"}, method = RequestMethod.GET)
    public TrendResponse responseTime(@RequestParam(value = "appId") Long appId,
                                      @RequestParam(value = "time") String[] time,
                                      @RequestParam(value = "period") Integer period) {
        Assert.notNull(appId, "ApplicationId must not be null while retrieve application response time trend data.");
        Assert.notEmpty(time, "Time ranges must not be null while retrieve application response time trend data.");
        Assert.notNull(period, "Period must be provided while retrieve application response time trend data.");
        Assert.state(period / 60 == 0, "Period must be 60 or the times of 60.");

        final Constaints.MetricName[] metricName = new Constaints.MetricName[] {RESPONSE_TIME, PV, CPM};
        final ServiceType[] serviceTypes =
                new ServiceType[] {ServiceType.WEB, ServiceType.DB, ServiceType.EXTERNAL, ServiceType.BACKGROUD};
        List<TimeRange> timeRanges = TimeUtils.convertToRange(time);

        Map<TimeRange, Iterable<ApplicationStatistic>> applicationMetricData =
                overviewService.getApplicationMetricData(appId, timeRanges, period, serviceTypes);

        return MetricUtils.toTrendResponse(applicationMetricData, metricName);
    }

    /**
     * Get Application Apdex trend data.
     *
     * @param appId
     * @param time
     * @param period
     *
     * @return
     */
    @RequestMapping(value = {"trend/apdex"}, method = RequestMethod.GET)
    public TrendResponse apdex(@RequestParam(value = "appId") Long appId, @RequestParam(value = "time") String[] time,
                               @RequestParam(value = "period") Integer period) {
        Assert.notNull(appId, "ApplicationId must not be null while retrieve application response time trend data.");
        Assert.notEmpty(time, "Time ranges must not be null while retrieve application response time trend data.");
        Assert.notNull(period, "Period must be provided while retrieve application response time trend data.");
        Assert.state(period / 60 == 0, "Period must be 60 or the times of 60.");

        final Constaints.MetricName[] metricName = new Constaints.MetricName[] {APDEX, SATISFIED, TOLERATED, FRUSTRATED};
        final ServiceType[] serviceTypes =
                new ServiceType[] {ServiceType.WEB, ServiceType.DB, ServiceType.EXTERNAL, ServiceType.BACKGROUD};
        List<TimeRange> timeRanges = TimeUtils.convertToRange(time);

        Map<TimeRange, Iterable<ApplicationStatistic>> applicationMetricData =
                overviewService.getApplicationMetricData(appId, timeRanges, period, serviceTypes);

        return MetricUtils.toTrendResponse(applicationMetricData, metricName);
    }

    /**
     * Get Application count per minute trend data.
     *
     * @param appId
     * @param time
     * @param period
     *
     * @return
     */
    @RequestMapping(value = {"trend/cpm"}, method = RequestMethod.GET)
    public TrendResponse cpm(@RequestParam(value = "appId") Long appId, @RequestParam(value = "time") String[] time,
                             @RequestParam(value = "period") Integer period) {
        Assert.notNull(appId, "ApplicationId must not be null while retrieve application response time trend data.");
        Assert.notEmpty(time, "Time ranges must not be null while retrieve application response time trend data.");
        Assert.notNull(period, "Period must be provided while retrieve application response time trend data.");
        Assert.state(period / 60 == 0, "Period must be 60 or the times of 60.");

        final Constaints.MetricName[] metricName = new Constaints.MetricName[] {RESPONSE_TIME, PV, CPM};
        final ServiceType[] serviceTypes =
                new ServiceType[] {ServiceType.WEB, ServiceType.DB, ServiceType.EXTERNAL, ServiceType.BACKGROUD};
        List<TimeRange> timeRanges = TimeUtils.convertToRange(time);

        Map<TimeRange, Iterable<ApplicationStatistic>> applicationMetricData =
                overviewService.getApplicationMetricData(appId, timeRanges, period, serviceTypes);

        return MetricUtils.toTrendResponse(applicationMetricData, metricName);
    }

    /**
     * Get Application WebTransaction limit top n, ordered by average response time.
     *
     * @param appId
     * @param from
     * @param to
     * @param limit
     *
     * @return
     */
    @RequestMapping(value = {"transactions"}, method = RequestMethod.GET)
    public List<Transaction> transaction(@RequestParam(value = "appId") Long appId,
                                         @RequestParam(value = "from", required = false)
                                         @DateTimeFormat(pattern = Constaints.TIME_PATTERN) LocalDateTime from,
                                         @RequestParam(value = "to", required = false)
                                         @DateTimeFormat(pattern = Constaints.TIME_PATTERN) LocalDateTime to,
                                         @RequestParam(value = "limit") Integer limit) {

        return null;
    }

    /**
     * Get Application error Rate trend data.
     *
     * @param appId
     * @param time
     * @param period
     *
     * @return
     */
    @RequestMapping(value = {"trend/errorRate"}, method = RequestMethod.GET)
    public TrendResponse errorRate(@RequestParam(value = "appId") Long appId,
                                   @RequestParam(value = "time") String[] time,
                                   @RequestParam(value = "period") Integer period) {
        Assert.notNull(appId, "ApplicationId must not be null while retrieve application response time trend data.");
        Assert.notEmpty(time, "Time ranges must not be null while retrieve application response time trend data.");
        Assert.notNull(period, "Period must be provided while retrieve application response time trend data.");
        Assert.state(period / 60 == 0, "Period must be 60 or the times of 60.");

        final Constaints.MetricName[] metricName = new Constaints.MetricName[] {ERROR_RATE, ERROR, PV};
        final ServiceType[] serviceTypes =
                new ServiceType[] {ServiceType.WEB, ServiceType.DB, ServiceType.EXTERNAL, ServiceType.BACKGROUD};
        List<TimeRange> timeRanges = TimeUtils.convertToRange(time);

        Map<TimeRange, Iterable<ApplicationStatistic>> applicationMetricData =
                overviewService.getApplicationMetricData(appId, timeRanges, period, serviceTypes);

        return MetricUtils.toTrendResponse(applicationMetricData, metricName);
    }

    /**
     * Get Application alarming information.
     *
     * @param appId
     * @param from
     * @param to
     */
    @RequestMapping(value = {"alarm"}, method = RequestMethod.GET)
    public void alarm(@RequestParam(value = "appId") Long appId,
                      @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = Constaints.TIME_PATTERN)
                      LocalDateTime from,
                      @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = Constaints.TIME_PATTERN)
                      LocalDateTime to) {
        //TODO: to be implemented
        throw new UnsupportedOperationException("not supported yet!");
    }

    /**
     * Get Application instances
     *
     * @param appId
     * @param from
     * @param to
     *
     * @return
     */
    @RequestMapping(value = {"instances"}, method = RequestMethod.GET)
    public List<InstanceVo> listInstance(@RequestParam(value = "appId") Long appId,
                                         @RequestParam(value = "from", required = false)
                                         @DateTimeFormat(pattern = Constaints.TIME_PATTERN) LocalDateTime from,
                                         @RequestParam(value = "to", required = false)
                                         @DateTimeFormat(pattern = Constaints.TIME_PATTERN) LocalDateTime to) {

        TimeRange timeRange = TimeUtils.createTimeRange(from, to);
        Application application = overviewService.getApplication(appId);
        Iterable<Instance> appInstances = overviewService.getApplicationInstances(appId);
        Iterable<InstanceStatistic> existInstanceStatistics =
                overviewService.getExistInstanceStatistics(appInstances, timeRange);

        return InstanceUtils.toInstanceVo(application, appInstances, existInstanceStatistics);
    }

    /**
     * Get InstanceVo Response Time trend data
     *
     * @param appId
     * @param time
     * @param period
     */
    @RequestMapping(value = {"instances/trend/rt"}, method = RequestMethod.GET)
    public TrendResponse instanceResponseTime(@RequestParam(value = "appId") Long appId,
                                              @RequestParam(value = "instanceId") Long instanceId,
                                              @RequestParam(value = "time") String[] time,
                                              @RequestParam(value = "period") Integer period) {
        Assert.notNull(appId, "ApplicationId must not be null while retrieve application response time trend data.");
        Assert.notNull(instanceId, "InstanceId must not be null while retrieve application response time trend data.");
        Assert.notEmpty(time, "Time ranges must not be null while retrieve application response time trend data.");
        Assert.notNull(period, "Period must be provided while retrieve application response time trend data.");
        Assert.state(period / 60 == 0, "Period must be 60 or the times of 60.");

        final Constaints.MetricName[] metricName = new Constaints.MetricName[] {RESPONSE_TIME, PV, CPM};
        final ServiceType[] serviceTypes =
                new ServiceType[] {ServiceType.WEB, ServiceType.DB, ServiceType.EXTERNAL, ServiceType.BACKGROUD};
        List<TimeRange> timeRanges = TimeUtils.convertToRange(time);

        Map<TimeRange, Iterable<InstanceStatistic>> applicationMetricData =
                overviewService.getInstanceMetricData(appId, timeRanges, period, serviceTypes);

        return MetricUtils.instanceMetricToTrendResponse(applicationMetricData, metricName);
    }

    /**
     * Get InstanceVo Apdex trend data.
     *
     * @param appId
     * @param time
     * @param period
     *
     * @return
     */
    @RequestMapping(value = {"instances/trend/apdex"}, method = RequestMethod.GET)
    public TrendResponse instanceApdex(@RequestParam(value = "appId") Long appId,
                                       @RequestParam(value = "instanceId") Long instanceId,
                                       @RequestParam(value = "time") String[] time,
                                       @RequestParam(value = "period") Integer period) {

        Assert.notNull(appId, "ApplicationId must not be null while retrieve application response time trend data.");
        Assert.notNull(instanceId, "InstanceId must not be null while retrieve application response time trend data.");
        Assert.notEmpty(time, "Time ranges must not be null while retrieve application response time trend data.");
        Assert.notNull(period, "Period must be provided while retrieve application response time trend data.");
        Assert.state(period / 60 == 0, "Period must be 60 or the times of 60.");

        final Constaints.MetricName[] metricName =
                new Constaints.MetricName[] {APDEX, SATISFIED, TOLERATED, FRUSTRATED};
        final ServiceType[] serviceTypes =
                new ServiceType[] {ServiceType.WEB, ServiceType.DB, ServiceType.EXTERNAL, ServiceType.BACKGROUD};
        List<TimeRange> timeRanges = TimeUtils.convertToRange(time);

        Map<TimeRange, Iterable<InstanceStatistic>> applicationMetricData =
                overviewService.getInstanceMetricData(appId, timeRanges, period, serviceTypes);

        return MetricUtils.instanceMetricToTrendResponse(applicationMetricData, metricName);
    }

    /**
     * Get InstanceVo count per minute trend data.
     *
     * @param appId
     * @param time
     * @param period
     *
     * @return
     */
    @RequestMapping(value = {"instances/trend/cpm"}, method = RequestMethod.GET)
    public TrendResponse instanceCpm(@RequestParam(value = "appId") Long appId,
                                     @RequestParam(value = "instanceId") Long instanceId,
                                     @RequestParam(value = "time") String[] time,
                                     @RequestParam(value = "period") Integer period) {

        Assert.notNull(appId, "ApplicationId must not be null while retrieve application response time trend data.");
        Assert.notNull(instanceId, "InstanceId must not be null while retrieve application response time trend data.");
        Assert.notEmpty(time, "Time ranges must not be null while retrieve application response time trend data.");
        Assert.notNull(period, "Period must be provided while retrieve application response time trend data.");
        Assert.state(period / 60 == 0, "Period must be 60 or the times of 60.");

        final Constaints.MetricName[] metricName = new Constaints.MetricName[] {RESPONSE_TIME, PV, CPM};
        final ServiceType[] serviceTypes =
                new ServiceType[] {ServiceType.WEB, ServiceType.DB, ServiceType.EXTERNAL, ServiceType.BACKGROUD};
        List<TimeRange> timeRanges = TimeUtils.convertToRange(time);

        Map<TimeRange, Iterable<InstanceStatistic>> applicationMetricData =
                overviewService.getInstanceMetricData(appId, timeRanges, period, serviceTypes);

        return MetricUtils.instanceMetricToTrendResponse(applicationMetricData, metricName);
    }

    /**
     * Get InstanceVo WebTransaction limit top n, ordered by average response time.
     *
     * @param appId
     * @param from
     * @param to
     * @param limit
     *
     * @return
     */
    @RequestMapping(value = {"instances/transactions"}, method = RequestMethod.GET)
    public List<Transaction> instanceTransaction(@RequestParam(value = "appId") Long appId,
                                                 @RequestParam(value = "instanceId") Long instanceId,
                                                 @RequestParam(value = "from", required = false)
                                                 @DateTimeFormat(pattern = Constaints.TIME_PATTERN) LocalDateTime from,
                                                 @RequestParam(value = "to", required = false)
                                                 @DateTimeFormat(pattern = Constaints.TIME_PATTERN) LocalDateTime to,
                                                 @RequestParam(value = "limit") Integer limit) {
        //TODO: to be implemented
        return null;
    }

    /**
     * Get Application error Rate trend data.
     *
     * @param appId
     * @param time
     * @param period
     *
     * @return
     */
    @RequestMapping(value = {"instances/trend/errorRate"}, method = RequestMethod.GET)
    public TrendResponse instanceErrorRate(@RequestParam(value = "appId") Long appId,
                                           @RequestParam(value = "instanceId") Long instanceId,
                                           @RequestParam(value = "time") String[] time,
                                           @RequestParam(value = "period") Integer period) {
        Assert.notNull(appId, "ApplicationId must not be null while retrieve application response time trend data.");
        Assert.notEmpty(time, "Time ranges must not be null while retrieve application response time trend data.");
        Assert.notNull(period, "Period must be provided while retrieve application response time trend data.");
        Assert.state(period / 60 == 0, "Period must be 60 or the times of 60.");

        final Constaints.MetricName[] metricName = new Constaints.MetricName[] {ERROR_RATE, ERROR, PV};
        final ServiceType[] serviceTypes =
                new ServiceType[] {ServiceType.WEB, ServiceType.DB, ServiceType.EXTERNAL, ServiceType.BACKGROUD};
        List<TimeRange> timeRanges = TimeUtils.convertToRange(time);

        Map<TimeRange, Iterable<InstanceStatistic>> instanceMetricData =
                overviewService.getInstanceMetricData(instanceId, timeRanges, period, serviceTypes);

        return MetricUtils.instanceMetricToTrendResponse(instanceMetricData, metricName);
    }

    /**
     * Get Application alarming information.
     *
     * @param appId
     * @param from
     * @param to
     */
    @RequestMapping(value = {"instances/alarm"}, method = RequestMethod.GET)
    public void instanceAlarm(@RequestParam(value = "appId") Long appId,
                              @RequestParam(value = "instanceId") Long instanceId,
                              @RequestParam(value = "from", required = false)
                              @DateTimeFormat(pattern = Constaints.TIME_PATTERN) LocalDateTime from,
                              @RequestParam(value = "to", required = false)
                              @DateTimeFormat(pattern = Constaints.TIME_PATTERN) LocalDateTime to) {
        //TODO: to be implemented
        throw new UnsupportedOperationException("not supported yet!");
    }

}
