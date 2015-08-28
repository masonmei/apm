package com.baidu.oped.apm.utils;

import static com.baidu.oped.apm.utils.NumberUtils.format;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.util.Assert;

import com.baidu.oped.apm.common.jpa.entity.ApplicationStatistic;
import com.baidu.oped.apm.common.jpa.entity.InstanceStatistic;
import com.baidu.oped.apm.common.jpa.entity.ServiceType;
import com.baidu.oped.apm.mvc.vo.DataPoint;
import com.baidu.oped.apm.mvc.vo.Metric;
import com.baidu.oped.apm.mvc.vo.MetricData;
import com.baidu.oped.apm.mvc.vo.TimeRange;
import com.baidu.oped.apm.mvc.vo.TrendResponse;

/**
 * Created by mason on 8/27/15.
 */
public class MetricUtils {
    /**
     * Convert applicatoin metric to standard output.
     *
     * @param metricDatas
     * @param metricNames
     *
     * @return
     */
    public static TrendResponse toTrendResponse(Map<TimeRange, Iterable<ApplicationStatistic>> metricDatas,
                                                Constaints.MetricName[] metricNames) {
        Assert.notNull(metricNames, "MetricNames must not be null while convert to trendResponse.");
        Assert.notNull(metricDatas, "MetricData must not be null while convert to trendResponse.");

        TrendResponse trendResponse = new TrendResponse();
        List<Metric> metrics = new ArrayList<>();
        for (Constaints.MetricName metricName : metricNames) {
            Metric metric = new Metric();
            metric.setDescription(metricName.getDescription());
            metric.setName(metricName.getFieldName());
            metric.setUnit(metricName.getUnit());
            metrics.add(metric);
        }
        trendResponse.setMetrics(metrics);

        List<MetricData> values = new ArrayList<>();

        for (Map.Entry<TimeRange, Iterable<ApplicationStatistic>> entry : metricDatas.entrySet()) {
            Iterable<ApplicationStatistic> multiServiceTypeStatistics = entry.getValue();
            Map<ServiceType, List<ApplicationStatistic>> splitMap = splitByServiceType(multiServiceTypeStatistics);

            for (ServiceType serviceType : splitMap.keySet()) {
                MetricData metricData = new MetricData();
                metricData.setTime(entry.getKey().toString());
                metricData.setLegend(serviceType.getDescription());
                List<DataPoint> dataPoints = splitMap.get(serviceType).stream().map(applicationStatistic -> {
                    DataPoint dataPoint = new DataPoint();
                    dataPoint.setTimestamp(applicationStatistic.getTimestamp());
                    dataPoint.setItems(readValuesFromApplicationStatistics(applicationStatistic, metricNames));
                    return dataPoint;
                }).sorted(Comparator.comparingLong(DataPoint::getTimestamp).reversed()).collect(Collectors.toList());
                metricData.setData(dataPoints);

                values.add(metricData);
            }

        }

        trendResponse.setValues(values);
        return trendResponse;
    }

    /**
     * Convert instance metric to standard output.
     *
     * @param metricDatas
     * @param metricNames
     *
     * @return
     */
    public static TrendResponse instanceMetricToTrendResponse(Map<TimeRange, Iterable<InstanceStatistic>> metricDatas,
                                                              Constaints.MetricName[] metricNames) {
        Assert.notNull(metricNames, "MetricNames must not be null while convert to trendResponse.");
        Assert.notNull(metricDatas, "MetricData must not be null while convert to trendResponse.");

        TrendResponse trendResponse = new TrendResponse();
        List<Metric> metrics = new ArrayList<>();
        for (Constaints.MetricName metricName : metricNames) {
            Metric metric = new Metric();
            metric.setDescription(metricName.getDescription());
            metric.setName(metricName.getFieldName());
            metric.setUnit(metricName.getUnit());
            metrics.add(metric);
        }
        trendResponse.setMetrics(metrics);

        List<MetricData> values = new ArrayList<>();

        for (Map.Entry<TimeRange, Iterable<InstanceStatistic>> entry : metricDatas.entrySet()) {
            Iterable<InstanceStatistic> multiServiceTypeStatistics = entry.getValue();
            Map<ServiceType, List<InstanceStatistic>> splitMap =
                    splitInstanceStatisticsByServiceType(multiServiceTypeStatistics);

            for (ServiceType serviceType : splitMap.keySet()) {
                MetricData metricData = new MetricData();
                metricData.setTime(entry.getKey().toString());
                metricData.setLegend(serviceType.getDescription());
                List<DataPoint> dataPoints = splitMap.get(serviceType).stream().map(instanceStatistic -> {
                    DataPoint dataPoint = new DataPoint();
                    dataPoint.setTimestamp(instanceStatistic.getTimestamp());
                    dataPoint.setItems(readValuesFromInstanceStatistics(instanceStatistic, metricNames));
                    return dataPoint;
                }).sorted(Comparator.comparingLong(DataPoint::getTimestamp).reversed()).collect(Collectors.toList());
                metricData.setData(dataPoints);

                values.add(metricData);
            }

        }

        trendResponse.setValues(values);
        return trendResponse;
    }

    private static List<Double> readValuesFromInstanceStatistics(InstanceStatistic instanceStatistic,
                                                                 Constaints.MetricName[] metricNames) {
        Assert.notNull(instanceStatistic, "Cannot read values from a null ApplicationStatistics Object.");
        Assert.notNull(metricNames, "MetricNames must not be null for read value from ApplicationStatistics.");

        List<Double> values = new ArrayList<>(metricNames.length);

        for (Constaints.MetricName name : metricNames) {
            Double value;
            switch (name) {
                case RESPONSE_TIME:
                    value = format(instanceStatistic.getMinResponseTime());
                    break;
                case PV:
                    value = format(instanceStatistic.getPv());
                    break;
                case ERROR:
                    value = format(instanceStatistic.getError());
                    break;
                case CPM:
                    Double original = calculateCpm(instanceStatistic.getPv(), instanceStatistic.getPeriod());
                    value = format(original);
                    break;
                case ERROR_RATE:
                    value = format(calculateRate(instanceStatistic.getError(), instanceStatistic.getPv()));
                    break;
                case APDEX:
                    value = format(ApdexUtils.calculateApdex(instanceStatistic.getSatisfied(),
                                                                    instanceStatistic.getTolerated(),
                                                                    instanceStatistic.getFrustrated()));
                    break;
                case SATISFIED:
                    value = format(instanceStatistic.getSatisfied());
                    break;
                case TOLERATED:
                    value = format(instanceStatistic.getTolerated());
                    break;
                case FRUSTRATED:
                    value = format(instanceStatistic.getFrustrated());
                    break;
                case MAX_RESPONSE_TIME:
                    value = format(instanceStatistic.getMaxResponseTime());
                    break;
                case MIN_RESPONSE_TIME:
                    value = format(instanceStatistic.getMinResponseTime());
                    break;
                case CPU_USAGE:
                    value = format(instanceStatistic.getCpuUsage());
                    break;
                case MEMORY_USAGE:
                    value = format(instanceStatistic.getMemoryUsage());
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported metric");
            }
            values.add(value);
        }

        return values;
    }

    /**
     * Read values of the given metrics.
     *
     * @param applicationStatistic
     * @param names
     *
     * @return
     */
    private static List<Double> readValuesFromApplicationStatistics(ApplicationStatistic applicationStatistic,
                                                                    Constaints.MetricName[] names) {
        Assert.notNull(applicationStatistic, "Cannot read values from a null ApplicationStatistics Object.");
        Assert.notNull(names, "MetricNames must not be null for read value from ApplicationStatistics.");

        List<Double> values = new ArrayList<>(names.length);

        for (Constaints.MetricName name : names) {
            Double value;
            switch (name) {
                case RESPONSE_TIME:
                    value = format(applicationStatistic.getMinResponseTime());
                    break;
                case PV:
                    value = format(applicationStatistic.getPv());
                    break;
                case ERROR:
                    value = format(applicationStatistic.getError());
                    break;
                case CPM:
                    Double original = calculateCpm(applicationStatistic.getPv(), applicationStatistic.getPeriod());
                    value = format(original);
                    break;
                case ERROR_RATE:
                    value = format(calculateRate(applicationStatistic.getError(), applicationStatistic.getPv()));
                    break;
                case APDEX:
                    value = format(ApdexUtils.calculateApdex(applicationStatistic.getSatisfied(),
                                                                    applicationStatistic.getTolerated(),
                                                                    applicationStatistic.getFrustrated()));
                    break;
                case SATISFIED:
                    value = format(applicationStatistic.getSatisfied());
                    break;
                case TOLERATED:
                    value = format(applicationStatistic.getTolerated());
                    break;
                case FRUSTRATED:
                    value = format(applicationStatistic.getFrustrated());
                    break;
                case MAX_RESPONSE_TIME:
                    value = format(applicationStatistic.getMaxResponseTime());
                    break;
                case MIN_RESPONSE_TIME:
                    value = format(applicationStatistic.getMinResponseTime());
                    break;
                case CPU_USAGE:
                    value = format(applicationStatistic.getCpuUsage());
                    break;
                case MEMORY_USAGE:
                    value = format(applicationStatistic.getMemoryUsage());
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported metric");
            }
            values.add(value);
        }

        return values;
    }

    /**
     * Rate Calculator
     *
     * @param error
     * @param pv
     *
     * @return
     */
    private static Double calculateRate(Long error, Long pv) {
        if (pv == null || pv < 0) {
            return 0.0;
        }
        return (error * 1.0) / pv;
    }

    /**
     * CPM calculator.
     *
     * @param pv
     * @param period
     *
     * @return
     */
    private static Double calculateCpm(Long pv, Integer period) {
        Assert.notNull(period, "Period must not be null while calculate cpm");
        return pv / (period * Constaints.PERIOD_TO_MINUTE_FACTOR);
    }

    /**
     * Split a set of ApplicationStatistic group by ServiceType.
     *
     * @param statistics original ApplicationStatistics to split
     *
     * @return
     */
    public static Map<ServiceType, List<ApplicationStatistic>> splitByServiceType(Iterable<ApplicationStatistic>
                                                                                          statistics) {
        Assert.notNull(statistics, "Cannot split a null iterable to map by serviceType");
        Map<ServiceType, List<ApplicationStatistic>> splitMap = new HashMap<>();
        StreamSupport.stream(statistics.spliterator(), false).forEach(applicationStatistic -> {
            ServiceType serviceType = applicationStatistic.getServiceType();

            if (splitMap.get(serviceType) == null) {
                splitMap.put(serviceType, new ArrayList<>());
            }
            splitMap.get(serviceType).add(applicationStatistic);
        });
        return splitMap;
    }

    /**
     * Split a set of ApplicationStatistic group by ServiceType.
     *
     * @param statistics original ApplicationStatistics to split
     *
     * @return
     */
    public static Map<ServiceType, List<InstanceStatistic>> splitInstanceStatisticsByServiceType(
            Iterable<InstanceStatistic> statistics) {
        Assert.notNull(statistics, "Cannot split a null iterable to map by serviceType");
        Map<ServiceType, List<InstanceStatistic>> splitMap = new HashMap<>();
        StreamSupport.stream(statistics.spliterator(), false).forEach(applicationStatistic -> {
            ServiceType serviceType = applicationStatistic.getServiceType();

            if (splitMap.get(serviceType) == null) {
                splitMap.put(serviceType, new ArrayList<>());
            }
            splitMap.get(serviceType).add(applicationStatistic);
        });
        return splitMap;
    }
}
