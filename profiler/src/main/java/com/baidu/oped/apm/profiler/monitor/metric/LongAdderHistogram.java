
package com.baidu.oped.apm.profiler.monitor.metric;

import com.baidu.oped.apm.common.HistogramSchema;
import com.baidu.oped.apm.common.HistogramSlot;
import com.baidu.oped.apm.common.ServiceType;
import com.baidu.oped.apm.common.SlotType;
import com.baidu.oped.apm.profiler.util.jdk.LongAdder;

/**
 * class LongAdderHistogram 
 *
 * @author meidongxu@baidu.com
 */
public class LongAdderHistogram implements Histogram {
    // We could use LongAdder only for fastCounter and AtomicLong for the others.
    private final LongAdder fastCounter = new LongAdder();
    private final LongAdder normalCounter = new LongAdder();
    private final LongAdder slowCounter = new LongAdder();
    private final LongAdder verySlowCounter = new LongAdder();

    private final LongAdder errorCounter = new LongAdder();

    private final short serviceType;
    private final HistogramSchema histogramSchema;

    public LongAdderHistogram(ServiceType serviceType) {
        this(serviceType.getCode(), serviceType.getHistogramSchema());
    }

    public LongAdderHistogram(short serviceType, HistogramSchema histogramSchema) {
        this.serviceType = serviceType;
        this.histogramSchema = histogramSchema;
    }

    public short getServiceType() {
        return serviceType;
    }

    public void addResponseTime(int millis) {
        final HistogramSlot histogramSlot = histogramSchema.findHistogramSlot(millis);
        final SlotType slotType = histogramSlot.getSlotType();
        switch (slotType) {
            case FAST:
                fastCounter.increment();
                return;
            case NORMAL:
                normalCounter.increment();
                return;
            case SLOW:
                slowCounter.increment();
                return;
            case VERY_SLOW:
                verySlowCounter.increment();
                return;
            case ERROR:
                errorCounter.increment();
                return;
            default:
                throw new IllegalArgumentException("slot Type notFound:" + slotType);
        }
    }


    public HistogramSnapshot createSnapshot() {
        long fast = fastCounter.sum();
        long normal = normalCounter.sum();
        long slow = slowCounter.sum();
        long verySlow = verySlowCounter.sum();
        long error = errorCounter.sum();

        return new HistogramSnapshot(this.serviceType, fast, normal, slow, verySlow, error);
    }

    @Override
    public String toString() {
        return "LongAdderHistogram{" +
                "fastCounter=" + fastCounter +
                ", normalCounter=" + normalCounter +
                ", slowCounter=" + slowCounter +
                ", verySlowCounter=" + verySlowCounter +
                ", errorCounter=" + errorCounter +
                ", serviceType=" + serviceType +
                '}';
    }
}
