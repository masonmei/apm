
package com.baidu.oped.apm.common.hbase;

import org.apache.hadoop.hbase.client.Result;

/**
 * class LimitEventHandler 
 *
 * @author meidongxu@baidu.com
 */
public interface LimitEventHandler {
    void handleLastResult(Result lastResult);
}
