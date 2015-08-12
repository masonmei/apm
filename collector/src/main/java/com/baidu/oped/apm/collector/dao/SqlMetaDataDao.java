
package com.baidu.oped.apm.collector.dao;

import com.baidu.oped.apm.thrift.dto.TSqlMetaData;

/**
 * class SqlMetaDataDao 
 *
 * @author meidongxu@baidu.com
 */
public interface SqlMetaDataDao {
    void insert(TSqlMetaData sqlMetaData);
}
