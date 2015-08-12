
package com.baidu.oped.apm.collector.dao;

/**
 * class MapStatisticsCalleeDao 
 *
 * @author meidongxu@baidu.com
 */
public interface MapStatisticsCalleeDao extends CachedStatisticsDao {
    void update(String calleeApplicationName, short calleeServiceType, String callerApplicationName,
                short callerServiceType, String callerHost, int elapsed, boolean isError);
}
