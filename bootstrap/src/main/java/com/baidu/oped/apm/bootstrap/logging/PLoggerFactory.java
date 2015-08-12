package com.baidu.oped.apm.bootstrap.logging;

import java.util.logging.Logger;

/**
 * class PLoggerFactory
 *
 * @author meidongxu@baidu.com
 */
public final class PLoggerFactory {

    private static PLoggerBinder loggerBinder;

    public static void initialize(PLoggerBinder loggerBinder) {
        if (PLoggerFactory.loggerBinder == null) {
            PLoggerFactory.loggerBinder = loggerBinder;
        } else {
            final Logger logger = Logger.getLogger(PLoggerFactory.class.getName());
            logger.warning("loggerBinder is not null");
        }
    }

    public static void unregister(PLoggerBinder loggerBinder) {
        // Limited to remove only the ones already registered
        // when writing a test case, logger register/unregister logic must be located in beforeClass and afterClass
        if (loggerBinder == PLoggerFactory.loggerBinder) {
            PLoggerFactory.loggerBinder = null;
        }
    }

    public static PLogger getLogger(String name) {
        if (loggerBinder == null) {
            // this prevents null exception: need to return Dummy until a Binder is assigned
            return DummyPLogger.INSTANCE;
        }
        return loggerBinder.getLogger(name);
    }

    public static PLogger getLogger(Class clazz) {
        if (clazz == null) {
            throw new NullPointerException("class must not be null");
        }
        return getLogger(clazz.getName());
    }
}
