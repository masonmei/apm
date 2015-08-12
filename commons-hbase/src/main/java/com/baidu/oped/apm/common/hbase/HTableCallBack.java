
package com.baidu.oped.apm.common.hbase;

import org.apache.hadoop.hbase.client.HTable;

import java.io.IOException;

/**
 * class HTableCallBack 
 *
 * @author meidongxu@baidu.com
 */
public interface HTableCallBack {
    void doExecute(HTable hTable) throws IOException;

//    void doMultiExecute(HTable... tables) throws IOException;
}
