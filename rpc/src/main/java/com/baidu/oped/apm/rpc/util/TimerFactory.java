
package com.baidu.oped.apm.rpc.util;

import com.baidu.oped.apm.common.util.PinpointThreadFactory;

import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.ThreadNameDeterminer;

import java.util.concurrent.TimeUnit;

/**
 * class TimerFactory 
 *
 * @author meidongxu@baidu.com
 */
public class TimerFactory {

    public static HashedWheelTimer createHashedWheelTimer(String threadName, long tickDuration, TimeUnit unit, int ticksPerWheel) {
        final PinpointThreadFactory threadFactory = new PinpointThreadFactory(threadName, true);
        return new HashedWheelTimer(threadFactory, ThreadNameDeterminer.CURRENT, tickDuration, unit, ticksPerWheel);
    }
}
