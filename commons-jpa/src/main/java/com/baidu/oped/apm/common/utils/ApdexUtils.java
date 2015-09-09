package com.baidu.oped.apm.common.utils;

/**
 * Created by mason on 8/13/15.
 */
public abstract class ApdexUtils {
    public static Double calculateApdex(Long satisfied, Long tolerated, Long frustrated) {
        if(satisfied == null) {
            satisfied = 0l;
        }

        if(tolerated == null){
            tolerated = 0l;
        }

        if(frustrated == null){
            frustrated = 0l;
        }

        long sat = 2 * satisfied + tolerated;
        long all = (frustrated + tolerated + satisfied) * 2;
        return NumberUtils.calculateRate(sat, all);
    }

    public enum Level {
        SATISFIED,
        TOLERATED,
        FRUSTRATED;

        public static Level getLevel(double apdexTime, double responseTime) {
            if (responseTime < 0) {
                throw new IllegalArgumentException("the value to calculate Apdex Level cannot be negative.");
            }
            if (apdexTime <= 0) {
                throw new IllegalArgumentException("apdex time must be positive.");
            }

            if (responseTime <= apdexTime) {
                return SATISFIED;
            } else if (responseTime <= 4 * apdexTime) {
                return TOLERATED;
            } else {
                return FRUSTRATED;
            }
        }
    }
}
