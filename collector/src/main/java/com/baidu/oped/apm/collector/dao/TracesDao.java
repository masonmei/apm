
package com.baidu.oped.apm.collector.dao;

import com.baidu.oped.apm.thrift.dto.TSpan;
import com.baidu.oped.apm.thrift.dto.TSpanChunk;

/**
 * class TracesDao 
 *
 * @author meidongxu@baidu.com
 */
public interface TracesDao {
    void insert(TSpan span);

    void insertSpanChunk(TSpanChunk spanChunk);
}
