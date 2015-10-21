package com.baidu.oped.apm.mvc.vo;

import static com.baidu.oped.apm.common.utils.NumberUtils.format;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.util.Assert;

import com.baidu.oped.apm.common.jpa.entity.MetricData;
import com.baidu.oped.apm.common.jpa.entity.ServerStatistic;
import com.baidu.oped.apm.common.utils.Constraints;
import com.baidu.oped.apm.common.utils.Constraints.CommonMetricValue;
import com.baidu.oped.apm.common.utils.Constraints.CpuLoadMetricName;
import com.baidu.oped.apm.common.utils.Constraints.GarbageMetricName;
import com.baidu.oped.apm.common.utils.Constraints.JVMMetricName;
import com.baidu.oped.apm.common.utils.Constraints.MetricName;
import com.baidu.oped.apm.common.utils.Constraints.ThreadMetricName;
import com.baidu.oped.apm.common.utils.NumberUtils;
import com.baidu.oped.apm.common.utils.TimeUtil;
import com.baidu.oped.apm.model.entity.DummyServerStatistic;

/**
 * Created by mason on 9/29/15.
 */
public class ServerTrendContext<T extends ServerStatistic> {

    private final TimeRange timeRange;
    private final Long periodInMillis;
    private Map<Long, MetricData> metricDatas;
    private Iterable<T> serverStatistics;
    private CommonMetricValue[] metricValue;
    private MetricName[] metricNames;

    public ServerTrendContext(TimeRange timeRange, Long periodInMillis) {
        this.timeRange = timeRange;
        this.periodInMillis = periodInMillis;
    }

    public void setMetricDatas(Iterable<MetricData> metricDatas) {
        this.metricDatas = StreamSupport.stream(metricDatas.spliterator(), false)
                .collect(Collectors.toMap(MetricData::getId, metricData -> metricData));
    }

    public void setServerStatistics(Iterable<T> serverStatistics) {
        this.serverStatistics = serverStatistics;
    }

    public void setMetricValue(CommonMetricValue[] metricValue) {
        this.metricValue = metricValue;
    }

    public void setMetricNames(MetricName[] metricNames) {
        this.metricNames = metricNames;
    }

    public TrendResponse toResponse() {
        TrendResponse response = new TrendResponse();
        List<MetricVo> metrics = buildMetrics();
        response.setMetrics(metrics);
        List<MetricDataVo> values = buildMetricData();
        response.setValues(values);
        return response;
    }

    private List<MetricDataVo> buildMetricData() {
        List<MetricDataVo> values = new ArrayList<>();

        for (MetricName metricName : metricNames) {
            MetricDataVo metricData = new MetricDataVo();
            metricData.setTime(timeRange.toString());
            metricData.setLegend(metricName.getDescription());

            List<DataPointVo> dataPoints = buildDataPoints(metricName);
            metricData.setData(dataPoints);

            values.add(metricData);
        }

        return values;
    }

    private List<DataPointVo> buildDataPoints(MetricName metricName) {

        List<DataPointVo> dataPoints = getStatistic().stream().map(applicationStatistic -> {
            DataPointVo dataPoint = new DataPointVo();
            dataPoint.setTimestamp(applicationStatistic.getTimestamp());
            dataPoint.setItems(readValuesFromStatistic(applicationStatistic, metricName));
            return dataPoint;
        }).sorted(Comparator.comparingLong(DataPointVo::getTimestamp)).collect(Collectors.toList());

        return dataPoints;
    }

    private List<Double> readValuesFromStatistic(ServerStatistic serverStatistic, MetricName metricName) {
        Assert.notNull(serverStatistic, "Cannot read values from a null CommonStatistic Object.");
        Assert.notNull(metricName, "MetricName must not be null for read value from CommonStatistic.");

        List<Double> values = new ArrayList<>(metricValue.length);

        Long metricDataId = null;
        if (metricName instanceof JVMMetricName) {
            JVMMetricName jvmMetricName = (JVMMetricName) metricName;
            switch (jvmMetricName) {
                case HEAP_INIT:
                    throw new UnsupportedOperationException("Not supported yet!");
                case HEAP_USED:
                    metricDataId = serverStatistic.getHeapUsedMetric();
                    break;
                case HEAP_MAX:
                    metricDataId = serverStatistic.getHeapMaxMetric();
                    break;
                case HEAP_COMMITTED:
                    throw new UnsupportedOperationException("Not supported yet!");
                case NON_HEAP_INIT:
                    throw new UnsupportedOperationException("Not supported yet!");
                case NON_HEAP_USED:
                    metricDataId = serverStatistic.getNonHeapUsedMetric();
                    break;
                case NON_HEAP_MAX:
                    metricDataId = serverStatistic.getNonHeapMaxMetric();
                    break;
                case NON_HEAP_COMMITTED:
                    throw new UnsupportedOperationException("Not supported yet!");
                case TOTAL_INIT:
                    throw new UnsupportedOperationException("Not supported yet!");
                case TOTAL_USED:
                    throw new UnsupportedOperationException("Not supported yet!");
                case TOTAL_MAX:
                    throw new UnsupportedOperationException("Not supported yet!");
                case TOTAL_COMMITTED:
                    throw new UnsupportedOperationException("Not supported yet!");
                default:
                    throw new UnsupportedOperationException("Not supported yet!");
            }
        } else if (metricName instanceof CpuLoadMetricName) {
            CpuLoadMetricName cpuLoadMetricName = (CpuLoadMetricName) metricName;
            switch (cpuLoadMetricName) {
                case JVM:
                    metricDataId = serverStatistic.getJvmCpuMetric();
                    break;
                case SYSTEM:
                    metricDataId = serverStatistic.getSystemCpuMetric();
                    break;
                default:
                    throw new UnsupportedOperationException("Not Supported!");
            }

        } else if (metricName instanceof GarbageMetricName) {
            GarbageMetricName garbageMetricName = (GarbageMetricName) metricName;
            switch (garbageMetricName) {
                case COUNT:
                    metricDataId = serverStatistic.getGcOldCountMetric();
                    break;
                case TIME:
                    metricDataId = serverStatistic.getGcOldTimeMetric();
                    break;
                default:
                    throw new UnsupportedOperationException("Not Supported!");
            }

        } else if (metricName instanceof ThreadMetricName) {
            ThreadMetricName threadMetricName = (ThreadMetricName) metricName;
            switch (threadMetricName) {
                case TOTAL:
                    throw new UnsupportedOperationException("Not supported yet!");
                case DAEMON:
                    throw new UnsupportedOperationException("Not supported yet!");
                case DEADLOCK:
                    throw new UnsupportedOperationException("Not supported yet!");
                default:
                    throw new UnsupportedOperationException("Not supported!");
            }
        }

        MetricData metricData = null;
        if(metricDataId != null){
            metricData = metricDatas.get(metricDataId);
        }

        if(metricData == null){
            metricData = new MetricData();
        }

        for (CommonMetricValue commonMetricValue : metricValue) {

            switch (commonMetricValue) {
                case COUNT:
                    values.add(format(metricData.getCount()));
                    break;
                case SUM:
                    values.add(format(metricData.getSum()));
                    break;
                case AVG:
                    values.add(format(metricData.getAvg()));
                    break;
                case MIN:
                    values.add(format(metricData.getMin()));
                    break;
                case MAX:
                    values.add(format(metricData.getMax()));
                    break;
                default:
                    throw new UnsupportedOperationException("Not Supported metricValue.");
            }
        }
        return values;
    }

    private List<ServerStatistic> getStatistic() {
        Map<Long, ServerStatistic> timestampStatisticMap = new HashMap<>();
        serverStatistics.forEach(statistic -> timestampStatisticMap.put(statistic.getTimestamp(), statistic));
        List<Long> timestamps = TimeUtil.getTimestamps(timeRange.getFrom(), timeRange.getTo(), periodInMillis);

        List<ServerStatistic> statisticList = timestamps.stream().map(timestamp -> {
            ServerStatistic statistic = timestampStatisticMap.get(timestamp);
            if (statistic == null) {
                statistic = new DummyServerStatistic();
                statistic.setTimestamp(timestamp);
                statistic.setPeriod(periodInMillis);
            }
            return statistic;
        }).collect(Collectors.toList());

        return statisticList;
    }

    private List<MetricVo> buildMetrics() {
        List<MetricVo> metrics = new ArrayList<>();
        for (Constraints.MetricValue metricName : metricValue) {
            MetricVo metric = new MetricVo();
            metric.setDescription(metricName.getDescription());
            metric.setName(metricName.getName());
            metric.setUnit(metricName.getUnit());
            metrics.add(metric);
        }
        return metrics;
    }

}
