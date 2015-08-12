
package com.baidu.oped.apm.collector.dao;

/**
 * class HostApplicationMapDao 
 *
 * @author meidongxu@baidu.com
 */
public interface HostApplicationMapDao {
    void insert(String host, String bindApplicationName, short bindServiceType, String parentApplicationName,
                short parentServiceType);
}
