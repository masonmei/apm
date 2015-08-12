
package com.baidu.oped.apm.mvc.vo;

import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.oped.apm.common.util.TimeSlot;

/**
 * class RangeFactory 
 *
 * @author meidongxu@baidu.com
 */
public class RangeFactory {
    @Autowired
    private TimeSlot timeSlot;

    /**
     * Create minute-based reversed Range for statistics
     * 
     * @param range
     * @return
     */
    public Range createStatisticsRange(Range range) {
        if (range == null) {
            throw new NullPointerException("range must not be null");
        }
        // HBase scanner does not include endTime when scanning, so 1 is usually added to the endTime.
        // In this case, the Range is reversed, so we instead subtract 1 from the startTime.
        final long startTime = timeSlot.getTimeSlot(range.getFrom()) - 1;
        final long endTime = timeSlot.getTimeSlot(range.getTo());
        return Range.createUncheckedRange(startTime, endTime);
    }

}
