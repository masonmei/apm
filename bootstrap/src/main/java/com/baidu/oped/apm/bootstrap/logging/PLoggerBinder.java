package com.baidu.oped.apm.bootstrap.logging;

/**
 * class PLoggerBinder
 *
 * @author meidongxu@baidu.com
 */
public interface PLoggerBinder {
    PLogger getLogger(String name);

    void shutdown();
}
