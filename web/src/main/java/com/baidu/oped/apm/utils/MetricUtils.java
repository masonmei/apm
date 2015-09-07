package com.baidu.oped.apm.utils;

import static com.baidu.oped.apm.utils.NumberUtils.format;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.util.Assert;

import com.baidu.oped.apm.common.jpa.entity.CommonStatistic;
import com.baidu.oped.apm.common.jpa.entity.HostStatistic;
import com.baidu.oped.apm.common.jpa.entity.ServiceType;
import com.baidu.oped.apm.common.jpa.entity.Statistic;
import com.baidu.oped.apm.mvc.vo.DataPoint;
import com.baidu.oped.apm.mvc.vo.Metric;
import com.baidu.oped.apm.mvc.vo.MetricData;
import com.baidu.oped.apm.mvc.vo.TimeRange;
import com.baidu.oped.apm.mvc.vo.TrendContext;
import com.baidu.oped.apm.mvc.vo.TrendResponse;

/**
 * Created by mason on 8/27/15.
 */
public class MetricUtils {
    /**
     * Convert application metric to standard output.
     *
     * @param context
     * @param metricNames
     *
     * @return
     */
    public static TrendResponse toTrendResponse(TrendContext context, Constaints.MetricName[] metricNames) {
        Assert.notNull(metricNames, "MetricNames must not be null while convert to trendResponse.");
        Assert.notNull(context, "MetricData must not be null while convert to trendResponse.");

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

        for (TimeRange timeRange : context.getTimeRanges()) {
            for (ServiceType serviceType : context.getServiceTypes()) {
                MetricData metricData = new MetricData();
                metricData.setTime(timeRange.toString());
                metricData.setLegend(serviceType.getDescription());

                List<DataPoint> dataPoints =
                        context.getStatistic(timeRange, serviceType).stream().map(applicationStatistic -> {
                            DataPoint dataPoint = new DataPoint();
                            dataPoint.setTimestamp(applicationStatistic.getTimestamp());
                            dataPoint.setItems(readValuesFromStatistic(applicationStatistic, metricNames));
                            return dataPoint;
                        }).sorted(Comparator.comparingLong(DataPoint::getTimestamp).reversed())
                                .collect(Collectors.toList());
                metricData.setData(dataPoints);

                values.add(metricData);
            }
        }

        trendResponse.setValues(values);
        return trendResponse;
    }

//    /**
//     * Convert application metric to standard output.
//     *
//     * @param metricDatas
//     * @param metricNames
//     *
//     * @return
//     */
//    public static TrendResponse toTrendResponse(Map<TimeRange, Iterable<Statistic>> metricDatas,
//                                                Constaints.MetricName[] metricNames) {
//        Assert.notNull(metricNames, "MetricNames must not be null while convert to trendResponse.");
//        Assert.notNull(metricDatas, "MetricData must not be null while convert to trendResponse.");
//
//        TrendResponse trendResponse = new TrendResponse();
//        List<Metric> metrics = new ArrayList<>();
//        for (Constaints.MetricName metricName : metricNames) {
//            Metric metric = new Metric();
//            metric.setDescription(metricName.getDescription());
//            metric.setName(metricName.getFieldName());
//            metric.setUnit(metricName.getUnit());
//            metrics.add(metric);
//        }
//        trendResponse.setMetrics(metrics);
//
//        List<MetricData> values = new ArrayList<>();
//
//        for (Map.Entry<TimeRange, Iterable<Statistic>> entry : metricDatas.entrySet()) {
//            Iterable<Statistic> multiServiceTypeStatistics = entry.getValue();
//            Map<ServiceType, List<Statistic>> splitMap = splitByServiceType(multiServiceTypeStatistics);
//
//            for (ServiceType serviceType : splitMap.keySet()) {
//                MetricData metricData = new MetricData();
//                metricData.setTime(entry.getKey().toString());
//                metricData.setLegend(serviceType.getDescription());
//                List<DataPoint> dataPoints = splitMap.get(serviceType).stream().map(applicationStatistic -> {
//                    DataPoint dataPoint = new DataPoint();
//                    dataPoint.setTimestamp(applicationStatistic.getTimestamp());
//                    dataPoint.setItems(readValuesFromStatistic(applicationStatistic, metricNames));
//                    return dataPoint;
//                }).sorted(Comparator.comparingLong(DataPoint::getTimestamp).reversed()).collect(Collectors.toList());
//                metricData.setData(dataPoints);
//
//                values.add(metricData);
//            }
//
//        }
//
//        trendResponse.setValues(values);
//        return trendResponse;
//    }

    //    /**
    //     * Convert instance metric to standard output.
    //     *
    //     * @param metricDatas
    //     * @param metricNames
    //     *
    //     * @return
    //     */
    //    public static TrendResponse instanceMetricToTrendResponse(Map<TimeRange, Iterable<InstanceStatistic>>
    // metricDatas,
    //                                                              Constaints.MetricName[] metricNames) {
    //        Assert.notNull(metricNames, "MetricNames must not be null while convert to trendResponse.");
    //        Assert.notNull(metricDatas, "MetricData must not be null while convert to trendResponse.");
    //
    //        TrendResponse trendResponse = new TrendResponse();
    //        List<Metric> metrics = new ArrayList<>();
    //        for (Constaints.MetricName metricName : metricNames) {
    //            Metric metric = new Metric();
    //            metric.setDescription(metricName.getDescription());
    //            metric.setName(metricName.getFieldName());
    //            metric.setUnit(metricName.getUnit());
    //            metrics.add(metric);
    //        }
    //        trendResponse.setMetrics(metrics);
    //
    //        List<MetricData> values = new ArrayList<>();
    //
    //        for (Map.Entry<TimeRange, Iterable<InstanceStatistic>> entry : metricDatas.entrySet()) {
    //            Iterable<InstanceStatistic> multiServiceTypeStatistics = entry.getValue();
    //            Map<ServiceType, List<InstanceStatistic>> splitMap =
    //                    splitInstanceStatisticsByServiceType(multiServiceTypeStatistics);
    //
    //            for (ServiceType serviceType : splitMap.keySet()) {
    //                MetricData metricData = new MetricData();
    //                metricData.setTime(entry.getKey().toString());
    //                metricData.setLegend(serviceType.getDescription());
    //                List<DataPoint> dataPoints = splitMap.get(serviceType).stream().map(instanceStatistic -> {
    //                    DataPoint dataPoint = new DataPoint();
    //                    dataPoint.setTimestamp(instanceStatistic.getTimestamp());
    //                    dataPoint.setItems(readValuesFromStatistic(instanceStatistic, metricNames));
    //                    return dataPoint;
    //                }).sorted(Comparator.comparingLong(DataPoint::getTimestamp).reversed()).collect(Collectors
    // .toList());
    //                metricData.setData(dataPoints);
    //
    //                values.add(metricData);
    //            }
    //
    //        }
    //
    //        trendResponse.setValues(values);
    //        return trendResponse;
    //    }

    //    /**
    //     * Convert instance metric to standard output.
    //     *
    //     * @param metricDatas
    //     * @param metricNames
    //     * @param serviceType
    //     *
    //     * @return
    //     */
    //    public static TrendResponse transactionMetricToTrendResponse(Map<TimeRange, Iterable<WebTransactionStatistic>>
    //                                                                         metricDatas,
    //                                                                 Constaints.MetricName[] metricNames,
    //                                                                 ServiceType serviceTypes) {
    //        Assert.notNull(metricNames, "MetricNames must not be null while convert to trendResponse.");
    //        Assert.notNull(metricDatas, "MetricData must not be null while convert to trendResponse.");
    //
    //        TrendResponse trendResponse = new TrendResponse();
    //        List<Metric> metrics = new ArrayList<>();
    //        for (Constaints.MetricName metricName : metricNames) {
    //            Metric metric = new Metric();
    //            metric.setDescription(metricName.getDescription());
    //            metric.setName(metricName.getFieldName());
    //            metric.setUnit(metricName.getUnit());
    //            metrics.add(metric);
    //        }
    //        trendResponse.setMetrics(metrics);
    //
    //        List<MetricData> values = new ArrayList<>();
    //
    //        for (Map.Entry<TimeRange, Iterable<WebTransactionStatistic>> entry : metricDatas.entrySet()) {
    //            Iterable<WebTransactionStatistic> multiServiceTypeStatistics = entry.getValue();
    //            Map<ServiceType, List<WebTransactionStatistic>> splitMap =
    //                    splitStatisticsStatisticsByServiceType(multiServiceTypeStatistics, serviceTypes);
    //
    //            for (ServiceType serviceType : splitMap.keySet()) {
    //                MetricData metricData = new MetricData();
    //                metricData.setTime(entry.getKey().toString());
    //                metricData.setLegend(serviceType.getDescription());
    //                List<DataPoint> dataPoints = splitMap.get(serviceType).stream().map(instanceStatistic -> {
    //                    DataPoint dataPoint = new DataPoint();
    //                    dataPoint.setTimestamp(instanceStatistic.getTimestamp());
    //                    dataPoint.setItems(readValuesFromStatistic(instanceStatistic, metricNames));
    //                    return dataPoint;
    //                }).sorted(Comparator.comparingLong(DataPoint::getTimestamp).reversed()).collect(Collectors
    // .toList());
    //                metricData.setData(dataPoints);
    //
    //                values.add(metricData);
    //            }
    //
    //        }
    //
    //        trendResponse.setValues(values);
    //        return trendResponse;
    //    }
    //
    //    private static List<Double> readValuesFromTransactionStatistics(WebTransactionStatistic instanceStatistic,
    //                                                                    Constaints.MetricName[] metricNames) {
    //        Assert.notNull(instanceStatistic, "Cannot read values from a null ApplicationStatistics Object.");
    //        Assert.notNull(metricNames, "MetricNames must not be null for read value from ApplicationStatistics.");
    //
    //        List<Double> values = new ArrayList<>(metricNames.length);
    //
    //        for (Constaints.MetricName name : metricNames) {
    //            Double value;
    //            switch (name) {
    //                case RESPONSE_TIME:
    //                    value = format(instanceStatistic.getMinResponseTime());
    //                    break;
    //                case PV:
    //                    value = format(instanceStatistic.getPv());
    //                    break;
    //                case ERROR:
    //                    value = format(instanceStatistic.getError());
    //                    break;
    //                case CPM:
    //                    Double original = calculateCpm(instanceStatistic.getPv(), instanceStatistic.getPeriod());
    //                    value = format(original);
    //                    break;
    //                case ERROR_RATE:
    //                    value = format(calculateDouble(instanceStatistic.getError(), instanceStatistic.getPv()));
    //                    break;
    //                case APDEX:
    //                    value = format(ApdexUtils.calculateApdex(instanceStatistic.getSatisfied(),
    //                                                                    instanceStatistic.getTolerated(),
    //                                                                    instanceStatistic.getFrustrated()));
    //                    break;
    //                case SATISFIED:
    //                    value = format(instanceStatistic.getSatisfied());
    //                    break;
    //                case TOLERATED:
    //                    value = format(instanceStatistic.getTolerated());
    //                    break;
    //                case FRUSTRATED:
    //                    value = format(instanceStatistic.getFrustrated());
    //                    break;
    //                case MAX_RESPONSE_TIME:
    //                    value = format(instanceStatistic.getMaxResponseTime());
    //                    break;
    //                case MIN_RESPONSE_TIME:
    //                    value = format(instanceStatistic.getMinResponseTime());
    //                    break;
    //                case CPU_USAGE:
    //                case MEMORY_USAGE:
    //                default:
    //                    throw new IllegalArgumentException("Unsupported metric");
    //            }
    //            values.add(value);
    //        }
    //
    //        return values;
    //    }

    //    private static List<Double> readValuesFromInstanceStatistics(InstanceStatistic instanceStatistic,
    //                                                                 Constaints.MetricName[] metricNames) {
    //        Assert.notNull(instanceStatistic, "Cannot read values from a null ApplicationStatistics Object.");
    //        Assert.notNull(metricNames, "MetricNames must not be null for read value from ApplicationStatistics.");
    //
    //        List<Double> values = new ArrayList<>(metricNames.length);
    //
    //        for (Constaints.MetricName name : metricNames) {
    //            Double value;
    //            switch (name) {
    //                case RESPONSE_TIME:
    //                    value = format(instanceStatistic.getMinResponseTime());
    //                    break;
    //                case PV:
    //                    value = format(instanceStatistic.getPv());
    //                    break;
    //                case ERROR:
    //                    value = format(instanceStatistic.getError());
    //                    break;
    //                case CPM:
    //                    Double original = calculateCpm(instanceStatistic.getPv(), instanceStatistic.getPeriod());
    //                    value = format(original);
    //                    break;
    //                case ERROR_RATE:
    //                    value = format(calculateDouble(instanceStatistic.getError(), instanceStatistic.getPv()));
    //                    break;
    //                case APDEX:
    //                    value = format(ApdexUtils.calculateApdex(instanceStatistic.getSatisfied(),
    //                                                                    instanceStatistic.getTolerated(),
    //                                                                    instanceStatistic.getFrustrated()));
    //                    break;
    //                case SATISFIED:
    //                    value = format(instanceStatistic.getSatisfied());
    //                    break;
    //                case TOLERATED:
    //                    value = format(instanceStatistic.getTolerated());
    //                    break;
    //                case FRUSTRATED:
    //                    value = format(instanceStatistic.getFrustrated());
    //                    break;
    //                case MAX_RESPONSE_TIME:
    //                    value = format(instanceStatistic.getMaxResponseTime());
    //                    break;
    //                case MIN_RESPONSE_TIME:
    //                    value = format(instanceStatistic.getMinResponseTime());
    //                    break;
    //                case CPU_USAGE:
    //                    value = format(instanceStatistic.getCpuUsage());
    //                    break;
    //                case MEMORY_USAGE:
    //                    value = format(instanceStatistic.getMemoryUsage());
    //                    break;
    //                default:
    //                    throw new IllegalArgumentException("Unsupported metric");
    //            }
    //            values.add(value);
    //        }
    //
    //        return values;
    //    }

    /**
     * Read values of the given metrics.
     *
     * @param statistic
     * @param names
     *
     * @return
     */
    private static List<Double> readValuesFromStatistic(Statistic statistic, Constaints.MetricName[] names) {
        Assert.notNull(statistic, "Cannot read values from a null CommonStatistic Object.");
        Assert.notNull(names, "MetricNames must not be null for read value from CommonStatistic.");

        List<Double> values = new ArrayList<>(names.length);

        CommonStatistic commonStatistic = null;
        HostStatistic hostStatistic = null;

        if (statistic instanceof CommonStatistic) {
            commonStatistic = (CommonStatistic) statistic;
        }

        if (statistic instanceof HostStatistic) {
            hostStatistic = (HostStatistic) statistic;
        }

        for (Constaints.MetricName name : names) {
            Double value;
            switch (name) {
                case RESPONSE_TIME:
                    if (commonStatistic != null) {
                        value = format(calculateDouble(commonStatistic.getSumResponseTime(), commonStatistic.getPv()));
                    } else {
                        value = null;
                    }
                    break;
                case MAX_RESPONSE_TIME:
                    if (commonStatistic != null) {
                        value = format(commonStatistic.getMaxResponseTime());
                    } else {
                        value = null;
                    }
                    break;
                case MIN_RESPONSE_TIME:
                    if (commonStatistic != null) {
                        value = format(commonStatistic.getMinResponseTime());
                    } else {
                        value = null;
                    }
                    break;

                case PV:
                    if (commonStatistic != null) {
                        value = format(commonStatistic.getPv());
                    } else {
                        value = null;
                    }
                    break;
                case CPM:
                    if (commonStatistic != null) {
                        Double original = calculateCpm(commonStatistic.getPv(), statistic.getPeriod());
                        value = format(original);
                    } else {
                        value = null;
                    }
                    break;

                case ERROR:
                    if (commonStatistic != null) {
                        value = format(commonStatistic.getError());
                    } else {
                        value = null;
                    }
                    break;
                case ERROR_RATE:
                    if (commonStatistic != null) {
                        value = format(calculateDouble(commonStatistic.getError(), commonStatistic.getPv()));
                    } else {
                        value = null;
                    }
                    break;
                case SATISFIED:
                    if (commonStatistic != null) {
                        value = format(commonStatistic.getSatisfied());
                    } else {
                        value = null;
                    }
                    break;
                case TOLERATED:
                    if (commonStatistic != null) {
                        value = format(commonStatistic.getTolerated());
                    } else {
                        value = null;
                    }
                    break;
                case FRUSTRATED:
                    if (commonStatistic != null) {
                        value = format(commonStatistic.getFrustrated());
                    } else {
                        value = null;
                    }
                    break;
                case APDEX:
                    if (commonStatistic != null) {
                        value = format(ApdexUtils.calculateApdex(commonStatistic.getSatisfied(),
                                                                        commonStatistic.getTolerated(),
                                                                        commonStatistic.getFrustrated()));
                    } else {
                        value = null;
                    }
                    break;

                case CPU_USAGE:
                    if (hostStatistic != null) {
                        value = format(hostStatistic.getCpuUsage());
                    } else {
                        value = null;
                    }
                    break;
                case MEMORY_USAGE:
                    if (hostStatistic != null) {
                        value = format(hostStatistic.getMemoryUsage());
                    } else {
                        value = null;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported metric");
            }
            values.add(value);
        }

        return values;
    }

    /**
     * Double Calculator
     *
     * @param number
     * @param pv
     *
     * @return
     */
    private static Double calculateDouble(Number number, Number pv) {
        if (pv == null || pv.doubleValue() < 0) {
            return 0.0;
        }

        if (number == null) {
            number = new BigDecimal(0);
        }

        return number.doubleValue() / pv.doubleValue();
    }

    /**
     * CPM calculator.
     *
     * @param pv
     * @param period
     *
     * @return
     */
    private static Double calculateCpm(Long pv, Long period) {
        Assert.notNull(period, "Period must not be null while calculate cpm");
        return calculateDouble(pv, period * Constaints.PERIOD_TO_MINUTE_FACTOR);
    }

//    /**
//     * Split a set of ApplicationStatistic group by ServiceType.
//     *
//     * @param statistics original ApplicationStatistics to split
//     *
//     * @return
//     */
//    public static Map<ServiceType, List<Statistic>> splitByServiceType(Iterable<Statistic> statistics) {
//        Assert.notNull(statistics, "Cannot split a null iterable to map by serviceType");
//        Map<ServiceType, List<Statistic>> splitMap = new HashMap<>();
//        StreamSupport.stream(statistics.spliterator(), false).forEach(statistic -> {
//            ServiceType serviceType = statistic.getServiceType();
//
//            if (splitMap.get(serviceType) == null) {
//                splitMap.put(serviceType, new ArrayList<>());
//            }
//            splitMap.get(serviceType).add(statistic);
//        });
//        return splitMap;
//    }

    //    /**
    //     * Split a set of ApplicationStatistic group by ServiceType.
    //     *
    //     * @param statistics original ApplicationStatistics to split
    //     *
    //     * @return
    //     */
    //    public static Map<ServiceType, List<InstanceStatistic>> splitInstanceStatisticsByServiceType
    //    (Iterable<InstanceStatistic> statistics) {
    //        Assert.notNull(statistics, "Cannot split a null iterable to map by serviceType");
    //        Map<ServiceType, List<InstanceStatistic>> splitMap = new HashMap<>();
    //        StreamSupport.stream(statistics.spliterator(), false).forEach(applicationStatistic -> {
    //            ServiceType serviceType = applicationStatistic.getServiceType();
    //
    //            if (splitMap.get(serviceType) == null) {
    //                splitMap.put(serviceType, new ArrayList<>());
    //            }
    //            splitMap.get(serviceType).add(applicationStatistic);
    //        });
    //        return splitMap;
    //    }

    //    /**
    //     * Split a set of WebTransactionStatistic group by ServiceType.
    //     *
    //     * @param statistics
    //     * @param serviceType
    //     *
    //     * @return
    //     */
    //    private static Map<ServiceType, List<WebTransactionStatistic>> splitStatisticsStatisticsByServiceType
    //    (Iterable<WebTransactionStatistic> statistics,
    //
    // ServiceType
    //
    //   serviceType) {
    //        Assert.notNull(statistics, "Cannot split a null iterable to map by serviceType");
    //        Map<ServiceType, List<WebTransactionStatistic>> splitMap = new HashMap<>();
    //        StreamSupport.stream(statistics.spliterator(), false).forEach(applicationStatistic -> {
    //            if (splitMap.get(serviceType) == null) {
    //                splitMap.put(serviceType, new ArrayList<>());
    //            }
    //            splitMap.get(serviceType).add(applicationStatistic);
    //        });
    //        return splitMap;
    //    }

}
