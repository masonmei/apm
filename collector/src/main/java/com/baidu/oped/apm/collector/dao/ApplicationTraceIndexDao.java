
package com.baidu.oped.apm.collector.dao;

import com.baidu.oped.apm.thrift.dto.TSpan;

/**
 * class ApplicationTraceIndexDao 
 *
 * @author meidongxu@baidu.com
 */
public interface ApplicationTraceIndexDao {
    void insert(TSpan span);
}
