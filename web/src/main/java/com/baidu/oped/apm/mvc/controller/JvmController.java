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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
public class JvmController {

    @Autowired
    private JvmServerStatisticService serverStatisticService;

    /**
     * Get the Memory Heap information of the given application.
     *
     * @param appId
     * @param time
     * @param period
     *
     * @return
     */
    @RequestMapping(value = {"heap"})
    public TrendResponse getHeapMemory(@RequestParam(value = "appId") Long appId,
            @RequestParam(value = "time[]") String[] time, @RequestParam(value = "period") Long period) {
        Assert.notNull(appId, "ApplicationId must not be null while retrieving heap trend data");
        Assert.notEmpty(time, "Time ranges must not be null while retrieving heap trend data");
        Assert.notNull(period, "Period must be provided while retrieving heap trend data");
        Assert.state(period % 60 == 0, "Period must be 60 or the times of 60.");
        Assert.state(time.length == 1, "Only One TimeRange required.");

        TimeRange timeRange = TimeUtils.convertToRange(time[0]);

        CommonMetricValue[] metricValue = new CommonMetricValue[] {AVG, MAX, MIN};
        JVMMetricName[] metricNames = new JVMMetricName[] {HEAP_USED, HEAP_MAX};

        Iterable<ApplicationServerStatistic> appServerStatistics =
                serverStatisticService.getAppServerStatistics(appId, period, timeRange);

        List<Long> metricDataIds = getMetricDataIds(appServerStatistics, metricNames);

        ServerTrendContext context = new ServerTrendContext(timeRange, period * 1000);
        context.setMetricNames(metricNames);
        context.setMetricValue(metricValue);
        context.setMetricDatas(serverStatisticService.getMetricData(metricDataIds));
        context.setServerStatistics(appServerStatistics);

        return context.toResponse();
    }

    @RequestMapping(value = {"non-heap"})
    public TrendResponse getNonHeapMemory(@RequestParam(value = "appId") Long appId,
            @RequestParam(value = "time[]") String[] time, @RequestParam(value = "period") Long period) {
        Assert.notNull(appId, "ApplicationId must not be null while retrieving non-heap trend data");
        Assert.notEmpty(time, "Time ranges must not be null while retrieving non-heap trend data");
        Assert.notNull(period, "Period must be provided while retrieving non-heap trend data");
        Assert.state(period % 60 == 0, "Period must be 60 or the times of 60.");
        Assert.state(time.length == 1, "Only One TimeRange required.");

        TimeRange timeRange = TimeUtils.convertToRange(time[0]);

        CommonMetricValue[] metricValue = new CommonMetricValue[] {AVG, MAX, MIN};
        JVMMetricName[] metricNames = new JVMMetricName[] {NON_HEAP_USED, NON_HEAP_MAX};

        Iterable<ApplicationServerStatistic> appServerStatistics =
                serverStatisticService.getAppServerStatistics(appId, period, timeRange);

        List<Long> metricDataIds = getMetricDataIds(appServerStatistics, metricNames);

        ServerTrendContext context = new ServerTrendContext(timeRange, period * 1000);
        context.setMetricNames(metricNames);
        context.setMetricValue(metricValue);
        context.setMetricDatas(serverStatisticService.getMetricData(metricDataIds));
        context.setServerStatistics(appServerStatistics);
        return context.toResponse();
    }

    @RequestMapping(value = {"garbage"})
    public TrendResponse getGarbageCollection(@RequestParam(value = "appId") Long appId,
            @RequestParam(value = "time[]") String[] time, @RequestParam(value = "period") Long period) {
        Assert.notNull(appId, "ApplicationId must not be null while retrieving garbage trend data");
        Assert.notEmpty(time, "Time ranges must not be null while retrieving garbage trend data");
        Assert.notNull(period, "Period must be provided while retrieving garbage trend data");
        Assert.state(period % 60 == 0, "Period must be 60 or the times of 60.");
        Assert.state(time.length == 1, "Only One TimeRange required.");

        TimeRange timeRange = TimeUtils.convertToRange(time[0]);

        CommonMetricValue[] metricValue = new CommonMetricValue[] {AVG};
        GarbageMetricName[] metricNames = new GarbageMetricName[] {COUNT, TIME};

        Iterable<ApplicationServerStatistic> appServerStatistics =
                serverStatisticService.getAppServerStatistics(appId, period, timeRange);

        List<Long> metricDataIds = getMetricDataIds(appServerStatistics, metricNames);

        ServerTrendContext context = new ServerTrendContext(timeRange, period * 1000);
        context.setMetricNames(metricNames);
        context.setMetricValue(metricValue);
        context.setMetricDatas(serverStatisticService.getMetricData(metricDataIds));
        context.setServerStatistics(appServerStatistics);
        return context.toResponse();
    }

    @RequestMapping(value = {"cpuload"})
    public TrendResponse getCpuLoad(@RequestParam(value = "appId") Long appId,
            @RequestParam(value = "time[]") String[] time, @RequestParam(value = "period") Long period) {
        Assert.notNull(appId, "ApplicationId must not be null while retrieving cpuload trend data");
        Assert.notEmpty(time, "Time ranges must not be null while retrieving cpuload trend data");
        Assert.notNull(period, "Period must be provided while retrieving cpuload trend data");
        Assert.state(period % 60 == 0, "Period must be 60 or the times of 60.");
        Assert.state(time.length == 1, "Only One TimeRange required.");

        TimeRange timeRange = TimeUtils.convertToRange(time[0]);

        CommonMetricValue[] metricValue = new CommonMetricValue[] {AVG, MAX, MIN};
        CpuLoadMetricName[] metricNames = new CpuLoadMetricName[] {JVM, SYSTEM};

        Iterable<ApplicationServerStatistic> appServerStatistics =
                serverStatisticService.getAppServerStatistics(appId, period, timeRange);

        List<Long> metricDataIds = getMetricDataIds(appServerStatistics, metricNames);

        ServerTrendContext context = new ServerTrendContext(timeRange, period * 1000);
        context.setMetricNames(metricNames);
        context.setMetricValue(metricValue);
        context.setMetricDatas(serverStatisticService.getMetricData(metricDataIds));
        context.setServerStatistics(appServerStatistics);

        return context.toResponse();
    }

    @RequestMapping(value = {"instance/heap"})
    public TrendResponse getInstanceHeapMemory(@RequestParam(value = "appId") Long appId,
            @RequestParam(value = "instanceId") Long instanceId, @RequestParam(value = "time[]") String[] time,
            @RequestParam(value = "period") Long period) {
        Assert.notNull(appId, "ApplicationId must not be null while retrieving heap trend data");
        Assert.notNull(instanceId, "InstanceId must not be null while retrieving cpuload trend data");
        Assert.notEmpty(time, "Time ranges must not be null while retrieving heap trend data");
        Assert.notNull(period, "Period must be provided while retrieving heap trend data");
        Assert.state(period % 60 == 0, "Period must be 60 or the times of 60.");
        Assert.state(time.length == 1, "Only One TimeRange required.");

        TimeRange timeRange = TimeUtils.convertToRange(time[0]);

        CommonMetricValue[] metricValue = new CommonMetricValue[] {AVG, MAX, MIN};
        JVMMetricName[] metricNames = new JVMMetricName[] {HEAP_USED, HEAP_MAX};

        Iterable<InstanceServerStatistic> instanceServerStatistics =
                serverStatisticService.getInstanceServerStatistics(instanceId, period, timeRange);

        List<Long> metricDataIds = getMetricDataIds(instanceServerStatistics, metricNames);

        ServerTrendContext context = new ServerTrendContext(timeRange, period * 1000);
        context.setMetricNames(metricNames);
        context.setMetricValue(metricValue);
        context.setMetricDatas(serverStatisticService.getMetricData(metricDataIds));
        context.setServerStatistics(instanceServerStatistics);

        return context.toResponse();
    }

    @RequestMapping(value = {"instance/non-heap"})
    public TrendResponse getInstanceNonHeapMemory(@RequestParam(value = "appId") Long appId,
            @RequestParam(value = "instanceId") Long instanceId, @RequestParam(value = "time[]") String[] time,
            @RequestParam(value = "period") Long period) {
        Assert.notNull(appId, "ApplicationId must not be null while retrieving non-heap trend data");
        Assert.notNull(instanceId, "InstanceId must not be null while retrieving cpuload trend data");
        Assert.notEmpty(time, "Time ranges must not be null while retrieving non-heap trend data");
        Assert.notNull(period, "Period must be provided while retrieving non-heap trend data");
        Assert.state(period % 60 == 0, "Period must be 60 or the times of 60.");
        Assert.state(time.length == 1, "Only One TimeRange required.");

        TimeRange timeRange = TimeUtils.convertToRange(time[0]);

        CommonMetricValue[] metricValue = new CommonMetricValue[] {AVG, MAX, MIN};
        JVMMetricName[] metricNames = new JVMMetricName[] {NON_HEAP_USED, NON_HEAP_MAX};

        Iterable<InstanceServerStatistic> instanceServerStatistics =
                serverStatisticService.getInstanceServerStatistics(instanceId, period, timeRange);

        List<Long> metricDataIds = getMetricDataIds(instanceServerStatistics, metricNames);

        ServerTrendContext context = new ServerTrendContext(timeRange, period * 1000);
        context.setMetricNames(metricNames);
        context.setMetricValue(metricValue);
        context.setMetricDatas(serverStatisticService.getMetricData(metricDataIds));
        context.setServerStatistics(instanceServerStatistics);
        return context.toResponse();
    }

    @RequestMapping(value = {"instance/garbage"})
    public TrendResponse getInstanceGarbageCollection(@RequestParam(value = "appId") Long appId,
            @RequestParam(value = "instanceId") Long instanceId, @RequestParam(value = "time[]") String[] time,
            @RequestParam(value = "period") Long period) {
        Assert.notNull(appId, "ApplicationId must not be null while retrieving garbage trend data");
        Assert.notNull(instanceId, "InstanceId must not be null while retrieving cpuload trend data");
        Assert.notEmpty(time, "Time ranges must not be null while retrieving garbage trend data");
        Assert.notNull(period, "Period must be provided while retrieving garbage trend data");
        Assert.state(period % 60 == 0, "Period must be 60 or the times of 60.");
        Assert.state(time.length == 1, "Only One TimeRange required.");

        TimeRange timeRange = TimeUtils.convertToRange(time[0]);

        CommonMetricValue[] metricValue = new CommonMetricValue[] {AVG};
        GarbageMetricName[] metricNames = new GarbageMetricName[] {COUNT, TIME};

        Iterable<InstanceServerStatistic> instanceServerStatistics =
                serverStatisticService.getInstanceServerStatistics(instanceId, period, timeRange);

        List<Long> metricDataIds = getMetricDataIds(instanceServerStatistics, metricNames);

        ServerTrendContext context = new ServerTrendContext(timeRange, period * 1000);
        context.setMetricNames(metricNames);
        context.setMetricValue(metricValue);
        context.setMetricDatas(serverStatisticService.getMetricData(metricDataIds));
        context.setServerStatistics(instanceServerStatistics);
        return context.toResponse();
    }

    @RequestMapping(value = {"instance/cpuload"})
    public TrendResponse getInstanceCpuLoad(@RequestParam(value = "appId") Long appId,
            @RequestParam(value = "instanceId") Long instanceId, @RequestParam(value = "time[]") String[] time,
            @RequestParam(value = "period") Long period) {
        Assert.notNull(appId, "ApplicationId must not be null while retrieving cpuload trend data");
        Assert.notNull(instanceId, "InstanceId must not be null while retrieving cpuload trend data");
        Assert.notEmpty(time, "Time ranges must not be null while retrieving cpuload trend data");
        Assert.notNull(period, "Period must be provided while retrieving cpuload trend data");
        Assert.state(period % 60 == 0, "Period must be 60 or the times of 60.");
        Assert.state(time.length == 1, "Only One TimeRange required.");

        TimeRange timeRange = TimeUtils.convertToRange(time[0]);

        CommonMetricValue[] metricValue = new CommonMetricValue[] {AVG, MAX, MIN};
        CpuLoadMetricName[] metricNames = new CpuLoadMetricName[] {JVM, SYSTEM};

        Iterable<InstanceServerStatistic> instanceServerStatistics =
                serverStatisticService.getInstanceServerStatistics(instanceId, period, timeRange);

        List<Long> metricDataIds = getMetricDataIds(instanceServerStatistics, metricNames);

        ServerTrendContext context = new ServerTrendContext(timeRange, period * 1000);
        context.setMetricNames(metricNames);
        context.setMetricValue(metricValue);
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
