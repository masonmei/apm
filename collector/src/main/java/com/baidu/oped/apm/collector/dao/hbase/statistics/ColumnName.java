
package com.baidu.oped.apm.collector.dao.hbase.statistics;

/**
 * class ColumnName 
 *
 * @author meidongxu@baidu.com
 */
public interface ColumnName {
    byte[] getColumnName();

    long getCallCount();

    void setCallCount(long callCount);
}
