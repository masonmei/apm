
package com.baidu.oped.apm.profiler.monitor.metric;

import com.baidu.oped.apm.bootstrap.context.Metric;

import java.util.Collection;

/**
 * class RpcMetric 
 *
 * @author meidongxu@baidu.com
 */
public interface RpcMetric extends Metric {

    void addResponseTime(String destinationId, int millis);

    Collection<HistogramSnapshot> createSnapshotList();

}
