package com.baidu.oped.apm.mvc.controller;

import com.baidu.oped.apm.mvc.vo.TimeRange;
import com.baidu.oped.apm.utils.TimeUtils;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;

import static java.lang.String.format;

/**
 * Used to defined a serious of validation.
 *
 * @author mason(meidongxu@baidu.com)
 */
public abstract class BaseController {
    protected static final Long DEFAULT_PERIOD = 60L;

    /**
     * Validate the application id, if invalid build the message with usage.
     *
     * @param appId the application id
     * @param usage the purpose of the this validation
     */
    protected void validApplicationId(Long appId, String usage) {
        Assert.notNull(appId, format("Application Id must not be null while %s", usage));
        Assert.state(appId > 0, format("Application id must be positive while %s", usage));
    }

    /**
     * Validate the instance id, if invalid build the message with usage.
     *
     * @param instanceId the application id
     * @param usage      the purpose of the this validation
     */
    public void validInstanceId(Long instanceId, String usage) {
        Assert.notNull(instanceId, format("Instance Id must not be null while %s", usage));
        Assert.state(instanceId > 0, format("Instance id must be positive while %s", usage));
    }

    /**
     * Validate the TimeRange, id invalid build the message with usage.
     *
     * @param from  the timeRange start
     * @param to    the timeRange end
     * @param usage the purpose of this validation
     */
    public void validTimeRange(LocalDateTime from, LocalDateTime to, String usage) {
        Assert.notNull(from, format("Time range start must not be null while %s", usage));
        Assert.notNull(to, format("Time range end must not be null while %s", usage));
        Assert.state(from.isBefore(to), "Time range start must before than end.");
    }

    /**
     * Validate the TimeRanges given in | split string, single required. id invalid build the message with usage.
     *
     * @param timeRangeStrings the timeRanges
     * @param usage            the purpose of this validation
     */
    public void validSingleTimeRanges(String[] timeRangeStrings, String usage) {
        List<TimeRange> timeRanges = TimeUtils.convertToRange(timeRangeStrings);
        Assert.state(timeRanges.size() == 1, format("Single Time range required while %s", usage));
        for (TimeRange timeRange : timeRanges) {
            validTimeRange(timeRange.getFrom(), timeRange.getTo(), usage);
        }
    }

    /**
     * Validate the TimeRanges given in | split string, id invalid build the message with usage.
     *
     * @param timeRangeStrings the timeRanges
     * @param usage            the purpose of this validation
     */
    public void validTimeRanges(String[] timeRangeStrings, String usage) {
        List<TimeRange> timeRanges = TimeUtils.convertToRange(timeRangeStrings);
        for (TimeRange timeRange : timeRanges) {
            validTimeRange(timeRange.getFrom(), timeRange.getTo(), usage);
        }
    }

    /**
     * Validate a period, if invalid build the message with usage.
     *
     * @param period the period
     * @param usage  the purpose of this validation
     */
    public void validPeriod(Long period, String usage) {
        Assert.notNull(period, format("Period must be provided while %s", usage));
        Assert.state(period % 60 == 0 && period > 0, "Period must be a multiple of 60");
    }

    /**
     * Validate the limit, if invalid build the message with usage.
     *
     * @param limit the limit
     * @param usage the purpose of the validation
     */
    public void validLimit(Integer limit, String usage) {
        Assert.state(limit > 0, format("Limit must be positive while %s", usage));
    }

    /**
     * Valid the sort information, if invalid build the message with usage.
     *
     * @param orderBy sort information
     * @param usage   the purpose of the validation
     */
    public void validSort(String orderBy, String usage) {
        Assert.notNull(orderBy, format("Order by must mot be null while %s", usage));
    }

    /**
     * Valid the page info, if invalid build the message with usage.
     *
     * @param pageCount the page count
     * @param pageSize  the page size
     * @param usage     the purpose of the validation
     */
    public void validPageInfo(Integer pageCount, Integer pageSize, String usage) {
        Assert.isNull(pageCount, format("Page Count must not be null while %s", usage));
        Assert.state(pageCount > 0, format("Page Count must be from 1 while %s", usage));
        Assert.notNull(pageSize, format("Page Size must not be null while %s", usage));
        Assert.state(pageSize > 0, format("Page Size must be positive while %s", usage));
    }


    /**
     * Valid the Trace Id, if invalid build the message with usage.
     *
     * @param traceId the transaction trace Id
     * @param usage   the purpose of the validation
     */
    public void validTraceId(Long traceId, String usage) {
        Assert.notNull(traceId, format("Trace id must not be null while %s", usage));
        Assert.state(traceId > 0, format("Trace id must be positive while %s", usage));
    }
}
