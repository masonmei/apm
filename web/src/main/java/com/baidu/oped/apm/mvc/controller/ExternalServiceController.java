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

import com.baidu.oped.apm.common.jpa.entity.ExternalService;
import com.baidu.oped.apm.common.jpa.entity.ExternalServiceStatistic;
import com.baidu.oped.apm.common.jpa.entity.ServiceType;
import com.baidu.oped.apm.common.utils.Constraints;
import com.baidu.oped.apm.model.service.AutomaticService;
import com.baidu.oped.apm.mvc.vo.ExternalServiceVo;
import com.baidu.oped.apm.mvc.vo.TimeRange;
import com.baidu.oped.apm.mvc.vo.TrendContext;
import com.baidu.oped.apm.mvc.vo.TrendResponse;
import com.baidu.oped.apm.utils.ExternalServiceUtils;
import com.baidu.oped.apm.utils.TimeUtils;
import com.baidu.oped.apm.utils.TrendUtils;

/**
 * Created by mason on 8/13/15.
 */
@RestController
@RequestMapping("external/applications")
public class ExternalServiceController {

    @Autowired
    private AutomaticService automaticService;

    /**
     * List Given application external services.
     *
     * @param appId the application identification
     * @param from  time range begin
     * @param to    time range end
     * @param limit top n limit
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<ExternalServiceVo> transactions(@RequestParam(value = "appId") Long appId,
            @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = Constraints.TIME_PATTERN)
            LocalDateTime from,
            @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = Constraints.TIME_PATTERN)
            LocalDateTime to, @RequestParam(value = "limit") Integer limit) {
        Assert.notNull(appId, "ApplicationId must not be null while retrieving top n transactions.");
        Assert.notNull(from, "Time range start must not be null while retrieving top n transactions.");
        Assert.notNull(to, "Time range end must not be null while retrieving top n transactions.");
        Assert.state(from.isBefore(to), "Time range start must before than end.");
        Assert.state(limit > 0, "The limit must be greater than 0.");

        final Long period = 60l;

        TimeRange timeRange = TimeUtils.createTimeRange(from, to);

        Iterable<ExternalService> externalServices = automaticService.getExternalServicesWithAppId(appId);
        Iterable<ExternalServiceStatistic> statistic =
                automaticService.getExternalServiceStatistic(externalServices, timeRange, period);

        return ExternalServiceUtils.topByAvgResponseTime(statistic, externalServices, timeRange, limit);
    }

    /**
     * Get top n external service response time trend data.
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

        Iterable<ExternalService> transactions = automaticService.getExternalServicesWithAppId(appId);

        Iterable<ExternalServiceStatistic> transactionsStatistic =
                automaticService.getExternalServiceStatistic(transactions, timeRange, period);

        TrendContext<String> trendContext = ExternalServiceUtils
                .topByAvgResponseTime(period, limit, timeRange, transactions, transactionsStatistic);

        return TrendUtils.toTrendResponse(trendContext, metricName);
    }

    /**
     * Get all external service cpm of the application.
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

        final Constraints.MetricName[] metricName = new Constraints.MetricName[] {CPM, RESPONSE_TIME, PV};
        final ServiceType serviceType = ServiceType.EXTERNAL;
        List<TimeRange> timeRanges = TimeUtils.convertToRange(time);

        TrendContext trendContext = automaticService.getMetricDataOfApp(appId, timeRanges, period, serviceType);

        return TrendUtils.toTrendResponse(trendContext, metricName);
    }

    /**
     * List external service of given instance.
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
    public List<ExternalServiceVo> instanceTransactions(@RequestParam(value = "appId") Long appId,
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
        final Long period = 60l;

        TimeRange timeRange = TimeUtils.createTimeRange(from, to);

        Iterable<ExternalService> externalServices = automaticService.getExternalServicesWithInstanceId(instanceId);
        Iterable<ExternalServiceStatistic> statistic =
                automaticService.getExternalServiceStatistic(externalServices, timeRange, period);

        return ExternalServiceUtils.topByAvgResponseTime(statistic, externalServices, timeRange, limit);
    }

    /**
     * Get top n external service response time trend data of given instance.
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

        Iterable<ExternalService> transactions = automaticService.getExternalServicesWithInstanceId(instanceId);

        Iterable<ExternalServiceStatistic> transactionsStatistic =
                automaticService.getExternalServiceStatistic(transactions, timeRange, period);

        TrendContext<String> trendContext = ExternalServiceUtils
                .topByAvgResponseTime(period, limit, timeRange, transactions, transactionsStatistic);

        return TrendUtils.toTrendResponse(trendContext, metricName);
    }

    /**
     * Get all external service cpm of given instance.
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

        final Constraints.MetricName[] metricName = new Constraints.MetricName[] {CPM, RESPONSE_TIME, PV};
        final ServiceType serviceType = ServiceType.EXTERNAL;
        List<TimeRange> timeRanges = TimeUtils.convertToRange(time);
        TrendContext trendContext =
                automaticService.getMetricDataOfInstance(instanceId, timeRanges, period, serviceType);

        return TrendUtils.toTrendResponse(trendContext, metricName);
    }
}
