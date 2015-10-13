package com.baidu.oped.apm.mvc.controller;

import com.baidu.oped.apm.common.jpa.entity.ExternalService;
import com.baidu.oped.apm.common.jpa.entity.ExternalServiceStatistic;
import com.baidu.oped.apm.common.jpa.entity.ServiceType;
import com.baidu.oped.apm.common.utils.Constraints;
import com.baidu.oped.apm.common.utils.Constraints.StatisticMetricValue;
import com.baidu.oped.apm.model.service.AutomaticService;
import com.baidu.oped.apm.mvc.vo.ExternalServiceVo;
import com.baidu.oped.apm.mvc.vo.TimeRange;
import com.baidu.oped.apm.mvc.vo.TrendContext;
import com.baidu.oped.apm.mvc.vo.TrendResponse;
import com.baidu.oped.apm.utils.ExternalServiceUtils;
import com.baidu.oped.apm.utils.TimeUtils;
import com.baidu.oped.apm.utils.TrendUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static com.baidu.oped.apm.common.utils.Constraints.StatisticMetricValue.*;
import static java.lang.String.format;

/**
 * 外部服务接口
 *
 * @author mason(meidongxu@baidu.com)
 */
@RestController
@RequestMapping("external/applications")
public class ExternalServiceController extends BaseController {
    private static final String RETRIEVE_TOP_N_TRANS = "retrieving %s level top n external services";
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
     * List Given application external services.
     *
     * @param appId the application identification
     * @param from  time range begin
     * @param to    time range end
     * @param limit top n limit
     * @return application level top n external service
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<ExternalServiceVo> transactions(@RequestParam(value = "appId") Long appId,
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

        Iterable<ExternalService> externalServices = automaticService.getExternalServicesOfApp(appId);
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
     * @return application level top n
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

        Iterable<ExternalService> transactions = automaticService.getExternalServicesOfApp(appId);

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
     * @return application level cpm
     */
    @RequestMapping(value = {"trend/cpm"})
    public TrendResponse cpm(@RequestParam(value = "appId") Long appId, @RequestParam(value = "time[]") String[] time,
                             @RequestParam(value = "period") Long period) {
        validApplicationId(appId, RETRIEVE_APP_CPM_TREND);
        validTimeRanges(time, RETRIEVE_APP_CPM_TREND);
        validPeriod(period, RETRIEVE_APP_CPM_TREND);

        final StatisticMetricValue[] metricName = new StatisticMetricValue[]{CPM, RESPONSE_TIME, PV};
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
     * @return instance level top n service
     */
    @RequestMapping(value = {"instances"}, method = RequestMethod.GET)
    public List<ExternalServiceVo> instanceTransactions(@RequestParam(value = "appId") Long appId,
                                                        @RequestParam(value = "instanceId") Long instanceId,
                                                        @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = Constraints.TIME_PATTERN)
                                                        LocalDateTime from,
                                                        @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = Constraints.TIME_PATTERN)
                                                        LocalDateTime to, @RequestParam(value = "limit") Integer limit) {
        validApplicationId(appId, RETRIEVE_INSTANCE_TOP_N_TRANS);
        validInstanceId(instanceId, RETRIEVE_INSTANCE_TOP_N_TRANS);
        validTimeRange(from, to, RETRIEVE_INSTANCE_TOP_N_TRANS);
        validLimit(limit, RETRIEVE_INSTANCE_TOP_N_TRANS);

        final Long period = DEFAULT_PERIOD;

        TimeRange timeRange = TimeUtils.createTimeRange(from, to);

        Iterable<ExternalService> externalServices = automaticService.getExternalServicesOfInstance(instanceId);
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
     * @return instance level top n response time
     */
    @RequestMapping(value = {"instances/trend/rt"})
    public TrendResponse instanceResponseTime(@RequestParam(value = "appId") Long appId,
                                              @RequestParam(value = "instanceId") Long instanceId, @RequestParam(value = "time[]") String[] time,
                                              @RequestParam(value = "period") Long period, @RequestParam(value = "limit") Integer limit) {
        validApplicationId(appId, RETRIEVE_INSTANCE_RT_TREND);
        validInstanceId(instanceId, RETRIEVE_INSTANCE_RT_TREND);
        validSingleTimeRanges(time, RETRIEVE_INSTANCE_RT_TREND);
        validPeriod(period, RETRIEVE_INSTANCE_RT_TREND);
        validLimit(limit, RETRIEVE_INSTANCE_RT_TREND);

        final StatisticMetricValue[] metricName = new StatisticMetricValue[]{RESPONSE_TIME, PV, CPM};
        List<TimeRange> timeRanges = TimeUtils.convertToRange(time);
        final TimeRange timeRange = timeRanges.get(0);

        Iterable<ExternalService> transactions = automaticService.getExternalServicesOfInstance(instanceId);

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
     * @return instance level cpm trend data
     */
    @RequestMapping(value = {"instances/trend/cpm"})
    public TrendResponse instanceCpm(@RequestParam(value = "appId") Long appId,
                                     @RequestParam(value = "instanceId") Long instanceId, @RequestParam(value = "time[]") String[] time,
                                     @RequestParam(value = "period") Long period) {
        validApplicationId(appId, RETRIEVE_INSTANCE_CPM_TREND);
        validInstanceId(instanceId, RETRIEVE_INSTANCE_CPM_TREND);
        validTimeRanges(time, RETRIEVE_INSTANCE_CPM_TREND);
        validPeriod(period, RETRIEVE_INSTANCE_CPM_TREND);

        final StatisticMetricValue[] metricName = new StatisticMetricValue[]{CPM, RESPONSE_TIME, PV};
        final ServiceType serviceType = ServiceType.EXTERNAL;
        List<TimeRange> timeRanges = TimeUtils.convertToRange(time);
        TrendContext trendContext =
                automaticService.getMetricDataOfInstance(instanceId, timeRanges, period, serviceType);

        return TrendUtils.toTrendResponse(trendContext, metricName);
    }
}
