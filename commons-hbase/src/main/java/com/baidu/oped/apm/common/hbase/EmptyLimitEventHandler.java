
package com.baidu.oped.apm.common.hbase;

import org.apache.hadoop.hbase.client.Result;

/**
 * class EmptyLimitEventHandler 
 *
 * @author meidongxu@baidu.com
 */
public class EmptyLimitEventHandler implements LimitEventHandler{

    @Override
    public void handleLastResult(Result lastResult) {
    }
}
