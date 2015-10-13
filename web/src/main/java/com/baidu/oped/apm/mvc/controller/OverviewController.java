package com.baidu.oped.apm.mvc.controller;

import static com.baidu.oped.apm.common.utils.Constraints.StatisticMetricValue.APDEX;
import static com.baidu.oped.apm.common.utils.Constraints.StatisticMetricValue.CPM;
import static com.baidu.oped.apm.common.utils.Constraints.StatisticMetricValue.ERROR;
import static com.baidu.oped.apm.common.utils.Constraints.StatisticMetricValue.ERROR_RATE;
import static com.baidu.oped.apm.common.utils.Constraints.StatisticMetricValue.FRUSTRATED;
import static com.baidu.oped.apm.common.utils.Constraints.StatisticMetricValue.PV;
import static com.baidu.oped.apm.common.utils.Constraints.StatisticMetricValue.RESPONSE_TIME;
import static com.baidu.oped.apm.common.utils.Constraints.StatisticMetricValue.SATISFIED;
import static com.baidu.oped.apm.common.utils.Constraints.StatisticMetricValue.TOLERATED;
import static java.lang.String.format;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baidu.oped.apm.common.jpa.entity.Application;
import com.baidu.oped.apm.common.jpa.entity.Instance;
import com.baidu.oped.apm.common.jpa.entity.InstanceStatistic;
import com.baidu.oped.apm.common.jpa.entity.ServiceType;
import com.baidu.oped.apm.common.utils.Constraints;
import com.baidu.oped.apm.common.utils.Constraints.StatisticMetricValue;
import com.baidu.oped.apm.model.service.AutomaticService;
import com.baidu.oped.apm.model.service.OverviewService;
import com.baidu.oped.apm.mvc.vo.InstanceVo;
import com.baidu.oped.apm.mvc.vo.TimeRange;
import com.baidu.oped.apm.mvc.vo.TransactionVo;
import com.baidu.oped.apm.mvc.vo.TrendContext;
import com.baidu.oped.apm.mvc.vo.TrendResponse;
import com.baidu.oped.apm.utils.InstanceUtils;
import com.baidu.oped.apm.utils.TimeUtils;
import com.baidu.oped.apm.utils.TrendUtils;

/**
 * Created by mason on 8/25/15.
 */
@RestController
@RequestMapping("overview/applications")
public class OverviewController extends BaseController {

    private static final String RETRIEVE_TOP_N_TRANS = "retrieving %s level top n transaction";
    private static final String RETRIEVE_RT_TREND = "retrieving %s level response time trend";
    private static final String RETRIEVE_APDEX_TREND = "retrieving %s level apdex trend";
    private static final String RETRIEVE_CPM_TREND = "retrieving %s level cpm trend";
    private static final String RETRIEVE_ERROR_TREND = "retrieving %s level errorRate trend";

    private static final String RETRIEVE_INSTANCE = "retrieving %s instances";

    private static final String RETRIEVE_APP_TOP_N_TRANS = format(RETRIEVE_TOP_N_TRANS, "application");
    private static final String RETRIEVE_APP_RT_TREND = format(RETRIEVE_RT_TREND, "application");
    private static final String RETRIEVE_APP_APDEX_TREND = format(RETRIEVE_APDEX_TREND, "application");
    private static final String RETRIEVE_APP_CPM_TREND = format(RETRIEVE_CPM_TREND, "application");
    private static final String RETRIEVE_APP_ERROR_TREND = format(RETRIEVE_ERROR_TREND, "application");
    private static final String RETRIEVE_APP_INSTANCE = format(RETRIEVE_INSTANCE, "application");

    private static final String RETRIEVE_INSTANCE_TOP_N_TRANS = format(RETRIEVE_TOP_N_TRANS, "instance");
    private static final String RETRIEVE_INSTANCE_RT_TREND = format(RETRIEVE_RT_TREND, "instance");
    private static final String RETRIEVE_INSTANCE_APDEX_TREND = format(RETRIEVE_APDEX_TREND, "instance");
    private static final String RETRIEVE_INSTANCE_CPM_TREND = format(RETRIEVE_CPM_TREND, "instance");
    private static final String RETRIEVE_INSTANCE_ERROR_TREND = format(RETRIEVE_ERROR_TREND, "instance");

    @Autowired
    private OverviewService overviewService;

    @Autowired
    private AutomaticService automaticService;

    /**
     * Get Application Response Time trend data
     *
     * @param appId
     * @param time
     * @param period
     */
    @RequestMapping(value = {"trend/rt"}, method = RequestMethod.GET)
    public TrendResponse responseTime(@RequestParam(value = "appId") Long appId,
                                      @RequestParam(value = "time[]") String[] time,
                                      @RequestParam(value = "period") Long period) {
        validApplicationId(appId, RETRIEVE_APP_RT_TREND);
        validTimeRanges(time, RETRIEVE_APP_RT_TREND);
        validPeriod(period, RETRIEVE_APP_RT_TREND);

        final StatisticMetricValue[] metricName = new StatisticMetricValue[]{RESPONSE_TIME, PV, CPM};
        final ServiceType[] serviceTypes = new ServiceType[]{ServiceType.WEB, ServiceType.DB, ServiceType.EXTERNAL};
        List<TimeRange> timeRanges = TimeUtils.convertToRange(time);
        TrendContext metricDataOfApp = automaticService.getMetricDataOfApp(appId, timeRanges, period, serviceTypes);

        return TrendUtils.toTrendResponse(metricDataOfApp, metricName);
    }

    /**
     * Get Application Apdex trend data.
     *
     * @param appId
     * @param time
     * @param period
     * @return
     */
    @RequestMapping(value = {"trend/apdex"}, method = RequestMethod.GET)
    public TrendResponse apdex(@RequestParam(value = "appId") Long appId,
                               @RequestParam(value = "time[]") String[] time,
                               @RequestParam(value = "period") Long period) {
        validApplicationId(appId, RETRIEVE_APP_APDEX_TREND);
        validTimeRanges(time, RETRIEVE_APP_APDEX_TREND);
        validPeriod(period, RETRIEVE_APP_APDEX_TREND);

        final StatisticMetricValue[] metricName = new StatisticMetricValue[]{APDEX, SATISFIED, TOLERATED, FRUSTRATED};
        final ServiceType[] serviceTypes = new ServiceType[]{ServiceType.WEB};
        List<TimeRange> timeRanges = TimeUtils.convertToRange(time);
        TrendContext metricDataOfApp = automaticService.getMetricDataOfApp(appId, timeRanges, period, serviceTypes);

        return TrendUtils.toTrendResponse(metricDataOfApp, metricName);
    }

    /**
     * Get Application count per minute trend data.
     *
     * @param appId
     * @param time
     * @param period
     * @return
     */
    @RequestMapping(value = {"trend/cpm"}, method = RequestMethod.GET)
    public TrendResponse cpm(@RequestParam(value = "appId") Long appId,
                             @RequestParam(value = "time[]") String[] time,
                             @RequestParam(value = "period") Long period) {
        validApplicationId(appId, RETRIEVE_APP_CPM_TREND);
        validTimeRanges(time, RETRIEVE_APP_CPM_TREND);
        validPeriod(period, RETRIEVE_APP_CPM_TREND);

        final StatisticMetricValue[] metricName = new StatisticMetricValue[]{CPM, RESPONSE_TIME, PV};
        final ServiceType[] serviceTypes = new ServiceType[]{ServiceType.WEB, ServiceType.EXTERNAL};
        List<TimeRange> timeRanges = TimeUtils.convertToRange(time);
        TrendContext metricDataOfApp = automaticService.getMetricDataOfApp(appId, timeRanges, period, serviceTypes);

        return TrendUtils.toTrendResponse(metricDataOfApp, metricName);
    }

    /**
     * Get Application WebTransaction limit top n, ordered by average response time desc.
     *
     * @param appId
     * @param from
     * @param to
     * @param limit
     * @return
     */
    @RequestMapping(value = {"transactions"}, method = RequestMethod.GET)
    public List<TransactionVo> transaction(@RequestParam(value = "appId") Long appId,
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
     * Get Application error Rate trend data.
     *
     * @param appId
     * @param time
     * @param period
     * @return
     */
    @RequestMapping(value = {"trend/errorRate"}, method = RequestMethod.GET)
    public TrendResponse errorRate(@RequestParam(value = "appId") Long appId,
                                   @RequestParam(value = "time[]") String[] time,
                                   @RequestParam(value = "period") Long period) {
        validApplicationId(appId, RETRIEVE_APP_ERROR_TREND);
        validTimeRanges(time, RETRIEVE_APP_ERROR_TREND);
        validPeriod(period, RETRIEVE_APP_ERROR_TREND);

        final StatisticMetricValue[] metricName = new StatisticMetricValue[]{ERROR_RATE, ERROR, PV};
        final ServiceType[] serviceTypes = new ServiceType[]{ServiceType.WEB, ServiceType.DB, ServiceType.EXTERNAL};
        List<TimeRange> timeRanges = TimeUtils.convertToRange(time);

        TrendContext metricDataOfApp = automaticService.getMetricDataOfApp(appId, timeRanges, period, serviceTypes);

        return TrendUtils.toTrendResponse(metricDataOfApp, metricName);
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
                      @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = Constraints.TIME_PATTERN)
                      LocalDateTime from,
                      @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = Constraints.TIME_PATTERN)
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
     * @return
     */
    @RequestMapping(value = {"instances"}, method = RequestMethod.GET)
    public List<InstanceVo> listInstance(@RequestParam(value = "appId") Long appId,
                                         @RequestParam(value = "from", required = false)
                                         @DateTimeFormat(pattern = Constraints.TIME_PATTERN)
                                         LocalDateTime from,
                                         @RequestParam(value = "to", required = false)
                                         @DateTimeFormat(pattern = Constraints.TIME_PATTERN)
                                         LocalDateTime to) {
        validApplicationId(appId, RETRIEVE_APP_INSTANCE);
        validTimeRange(from, to, RETRIEVE_APP_INSTANCE);

        TimeRange timeRange = TimeUtils.createTimeRange(from, to);
        Application application = automaticService.getApplication(appId);
        Iterable<Instance> appInstances = automaticService.getApplicationInstances(appId);
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
                                              @RequestParam(value = "instanceId") Long instanceId, @RequestParam(value = "time[]") String[] time,
                                              @RequestParam(value = "period") Long period) {
        validApplicationId(appId, RETRIEVE_INSTANCE_RT_TREND);
        validInstanceId(instanceId, RETRIEVE_INSTANCE_RT_TREND);
        validTimeRanges(time, RETRIEVE_INSTANCE_RT_TREND);
        validPeriod(period, RETRIEVE_INSTANCE_RT_TREND);

        final StatisticMetricValue[] metricName = new StatisticMetricValue[]{RESPONSE_TIME, PV, CPM};
        final ServiceType[] serviceTypes = new ServiceType[]{ServiceType.WEB, ServiceType.DB, ServiceType.EXTERNAL};
        List<TimeRange> timeRanges = TimeUtils.convertToRange(time);

        TrendContext metricDataOfInstance =
                automaticService.getMetricDataOfInstance(instanceId, timeRanges, period, serviceTypes);

        return TrendUtils.toTrendResponse(metricDataOfInstance, metricName);
    }

    /**
     * Get InstanceVo Apdex trend data.
     *
     * @param appId
     * @param time
     * @param period
     * @return
     */
    @RequestMapping(value = {"instances/trend/apdex"}, method = RequestMethod.GET)
    public TrendResponse instanceApdex(@RequestParam(value = "appId") Long appId,
                                       @RequestParam(value = "instanceId") Long instanceId,
                                       @RequestParam(value = "time[]") String[] time,
                                       @RequestParam(value = "period") Long period) {
        validApplicationId(appId, RETRIEVE_INSTANCE_APDEX_TREND);
        validInstanceId(instanceId, RETRIEVE_INSTANCE_APDEX_TREND);
        validTimeRanges(time, RETRIEVE_INSTANCE_APDEX_TREND);
        validPeriod(period, RETRIEVE_INSTANCE_APDEX_TREND);

        final StatisticMetricValue[] metricName = new StatisticMetricValue[]{APDEX, SATISFIED, TOLERATED, FRUSTRATED};
        final ServiceType[] serviceTypes = new ServiceType[]{ServiceType.WEB};
        List<TimeRange> timeRanges = TimeUtils.convertToRange(time);

        TrendContext metricDataOfInstance =
                automaticService.getMetricDataOfInstance(instanceId, timeRanges, period, serviceTypes);

        return TrendUtils.toTrendResponse(metricDataOfInstance, metricName);
    }

    /**
     * Get InstanceVo count per minute trend data.
     *
     * @param appId
     * @param time
     * @param period
     * @return
     */
    @RequestMapping(value = {"instances/trend/cpm"}, method = RequestMethod.GET)
    public TrendResponse instanceCpm(@RequestParam(value = "appId") Long appId,
                                     @RequestParam(value = "instanceId") Long instanceId,
                                     @RequestParam(value = "time[]") String[] time,
                                     @RequestParam(value = "period") Long period) {
        validApplicationId(appId, RETRIEVE_INSTANCE_CPM_TREND);
        validInstanceId(instanceId, RETRIEVE_INSTANCE_CPM_TREND);
        validTimeRanges(time, RETRIEVE_INSTANCE_CPM_TREND);
        validPeriod(period, RETRIEVE_INSTANCE_CPM_TREND);

        final StatisticMetricValue[] metricName = new StatisticMetricValue[]{CPM, RESPONSE_TIME, PV};
        final ServiceType[] serviceTypes = new ServiceType[]{ServiceType.WEB, ServiceType.EXTERNAL};
        List<TimeRange> timeRanges = TimeUtils.convertToRange(time);

        TrendContext metricDataOfInstance =
                automaticService.getMetricDataOfInstance(instanceId, timeRanges, period, serviceTypes);

        return TrendUtils.toTrendResponse(metricDataOfInstance, metricName);
    }

    /**
     * Get InstanceVo WebTransaction limit top n, ordered by average response time.
     *
     * @param appId
     * @param from
     * @param to
     * @param limit
     * @return
     */
    @RequestMapping(value = {"instances/transactions"}, method = RequestMethod.GET)
    public List<TransactionVo> instanceTransaction(@RequestParam(value = "appId") Long appId,
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
     * Get Application error Rate trend data.
     *
     * @param appId
     * @param time
     * @param period
     * @return
     */
    @RequestMapping(value = {"instances/trend/errorRate"}, method = RequestMethod.GET)
    public TrendResponse instanceErrorRate(@RequestParam(value = "appId") Long appId,
                                           @RequestParam(value = "instanceId") Long instanceId,
                                           @RequestParam(value = "time[]") String[] time,
                                           @RequestParam(value = "period") Long period) {
        validApplicationId(appId, RETRIEVE_INSTANCE_ERROR_TREND);
        validInstanceId(instanceId, RETRIEVE_INSTANCE_ERROR_TREND);
        validPeriod(period, RETRIEVE_INSTANCE_ERROR_TREND);

        final StatisticMetricValue[] metricName = new StatisticMetricValue[]{ERROR_RATE, ERROR, PV};
        final ServiceType[] serviceTypes = new ServiceType[]{ServiceType.WEB, ServiceType.DB, ServiceType.EXTERNAL};
        List<TimeRange> timeRanges = TimeUtils.convertToRange(time);

        TrendContext metricDataOfInstance =
                automaticService.getMetricDataOfInstance(instanceId, timeRanges, period, serviceTypes);

        return TrendUtils.toTrendResponse(metricDataOfInstance, metricName);
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
                              @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = Constraints.TIME_PATTERN)
                              LocalDateTime from,
                              @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = Constraints.TIME_PATTERN)
                              LocalDateTime to) {
        //TODO: to be implemented
        throw new UnsupportedOperationException("not supported yet!");
    }

}
