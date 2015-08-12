package com.baidu.oped.apm.bootstrap;

/**
 * class ProfilerLibClass
 *
 * @author meidongxu@baidu.com
 */
public class ProfilerLibClass {

    private static final String[] PINPOINT_PROFILER_CLASS =
            new String[] {"com.baidu.oped.apm.profiler", "com.baidu.oped.apm.thrift", "com.baidu.oped.apm.rpc",
                                 "javassist", "org.slf4j", "org.apache.thrift", "org.jboss.netty", "com.google.common",
                                 "org.apache.commons.lang", "org.apache.log4j", "com.codahale.metrics"};

    public boolean onLoadClass(String clazzName) {
        final int length = PINPOINT_PROFILER_CLASS.length;
        for (int i = 0; i < length; i++) {
            if (clazzName.startsWith(PINPOINT_PROFILER_CLASS[i])) {
                return true;
            }
        }
        return false;
    }
}
