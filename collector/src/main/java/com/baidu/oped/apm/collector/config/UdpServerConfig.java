package com.baidu.oped.apm.collector.config;

/**
 * Created by mason on 11/12/15.
 */
public interface UdpServerConfig extends ServerConfig {
    int getBufferSize();

    boolean isCollectMetric();
}
