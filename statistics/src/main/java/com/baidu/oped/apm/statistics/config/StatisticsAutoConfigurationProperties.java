package com.baidu.oped.apm.statistics.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by mason on 8/29/15.
 */
@ConfigurationProperties(prefix = "apm.statistics")
public class StatisticsAutoConfigurationProperties {
    private long webTransationDelay;

    public long getWebTransationDelay() {
        return webTransationDelay;
    }

    public void setWebTransationDelay(long webTransationDelay) {
        this.webTransationDelay = webTransationDelay;
    }

}
