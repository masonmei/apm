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
public abstract class TrendUtils {
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
                        }).sorted(Comparator.comparingLong(DataPoint::getTimestamp))
                                .collect(Collectors.toList());
                metricData.setData(dataPoints);

                values.add(metricData);
            }
        }

        trendResponse.setValues(values);
        return trendResponse;
    }

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

}
