
package com.baidu.oped.apm.profiler.monitor.metric;

import com.baidu.oped.apm.common.ServiceType;

/**
 * class ContextMetric 
 *
 * @author meidongxu@baidu.com
 */
public class ContextMetric {
    // Response time of WAS
    private final Histogram responseMetric;
    // Response time to unknown user
    private final Histogram userHistogram;

    private final ServiceType contextServiceType;

    // Response time to known peer
    private final AcceptHistogram acceptHistogram = new DefaultAcceptHistogram();

    public ContextMetric(ServiceType contextServiceType) {
        if (contextServiceType == null) {
            throw new NullPointerException("contextServiceType must not be null");
        }

        this.contextServiceType = contextServiceType;

        this.responseMetric = new LongAdderHistogram(contextServiceType);
        this.userHistogram = new LongAdderHistogram(contextServiceType);
    }

    public void addResponseTime(int millis) {
        this.responseMetric.addResponseTime(millis);
    }

    public void addAcceptHistogram(String parentApplicationName, short serviceType, int millis) {
        if (parentApplicationName == null) {
            throw new NullPointerException("parentApplicationName must not be null");
        }
        this.acceptHistogram.addResponseTime(parentApplicationName, serviceType, millis);
    }

    public void addUserAcceptHistogram(int millis) {
        this.userHistogram.addResponseTime(millis);
    }




}
