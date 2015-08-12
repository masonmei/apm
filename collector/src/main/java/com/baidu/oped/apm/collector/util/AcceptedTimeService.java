
package com.baidu.oped.apm.collector.util;

/**
 * class AcceptedTimeService 
 *
 * @author meidongxu@baidu.com
 */
public interface AcceptedTimeService {

    void accept();

    void accept(long time);

    long getAcceptedTime();
}