
package com.baidu.oped.apm.collector.dao;

import com.baidu.oped.apm.thrift.dto.TApiMetaData;

/**
 * class ApiMetaDataDao 
 *
 * @author meidongxu@baidu.com
 */
public interface ApiMetaDataDao {

    void insert(TApiMetaData apiMetaData);
}
