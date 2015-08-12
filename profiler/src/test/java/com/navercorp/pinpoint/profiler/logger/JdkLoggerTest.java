
package com.baidu.oped.apm.profiler.logger;

import org.junit.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * class JdkLoggerTest 
 *
 * @author meidongxu@baidu.com
 */
public class JdkLoggerTest {
    @Test
    public void test() {
        Logger logger = Logger.getLogger(this.getClass().getName());

        logger.info("tset");

        // formatting is not supported
        logger.log(Level.INFO, "Test %s", "sdfsdf");

        logger.log(Level.INFO, "Test ", new Exception());

        logger.logp(Level.INFO, JdkLoggerTest.class.getName(), "test()", "tsdd");

        // Should not log this because log level for test defined in logging.properties is "fine".
        logger.finest("logged?");

    }
}
