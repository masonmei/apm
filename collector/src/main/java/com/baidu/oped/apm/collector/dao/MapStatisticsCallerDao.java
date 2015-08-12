
package com.baidu.oped.apm.collector.dao;

/**
 * class MapStatisticsCallerDao 
 *
 * @author meidongxu@baidu.com
 */
public interface MapStatisticsCallerDao extends CachedStatisticsDao {
    void update(String callerApplicationName, short callerServiceType, String callerAgentId,
                String calleeApplicationName, short calleeServiceType, String calleeHost, int elapsed, boolean isError);
}
