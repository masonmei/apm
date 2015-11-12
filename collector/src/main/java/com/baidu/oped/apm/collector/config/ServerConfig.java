package com.baidu.oped.apm.collector.config;

/**
 * Created by mason on 11/12/15.
 */
public interface ServerConfig {
    String getReceiverName();

    String getListenIp();

    int getListenPort();

    int getWorkerThread();

    int getWorkerQueueSize();
}
