
package com.baidu.oped.apm.collector.dao;

/**
 * class MapResponseTimeDao 
 *
 * @author meidongxu@baidu.com
 */
public interface MapResponseTimeDao extends CachedStatisticsDao {
    void received(String applicationName, short serviceType, String agentId, int elapsed, boolean isError);
}
