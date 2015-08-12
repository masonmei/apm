
package com.baidu.oped.apm.profiler.monitor;

/**
 * class MonitorName 
 *
 * @author meidongxu@baidu.com
 */
public class MonitorName {

    private final String name;

    public MonitorName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return "MonitorName(" + this.name + ")";
    }

}
