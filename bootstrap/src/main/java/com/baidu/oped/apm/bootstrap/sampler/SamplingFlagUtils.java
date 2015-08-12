package com.baidu.oped.apm.bootstrap.sampler;

/**
 * class SamplingFlagUtils
 *
 * @author meidongxu@baidu.com
 */
public final class SamplingFlagUtils {

    // 1 byte dummy mark for further expansion of sampling specs
    public static final String SAMPLING_RATE_PREFIX = "s";

    public static final String SAMPLING_RATE_FALSE = SAMPLING_RATE_PREFIX + "0";
    public static final String SAMPLING_RATE_TRUE = SAMPLING_RATE_PREFIX + "1";

    private SamplingFlagUtils() {
    }

    public static boolean isSamplingFlag(String samplingFlag) {
        if (samplingFlag == null) {
            return true;
        }
        // we turn off sampling only when a specific flag was given
        // XXX needs better detection mechanism through prefix parsing
        if (samplingFlag.startsWith(SAMPLING_RATE_PREFIX)) {
            return !SAMPLING_RATE_FALSE.equals(samplingFlag);
        }
        return true;
    }
}

