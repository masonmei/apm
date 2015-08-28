package com.baidu.oped.apm.utils;

/**
 * Created by mason on 8/13/15.
 */
public class ApdexUtils {
    public static Double calculateApdex(Long satisfied, Long tolerated, Long frustrated) {
        return null;
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
