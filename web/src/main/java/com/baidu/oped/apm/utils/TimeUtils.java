package com.baidu.oped.apm.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.baidu.oped.apm.mvc.vo.TimeRange;

/**
 * Created by mason on 8/27/15.
 */
public class TimeUtils {
    private static final String TIME_RANGE_ELEMENT_SPLITER = "|";

    public static List<TimeRange> convertToRange(String... stringRanges){
        List<TimeRange> timeRanges = new ArrayList<>();
        for (String stringRange : stringRanges) {
            TimeRange timeRange = convertToRange(stringRange);
            timeRanges.add(timeRange);
        }
        return timeRanges;
    }

    public static TimeRange convertToRange(String stringRange) {
        Assert.hasLength(stringRange, "Cannot convert empty string to timeRange");

        String[] pair = stringRange.split(TIME_RANGE_ELEMENT_SPLITER);

        if (pair.length != 2) {
            throw new IllegalArgumentException("invalid time range arguments");
        }

        LocalDateTime from = getLocalDateTime(pair[0]);

        LocalDateTime to = getLocalDateTime(pair[1]);
        return new TimeRange(from, to);
    }

    public static LocalDateTime getLocalDateTime(String stringDatetime) {
        Assert.hasLength(stringDatetime, "cannot convert an empty string to LocalDateTime");
        if (!StringUtils.isEmpty(stringDatetime)) {
            return fromString(stringDatetime);
        } else {
            return LocalDateTime.now();
        }
    }

    public static LocalDateTime fromString(String stringTime) {
        Assert.hasLength(stringTime, "cannot convert an empty string to LocalDateTime");
        return LocalDateTime.parse(stringTime, DateTimeFormatter.ofPattern(Constaints.TIME_PATTERN));
    }
}
