package com.baidu.oped.apm.config.common;

import java.util.TimeZone;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.annotation.Configuration;

/**
 * class UTCTimeZoneConfiguration 
 *
 * @author meidongxu@baidu.com
 */


@Configuration
public class UTCTimeZoneConfiguration implements ServletContextListener {

    public void contextInitialized(ServletContextEvent event) {
        System.setProperty("user.timezone", "UTC");
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public void contextDestroyed(ServletContextEvent event) {}

}
