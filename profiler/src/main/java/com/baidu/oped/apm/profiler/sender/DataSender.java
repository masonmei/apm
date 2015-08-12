
package com.baidu.oped.apm.profiler.sender;

import org.apache.thrift.TBase;

/**
 * class DataSender 
 *
 * @author meidongxu@baidu.com
 */
public interface DataSender {

    boolean send(TBase<?, ?> data);

    void stop();

    boolean isNetworkAvailable();
}
