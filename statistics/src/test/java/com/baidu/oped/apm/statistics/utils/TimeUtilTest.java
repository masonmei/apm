package com.baidu.oped.apm.statistics.utils;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.Test;

/**
 * Created by mason on 9/5/15.
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
    public void testFactorToSecond() throws Exception {
        long factor = TimeUtil.factorToMillis(ChronoUnit.SECONDS);
        assertEquals(1000l, factor);
        factor = TimeUtil.factorToMillis(ChronoUnit.HOURS);
        assertEquals(3600000l, factor);
    }
}