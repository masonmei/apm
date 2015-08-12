
package com.baidu.oped.apm.profiler.logging;

import com.baidu.oped.apm.bootstrap.logging.PLoggerBinder;
import com.baidu.oped.apm.bootstrap.logging.PLoggerFactory;

/**
 * For unit test to register/unregister loggerBinder.
 *
 * @author emeroad
 */
public class Slf4jLoggerBinderInitializer {

    private static final PLoggerBinder loggerBinder = new Slf4jLoggerBinder();

    public static void beforeClass() {
        PLoggerFactory.initialize(loggerBinder);
    }

    public static void afterClass() {
        PLoggerFactory.unregister(loggerBinder);
    }
}
