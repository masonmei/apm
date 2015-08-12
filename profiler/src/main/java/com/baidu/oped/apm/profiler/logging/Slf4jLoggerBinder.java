
package com.baidu.oped.apm.profiler.logging;

import com.baidu.oped.apm.bootstrap.logging.PLogger;
import com.baidu.oped.apm.bootstrap.logging.PLoggerBinder;

import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * class Slf4jLoggerBinder 
 *
 * @author meidongxu@baidu.com
 */
public class Slf4jLoggerBinder implements PLoggerBinder {

    private ConcurrentMap<String, PLogger> loggerCache = new ConcurrentHashMap<String, PLogger>(256, 0.75f, 128);

    @Override
    public PLogger getLogger(String name) {

        final PLogger hitPLogger = loggerCache.get(name);
        if (hitPLogger != null) {
            return hitPLogger;
        }

        org.slf4j.Logger slf4jLogger = LoggerFactory.getLogger(name);

        final Slf4jPLoggerAdapter slf4jLoggerAdapter = new Slf4jPLoggerAdapter(slf4jLogger);
        final PLogger before = loggerCache.putIfAbsent(name, slf4jLoggerAdapter);
        if (before != null) {
            return before;
        }
        return slf4jLoggerAdapter;
    }

    @Override
    public void shutdown() {
        // Maybe we don't need to do this. Unregistering LoggerFactory would be enough.
        loggerCache = null;
    }
}
