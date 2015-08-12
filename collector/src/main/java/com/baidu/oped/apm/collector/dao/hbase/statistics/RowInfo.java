
package com.baidu.oped.apm.collector.dao.hbase.statistics;

/**
 * class RowInfo 
 *
 * @author meidongxu@baidu.com
 */
public interface RowInfo {

    RowKey getRowKey();

    ColumnName getColumnName();

}
