package com.baidu.oped.apm.common.utils;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.Test;

/**
 * Created by mason on 9/7/15.
 */
public class TimeUtilTest {

    @Test
    public void testGetPeriodStart() throws Exception {
        LocalDateTime periodStart = TimeUtil.getPeriodStart(LocalDateTime.now(), 1, ChronoUnit.MINUTES);
        assertEquals(0, periodStart.getSecond());
        assertEquals(0, periodStart.getNano());
        periodStart = TimeUtil.getPeriodStart(LocalDateTime.now(), 1, ChronoUnit.HOURS);
        assertEquals(0, periodStart.getMinute());
        assertEquals(0, periodStart.getSecond());
        assertEquals(0, periodStart.getNano());
        periodStart = TimeUtil.getPeriodStart(LocalDateTime.of(2015, 9, 5, 23, 14, 23, 23), 5, ChronoUnit.MINUTES);
        assertEquals(10, periodStart.getMinute());
        assertEquals(0, periodStart.getSecond());
        assertEquals(0, periodStart.getNano());
        periodStart = TimeUtil.getPeriodStart(LocalDateTime.of(2015, 9, 5, 23, 14, 23, 23), 2, ChronoUnit.MINUTES);
        assertEquals(14, periodStart.getMinute());
        assertEquals(0, periodStart.getSecond());
        assertEquals(0, periodStart.getNano());
    }

    @Test
    public void testFactorToMillis() throws Exception {
        long factor = TimeUtil.factorToMillis(ChronoUnit.SECONDS);
        assertEquals(1000l, factor);
        factor = TimeUtil.factorToMillis(ChronoUnit.HOURS);
        assertEquals(3600000l, factor);
    }

    @Test
    public void testGetRange() throws Exception {

    }

    @Test
    public void testGetTimestamps() throws Exception {
        LocalDateTime from = LocalDateTime.of(2015, 9, 5, 22, 14, 23, 23);
        LocalDateTime to = LocalDateTime.of(2015, 9, 5, 23, 14, 23, 23);

        List<Long> timestamps = TimeUtil.getTimestamps(from, to, 60000l);
        assertEquals(60, timestamps.size());

        timestamps = TimeUtil.getTimestamps(from, to, 300000l);
        assertEquals(12, timestamps.size());

    }
}