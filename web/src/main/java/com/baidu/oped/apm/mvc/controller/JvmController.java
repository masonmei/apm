package com.baidu.oped.apm.mvc.controller;

import static com.baidu.oped.apm.common.utils.Constraints.CommonMetricValue.AVG;
import static com.baidu.oped.apm.common.utils.Constraints.CommonMetricValue.MAX;
import static com.baidu.oped.apm.common.utils.Constraints.CommonMetricValue.MIN;
import static com.baidu.oped.apm.common.utils.Constraints.CpuLoadMetricName.JVM;
import static com.baidu.oped.apm.common.utils.Constraints.CpuLoadMetricName.SYSTEM;
import static com.baidu.oped.apm.common.utils.Constraints.GarbageMetricName.COUNT;
import static com.baidu.oped.apm.common.utils.Constraints.GarbageMetricName.TIME;
import static com.baidu.oped.apm.common.utils.Constraints.JVMMetricName.HEAP_MAX;
import static com.baidu.oped.apm.common.utils.Constraints.JVMMetricName.HEAP_USED;
import static com.baidu.oped.apm.common.utils.Constraints.JVMMetricName.NON_HEAP_MAX;
import static com.baidu.oped.apm.common.utils.Constraints.JVMMetricName.NON_HEAP_USED;
import static java.lang.String.format;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.baidu.oped.apm.common.jpa.entity.AbstractServerStatistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baidu.oped.apm.common.jpa.entity.ApplicationServerStatistic;
import com.baidu.oped.apm.common.jpa.entity.InstanceServerStatistic;
import com.baidu.oped.apm.common.jpa.entity.ServerStatistic;
import com.baidu.oped.apm.common.utils.Constraints.CommonMetricValue;
import com.baidu.oped.apm.common.utils.Constraints.CpuLoadMetricName;
import com.baidu.oped.apm.common.utils.Constraints.GarbageMetricName;
import com.baidu.oped.apm.common.utils.Constraints.JVMMetricName;
import com.baidu.oped.apm.common.utils.Constraints.MetricName;
import com.baidu.oped.apm.model.service.JvmServerStatisticService;
import com.baidu.oped.apm.mvc.vo.ServerTrendContext;
import com.baidu.oped.apm.mvc.vo.TimeRange;
import com.baidu.oped.apm.mvc.vo.TrendResponse;
import com.baidu.oped.apm.utils.TimeUtils;

/**
 * Get the JVM related information.
 *
 * @author mason(meidongxu@baidu.com)
 */
@RestController
@RequestMapping("resource/applications")
public class JvmController extends BaseController {

    private static final String RETRIEVE_TREND = "retrieving %s level %s trend";

    private static final String RETRIEVE_APP_HEAP_TREND = format(RETRIEVE_TREND, "application", "heap");
    private static final String RETRIEVE_INSTANCE_HEAP_TREND = format(RETRIEVE_TREND, "instance", "heap");

    private static final String RETRIEVE_APP_NON_HEAP_TREND = format(RETRIEVE_TREND, "application", "non-heap");
    private static final String RETRIEVE_INSTANCE_NON_HEAP_TREND = format(RETRIEVE_TREND, "instance", "non-heap");

    private static final String RETRIEVE_APP_GARBAGE_TREND = format(RETRIEVE_TREND, "application", "garbage");
    private static final String RETRIEVE_INSTANCE_GARBAGE_TREND = format(RETRIEVE_TREND, "instance", "garbage");

    private static final String RETRIEVE_APP_CPULOAD_TREND = format(RETRIEVE_TREND, "application", "cpuload");
    private static final String RETRIEVE_INSTANCE_CPULOAD_TREND = format(RETRIEVE_TREND, "instance", "cpuload");

    private static final CommonMetricValue[] COMMON_METRIC_VALUES = new CommonMetricValue[]{AVG, MAX, MIN};


    @Autowired
    private JvmServerStatisticService serverStatisticService;

    /**
     * Get the Memory Heap information of the given application.
     *
     * @param appId
     * @param time
     * @param period
     * @return
     */
    @RequestMapping(value = {"heap"})
    public TrendResponse getHeapMemory(@RequestParam(value = "appId") Long appId,
                                       @RequestParam(value = "time[]") String[] time,
                                       @RequestParam(value = "period") Long period) {
        validApplicationId(appId, RETRIEVE_APP_HEAP_TREND);
        validSingleTimeRanges(time, RETRIEVE_APP_HEAP_TREND);
        validPeriod(period, RETRIEVE_APP_HEAP_TREND);

        TimeRange timeRange = TimeUtils.convertToRange(time[0]);

        JVMMetricName[] metricNames = new JVMMetricName[]{HEAP_USED, HEAP_MAX};

        Iterable<ApplicationServerStatistic> appServerStatistics =
                serverStatisticService.getAppServerStatistics(appId, period, timeRange);

        List<Long> metricDataIds = getMetricDataIds(appServerStatistics, metricNames);

        ServerTrendContext<ApplicationServerStatistic> context = buildTrendContext(timeRange, period);
        context.setMetricNames(metricNames);

        context.setMetricDatas(serverStatisticService.getMetricData(metricDataIds));
        context.setServerStatistics(appServerStatistics);

        return context.toResponse();
    }

    public static <T extends AbstractServerStatistic> ServerTrendContext<T> buildTrendContext(
            TimeRange timeRange, Long period) {
        Long periodInMillis = period * 1000;
        ServerTrendContext<T> context = new ServerTrendContext<>(timeRange, periodInMillis);

        context.setMetricValue(COMMON_METRIC_VALUES);
        return context;
    }

    @RequestMapping(value = {"non-heap"})
    public TrendResponse getNonHeapMemory(@RequestParam(value = "appId") Long appId,
                                          @RequestParam(value = "time[]") String[] time,
                                          @RequestParam(value = "period") Long period) {
        validApplicationId(appId, RETRIEVE_APP_NON_HEAP_TREND);
        validSingleTimeRanges(time, RETRIEVE_APP_NON_HEAP_TREND);
        validPeriod(period, RETRIEVE_APP_NON_HEAP_TREND);

        TimeRange timeRange = TimeUtils.convertToRange(time[0]);

        JVMMetricName[] metricNames = new JVMMetricName[]{NON_HEAP_USED, NON_HEAP_MAX};

        Iterable<ApplicationServerStatistic> appServerStatistics =
                serverStatisticService.getAppServerStatistics(appId, period, timeRange);

        List<Long> metricDataIds = getMetricDataIds(appServerStatistics, metricNames);

        ServerTrendContext<ApplicationServerStatistic> context = buildTrendContext(timeRange, period);
        context.setMetricNames(metricNames);
        context.setMetricDatas(serverStatisticService.getMetricData(metricDataIds));
        context.setServerStatistics(appServerStatistics);
        return context.toResponse();
    }

    @RequestMapping(value = {"garbage"})
    public TrendResponse getGarbageCollection(@RequestParam(value = "appId") Long appId,
                                              @RequestParam(value = "time[]") String[] time,
                                              @RequestParam(value = "period") Long period) {
        validApplicationId(appId, RETRIEVE_APP_GARBAGE_TREND);
        validSingleTimeRanges(time, RETRIEVE_APP_GARBAGE_TREND);
        validPeriod(period, RETRIEVE_APP_GARBAGE_TREND);

        TimeRange timeRange = TimeUtils.convertToRange(time[0]);

        CommonMetricValue[] metricValue = new CommonMetricValue[]{AVG};
        GarbageMetricName[] metricNames = new GarbageMetricName[]{COUNT, TIME};

        Iterable<ApplicationServerStatistic> appServerStatistics =
                serverStatisticService.getAppServerStatistics(appId, period, timeRange);

        List<Long> metricDataIds = getMetricDataIds(appServerStatistics, metricNames);

        ServerTrendContext<ApplicationServerStatistic> context = buildTrendContext(timeRange, period);
        context.setMetricNames(metricNames);
        context.setMetricValue(metricValue);
        context.setMetricDatas(serverStatisticService.getMetricData(metricDataIds));
        context.setServerStatistics(appServerStatistics);
        return context.toResponse();
    }

    @RequestMapping(value = {"cpuload"})
    public TrendResponse getCpuLoad(@RequestParam(value = "appId") Long appId,
                                    @RequestParam(value = "time[]") String[] time, @RequestParam(value = "period") Long period) {
        validApplicationId(appId, RETRIEVE_APP_CPULOAD_TREND);
        validSingleTimeRanges(time, RETRIEVE_APP_CPULOAD_TREND);
        validPeriod(period, RETRIEVE_APP_CPULOAD_TREND);

        TimeRange timeRange = TimeUtils.convertToRange(time[0]);

        CpuLoadMetricName[] metricNames = new CpuLoadMetricName[]{JVM, SYSTEM};

        Iterable<ApplicationServerStatistic> appServerStatistics =
                serverStatisticService.getAppServerStatistics(appId, period, timeRange);

        List<Long> metricDataIds = getMetricDataIds(appServerStatistics, metricNames);

        ServerTrendContext<ApplicationServerStatistic> context = buildTrendContext(timeRange, period);
        context.setMetricNames(metricNames);
        context.setMetricDatas(serverStatisticService.getMetricData(metricDataIds));
        context.setServerStatistics(appServerStatistics);

        return context.toResponse();
    }

    @RequestMapping(value = {"instance/heap"})
    public TrendResponse getInstanceHeapMemory(@RequestParam(value = "appId") Long appId,
                                               @RequestParam(value = "instanceId") Long instanceId,
                                               @RequestParam(value = "time[]") String[] time,
                                               @RequestParam(value = "period") Long period) {
        validInstanceId(appId, RETRIEVE_INSTANCE_HEAP_TREND);
        validSingleTimeRanges(time, RETRIEVE_INSTANCE_HEAP_TREND);
        validPeriod(period, RETRIEVE_INSTANCE_HEAP_TREND);

        TimeRange timeRange = TimeUtils.convertToRange(time[0]);

        final JVMMetricName[] metricNames = new JVMMetricName[]{HEAP_USED, HEAP_MAX};

        Iterable<InstanceServerStatistic> instanceServerStatistics =
                serverStatisticService.getInstanceServerStatistics(instanceId, period, timeRange);

        List<Long> metricDataIds = getMetricDataIds(instanceServerStatistics, metricNames);

        ServerTrendContext<InstanceServerStatistic> context = buildTrendContext(timeRange, period * 1000);
        context.setMetricNames(metricNames);
        context.setMetricDatas(serverStatisticService.getMetricData(metricDataIds));
        context.setServerStatistics(instanceServerStatistics);

        return context.toResponse();
    }

    @RequestMapping(value = {"instance/non-heap"})
    public TrendResponse getInstanceNonHeapMemory(@RequestParam(value = "appId") Long appId,
                                                  @RequestParam(value = "instanceId") Long instanceId,
                                                  @RequestParam(value = "time[]") String[] time,
                                                  @RequestParam(value = "period") Long period) {
        validInstanceId(appId, RETRIEVE_INSTANCE_NON_HEAP_TREND);
        validSingleTimeRanges(time, RETRIEVE_INSTANCE_NON_HEAP_TREND);
        validPeriod(period, RETRIEVE_INSTANCE_NON_HEAP_TREND);

        TimeRange timeRange = TimeUtils.convertToRange(time[0]);

        JVMMetricName[] metricNames = new JVMMetricName[]{NON_HEAP_USED, NON_HEAP_MAX};

        Iterable<InstanceServerStatistic> instanceServerStatistics =
                serverStatisticService.getInstanceServerStatistics(instanceId, period, timeRange);

        List<Long> metricDataIds = getMetricDataIds(instanceServerStatistics, metricNames);
        ServerTrendContext<InstanceServerStatistic> context = buildTrendContext(timeRange, period * 1000);
        context.setMetricNames(metricNames);
        context.setMetricDatas(serverStatisticService.getMetricData(metricDataIds));
        context.setServerStatistics(instanceServerStatistics);
        return context.toResponse();
    }

    @RequestMapping(value = {"instance/garbage"})
    public TrendResponse getInstanceGarbageCollection(@RequestParam(value = "appId") Long appId,
                                                      @RequestParam(value = "instanceId") Long instanceId,
                                                      @RequestParam(value = "time[]") String[] time,
                                                      @RequestParam(value = "period") Long period) {
        validInstanceId(appId, RETRIEVE_INSTANCE_GARBAGE_TREND);
        validSingleTimeRanges(time, RETRIEVE_INSTANCE_GARBAGE_TREND);
        validPeriod(period, RETRIEVE_INSTANCE_GARBAGE_TREND);

        TimeRange timeRange = TimeUtils.convertToRange(time[0]);

        CommonMetricValue[] metricValue = new CommonMetricValue[]{AVG};
        GarbageMetricName[] metricNames = new GarbageMetricName[]{COUNT, TIME};

        Iterable<InstanceServerStatistic> instanceServerStatistics =
                serverStatisticService.getInstanceServerStatistics(instanceId, period, timeRange);

        List<Long> metricDataIds = getMetricDataIds(instanceServerStatistics, metricNames);

        ServerTrendContext<InstanceServerStatistic> context = buildTrendContext(timeRange, period * 1000);
        context.setMetricNames(metricNames);
        context.setMetricValue(metricValue);
        context.setMetricDatas(serverStatisticService.getMetricData(metricDataIds));
        context.setServerStatistics(instanceServerStatistics);
        return context.toResponse();
    }

    @RequestMapping(value = {"instance/cpuload"})
    public TrendResponse getInstanceCpuLoad(@RequestParam(value = "appId") Long appId,
                                            @RequestParam(value = "instanceId") Long instanceId,
                                            @RequestParam(value = "time[]") String[] time,
                                            @RequestParam(value = "period") Long period) {
        validInstanceId(appId, RETRIEVE_INSTANCE_CPULOAD_TREND);
        validSingleTimeRanges(time, RETRIEVE_INSTANCE_CPULOAD_TREND);
        validPeriod(period, RETRIEVE_INSTANCE_CPULOAD_TREND);

        TimeRange timeRange = TimeUtils.convertToRange(time[0]);

        CpuLoadMetricName[] metricNames = new CpuLoadMetricName[]{JVM, SYSTEM};

        Iterable<InstanceServerStatistic> instanceServerStatistics =
                serverStatisticService.getInstanceServerStatistics(instanceId, period, timeRange);

        List<Long> metricDataIds = getMetricDataIds(instanceServerStatistics, metricNames);

        ServerTrendContext<InstanceServerStatistic> context = buildTrendContext(timeRange, period * 1000);
        context.setMetricNames(metricNames);
        context.setMetricDatas(serverStatisticService.getMetricData(metricDataIds));
        context.setServerStatistics(instanceServerStatistics);

        return context.toResponse();
    }

    private <T extends ServerStatistic> List<Long> getMetricDataIds(Iterable<T> serverStatistics,
                                                                    MetricName metricName) {
        if (metricName instanceof JVMMetricName) {
            JVMMetricName jvmMetricName = (JVMMetricName) metricName;

            switch (jvmMetricName) {
                case HEAP_INIT:
                    throw new UnsupportedOperationException("Not support yet!");
                case HEAP_USED:
                    return StreamSupport.stream(serverStatistics.spliterator(), false)
                            .map(ServerStatistic::getHeapUsedMetric).collect(Collectors.toList());
                case HEAP_MAX:
                    return StreamSupport.stream(serverStatistics.spliterator(), false)
                            .map(ServerStatistic::getHeapMaxMetric).collect(Collectors.toList());
                case HEAP_COMMITTED:
                    throw new UnsupportedOperationException("Not support yet!");
                case NON_HEAP_INIT:
                    throw new UnsupportedOperationException("Not support yet!");
                case NON_HEAP_USED:
                    return StreamSupport.stream(serverStatistics.spliterator(), false)
                            .map(ServerStatistic::getNonHeapUsedMetric).collect(Collectors.toList());
                case NON_HEAP_MAX:
                    return StreamSupport.stream(serverStatistics.spliterator(), false)
                            .map(ServerStatistic::getNonHeapMaxMetric).collect(Collectors.toList());
                case NON_HEAP_COMMITTED:
                    throw new UnsupportedOperationException("Not support yet!");
                case TOTAL_INIT:
                    throw new UnsupportedOperationException("Not support yet!");
                case TOTAL_USED:
                    throw new UnsupportedOperationException("Not support yet!");
                case TOTAL_MAX:
                    throw new UnsupportedOperationException("Not support yet!");
                case TOTAL_COMMITTED:
                    throw new UnsupportedOperationException("Not support yet!");
                default:
                    throw new UnsupportedOperationException("Not support yet!");
            }
        } else if (metricName instanceof CpuLoadMetricName) {
            CpuLoadMetricName cpuLoadMetricName = (CpuLoadMetricName) metricName;

            switch (cpuLoadMetricName) {
                case JVM:
                    return StreamSupport.stream(serverStatistics.spliterator(), false)
                            .map(ServerStatistic::getJvmCpuMetric).collect(Collectors.toList());
                case SYSTEM:
                    return StreamSupport.stream(serverStatistics.spliterator(), false)
                            .map(ServerStatistic::getSystemCpuMetric).collect(Collectors.toList());
                default:
                    throw new UnsupportedOperationException("Not support yet!");
            }
        } else if (metricName instanceof GarbageMetricName) {
            GarbageMetricName garbageMetricName = (GarbageMetricName) metricName;

            switch (garbageMetricName) {
                case COUNT:
                    return StreamSupport.stream(serverStatistics.spliterator(), false)
                            .map(ServerStatistic::getGcOldCountMetric).collect(Collectors.toList());
                case TIME:
                    return StreamSupport.stream(serverStatistics.spliterator(), false)
                            .map(ServerStatistic::getGcOldTimeMetric).collect(Collectors.toList());
                default:
                    throw new UnsupportedOperationException("Not support yet!");
            }
        }

        throw new UnsupportedOperationException("Not support yet!");
    }

    private <T extends ServerStatistic> List<Long> getMetricDataIds(Iterable<T> serverStatistics,
                                                                    MetricName[] metricName) {
        List<Long> ids = new ArrayList<>();
        for (MetricName name : metricName) {
            ids.addAll(getMetricDataIds(serverStatistics, name));
        }
        return ids;
    }
}
