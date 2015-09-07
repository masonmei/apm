package com.baidu.oped.apm.statistics.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

/**
 * Created by mason on 9/5/15.
 */
public class TimeUtil {
    public static LocalDateTime getPeriodStart(final LocalDateTime time, final long period, final ChronoUnit unit) {
        long periodInMillis = period * factorToMillis(unit);
        long epochSecond = time.atZone(ZoneId.systemDefault()).toEpochSecond();
        long millisToReduce = epochSecond * 1000 % periodInMillis;
        LocalDateTime minus = time.minus(millisToReduce, ChronoUnit.MILLIS);
        return minus.minusNanos(minus.getNano());
    }

    public static long factorToMillis(ChronoUnit from) {
        long fromMillis = from.getDuration().toMillis();
        long toMillis = ChronoUnit.MILLIS.getDuration().toMillis();
        return fromMillis / toMillis;
    }

}
