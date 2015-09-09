package com.baidu.oped.apm.utils;

import static com.baidu.oped.apm.common.utils.NumberUtils.calculateRate;
import static com.baidu.oped.apm.common.utils.NumberUtils.format;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.domain.Page;

import com.baidu.oped.apm.common.jpa.entity.Application;
import com.baidu.oped.apm.common.jpa.entity.ApplicationStatistic;
import com.baidu.oped.apm.common.jpa.entity.Instance;
import com.baidu.oped.apm.common.jpa.entity.InstanceStatistic;
import com.baidu.oped.apm.common.utils.ApdexUtils;
import com.baidu.oped.apm.mvc.vo.ApplicationVo;
import com.baidu.oped.apm.mvc.vo.InstanceVo;
import com.baidu.oped.apm.mvc.vo.PageInfo;
import com.baidu.oped.apm.mvc.vo.QueryResponse;
import com.baidu.oped.apm.mvc.vo.TimeRange;

/**
 * Created by mason on 9/7/15.
 */
public abstract class QueryUtils {

    /**
     *
     * @param page
     * @param applicationStatistics
     * @param timeRange
     * @return
     */
    public static QueryResponse<ApplicationVo> toApplicationResponse(
            Page<Application> page, Iterable<ApplicationStatistic> applicationStatistics, TimeRange timeRange) {

        PageInfo pageInfo = new PageInfo();
        List<ApplicationVo> data = new ArrayList<>(page.getSize());
        QueryResponse<ApplicationVo> response = new QueryResponse<>(pageInfo, data);

        pageInfo.setPageSize(page.getSize());
        pageInfo.setPageNumber(page.getNumber());
        pageInfo.setTotal(page.getTotalElements());

        // appNames
        Map<Long, String> appIdNameMap =
                page.getContent().stream().collect(Collectors.toMap(Application::getId, Application::getAppName));

        // Group by appId
        Map<Long, List<ApplicationStatistic>> appMappedStatistic =
                StreamSupport.stream(applicationStatistics.spliterator(), false)
                        .collect(Collectors.groupingBy(ApplicationStatistic::getAppId));

        appMappedStatistic.forEach((aLong, statistics) -> {
            DoubleSummaryStatistics sumResponseSummaryStatistics =
                    statistics.stream().filter(statistic -> statistic.getSumResponseTime() != null)
                            .mapToDouble(ApplicationStatistic::getSumResponseTime).summaryStatistics();
            DoubleSummaryStatistics pvSummaryStatistics =
                    statistics.stream().filter(statistic -> statistic.getPv() != null)
                            .mapToDouble(ApplicationStatistic::getPv).summaryStatistics();
            DoubleSummaryStatistics errorSummaryStatistics =
                    statistics.stream().filter(statistic -> statistic.getError() != null)
                            .mapToDouble(ApplicationStatistic::getError).summaryStatistics();

            ApplicationVo vo = new ApplicationVo();
            vo.setAppId(aLong);
            vo.setResponseTime(format(calculateRate(sumResponseSummaryStatistics.getSum(),
                                                           pvSummaryStatistics.getSum())));
            vo.setCpm(format(calculateRate(pvSummaryStatistics.getSum(), timeRange.getDuration(ChronoUnit.MINUTES))));
            vo.setErrorRate(format(calculateRate(errorSummaryStatistics.getSum(), pvSummaryStatistics.getSum())));
            vo.setAppName(appIdNameMap.get(aLong));
            data.add(vo);
        });

        return response;
    }

    /**
     *
     * @param appId
     * @param page
     * @param instanceStatistics
     * @param timeRange
     * @return
     */
    public static QueryResponse<InstanceVo> toInstanceResponse(
            long appId, Page<Instance> page, Iterable<InstanceStatistic> instanceStatistics, TimeRange timeRange) {

        PageInfo pageInfo = new PageInfo();
        List<InstanceVo> data = new ArrayList<>(page.getSize());
        QueryResponse<InstanceVo> response = new QueryResponse<>(pageInfo, data);

        pageInfo.setPageSize(page.getSize());
        pageInfo.setPageNumber(page.getNumber());
        pageInfo.setTotal(page.getTotalElements());

        // appNames
        Map<Long, String> instanceIdNameMap =
                page.getContent().stream().collect(Collectors.toMap(Instance::getId, instance -> {
                    StringBuilder builder = new StringBuilder();
                    builder.append(instance.getHost());
                    if (instance.getPort() != null) {
                        builder.append(":").append(instance.getPort());
                    }
                    return builder.toString();
                }));

        // Group by instanceId
        Map<Long, List<InstanceStatistic>> appMappedStatistic =
                StreamSupport.stream(instanceStatistics.spliterator(), false)
                        .collect(Collectors.groupingBy(InstanceStatistic::getInstanceId));

        appMappedStatistic.forEach((aLong, statistics) -> {
            DoubleSummaryStatistics sumResponseSummaryStatistics =
                    statistics.stream().filter(statistic -> statistic.getSumResponseTime() != null)
                            .mapToDouble(InstanceStatistic::getSumResponseTime).summaryStatistics();
            DoubleSummaryStatistics pvSummaryStatistics =
                    statistics.stream().filter(statistic -> statistic.getPv() != null)
                            .mapToDouble(InstanceStatistic::getPv).summaryStatistics();
            DoubleSummaryStatistics errorSummaryStatistics =
                    statistics.stream().filter(statistic -> statistic.getError() != null)
                            .mapToDouble(InstanceStatistic::getError).summaryStatistics();
            LongSummaryStatistics satisfiedSummaryStatistics =
                    statistics.stream().filter(statistic -> statistic.getSatisfied() != null)
                            .mapToLong(InstanceStatistic::getSatisfied).summaryStatistics();
            LongSummaryStatistics toleratedSummaryStatistics =
                    statistics.stream().filter(statistic -> statistic.getTolerated() != null)
                            .mapToLong(InstanceStatistic::getTolerated).summaryStatistics();
            LongSummaryStatistics frustratedSummaryStatistics =
                    statistics.stream().filter(statistic -> statistic.getFrustrated() != null)
                            .mapToLong(InstanceStatistic::getFrustrated).summaryStatistics();
            DoubleSummaryStatistics cpuUsageSummaryStatistics =
                    statistics.stream().filter(statistic -> statistic.getCpuUsage() != null)
                            .mapToDouble(InstanceStatistic::getCpuUsage).summaryStatistics();
            DoubleSummaryStatistics memoryUsageSummaryStatistics =
                    statistics.stream().filter(statistic -> statistic.getMemoryUsage() != null)
                            .mapToDouble(InstanceStatistic::getMemoryUsage).summaryStatistics();

            InstanceVo vo = new InstanceVo();
            vo.setInstanceId(aLong);
            vo.setAppId(appId);
            vo.setResponseTime(format(calculateRate(sumResponseSummaryStatistics.getSum(),
                                                           pvSummaryStatistics.getCount())));
            vo.setCpm(format(calculateRate(pvSummaryStatistics.getSum(), timeRange.getDuration(ChronoUnit.MINUTES))));
            vo.setErrorRate(format(calculateRate(errorSummaryStatistics.getSum(), pvSummaryStatistics.getSum())));
            vo.setInstanceName(instanceIdNameMap.get(aLong));
            vo.setApdex(ApdexUtils.calculateApdex(satisfiedSummaryStatistics.getSum(),
                                                         toleratedSummaryStatistics.getSum(),
                                                         frustratedSummaryStatistics.getSum()));
            vo.setCpuUsage(format(cpuUsageSummaryStatistics.getAverage()));
            vo.setMemoryUsage(format(memoryUsageSummaryStatistics.getAverage()));
            data.add(vo);
        });

        return response;
    }
}
