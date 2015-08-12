
package com.baidu.oped.apm.collector.dao;

import com.baidu.oped.apm.thrift.dto.TStringMetaData;

/**
 * class StringMetaDataDao 
 *
 * @author meidongxu@baidu.com
 */
public interface StringMetaDataDao {

    void insert(TStringMetaData stringMetaData);
}
