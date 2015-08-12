
package com.baidu.oped.apm.collector.util;

import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;

/**
 * class ThreadLocalAcceptedTimeService 
 *
 * @author meidongxu@baidu.com
 */
@Component
public class ThreadLocalAcceptedTimeService implements AcceptedTimeService {

    private final ThreadLocal<Long> local = new NamedThreadLocal<Long>("AcceptedTimeService");

    @Override
    public void accept() {
        accept(System.currentTimeMillis());
    }

    @Override
    public void accept(long time) {
        local.set(time);
    }

    @Override
    public long getAcceptedTime() {
        Long acceptedTime = local.get();
        if (acceptedTime == null) {
            return System.currentTimeMillis();
        }
        return acceptedTime;
    }
}
