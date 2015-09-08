package com.baidu.oped.apm.common.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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

    public static double getRange(final LocalDateTime from, final LocalDateTime to, ChronoUnit unit) {
        long fromMillis = from.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long toMillis = to.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return (toMillis - fromMillis) / factorToMillis(unit);
    }

    public static List<Long> getTimestamps(LocalDateTime from , LocalDateTime to, Long periodInMillis){
        final List<Long> timestamps = new ArrayList<>();
        LocalDateTime periodStart = TimeUtil.getPeriodStart(from, periodInMillis, ChronoUnit.MILLIS);
        while(true){
            if(periodStart.isBefore(from)){
                periodStart = periodStart.plus(periodInMillis, ChronoUnit.MILLIS);
                continue;
            }

            if(periodStart.isAfter(to)){
                break;
            }

            timestamps.add(periodStart.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            periodStart = periodStart.plus(periodInMillis, ChronoUnit.MILLIS);
        }
        return timestamps;
    }
}
