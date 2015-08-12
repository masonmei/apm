
package com.baidu.oped.apm.common.util;

/**
 * class DefaultTimeSlot 
 *
 * @author meidongxu@baidu.com
 */
public class DefaultTimeSlot implements TimeSlot {

    private static final long ONE_MIN_RESOLUTION =  60000; // 1min

    private final long resolution;

    public DefaultTimeSlot() {
        this(ONE_MIN_RESOLUTION);
    }

    public DefaultTimeSlot(long resolution) {
        this.resolution = resolution;
    }

    @Override
    public long getTimeSlot(long time) {
        // not necessary to add ONE_MIN_RESOLUTION as all the timeslots are based on the start value of the given time.
        return (time / resolution) * resolution;
    }
}
