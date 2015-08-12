package com.baidu.oped.apm.bootstrap.config;

import java.io.IOException;
import java.util.Properties;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.Assert;

/**
 * This is mason
 *
 * @author
 */
public class ProfilerConfigTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void defaultProfilableClassFilter() throws IOException {
        ProfilerConfig profilerConfig = new ProfilerConfig();
        Filter<String> profilableClassFilter = profilerConfig.getProfilableClassFilter();
        Assert.assertFalse(profilableClassFilter.filter("net/spider/king/wang/Jjang"));
    }

    @Test
    public void readProperty() throws IOException {
        String path = ProfilerConfig.class.getResource("/com/baidu/oped/apm/bootstrap/config/test.property").getPath();
        logger.debug("path:{}", path);

        ProfilerConfig profilerConfig = ProfilerConfig.load(path);
    }

    @Test
    public void testPlaceHolder() throws IOException {
        Properties properties = new Properties();
        properties.setProperty("profiler.collector.span.ip", "${test1}");
        properties.setProperty("profiler.collector.stat.ip", "${test1}");
        properties.setProperty("profiler.collector.tcp.ip", "${test2}");
        // placeHolderValue
        properties.setProperty("test1", "placeHolder1");
        properties.setProperty("test2", "placeHolder2");

        ProfilerConfig profilerConfig = new ProfilerConfig(properties);

        Assert.assertEquals(profilerConfig.getCollectorSpanServerIp(), "placeHolder1");
        Assert.assertEquals(profilerConfig.getCollectorStatServerIp(), "placeHolder1");
        Assert.assertEquals(profilerConfig.getCollectorTcpServerIp(), "placeHolder2");
    }

    @Test
    public void ioBufferingTest() throws IOException {
        String path = ProfilerConfig.class.getResource("/com/baidu/oped/apm/bootstrap/config/test.property").getPath();
        logger.debug("path:{}", path);

        ProfilerConfig profilerConfig = ProfilerConfig.load(path);

        Assert.assertEquals(profilerConfig.isIoBufferingEnable(), false);
        Assert.assertEquals(profilerConfig.getIoBufferingBufferSize(), 30);
    }

    @Test
    public void ioBufferingDefault() throws IOException {
        String path =
                ProfilerConfig.class.getResource("/com/baidu/oped/apm/bootstrap/config/default.property").getPath();
        logger.debug("path:{}", path);

        ProfilerConfig profilerConfig = ProfilerConfig.load(path);

        Assert.assertEquals(profilerConfig.isIoBufferingEnable(), true);
        Assert.assertEquals(profilerConfig.getIoBufferingBufferSize(), 10);
    }

    @Test
    public void tcpCommandAcceptrConfigTest1() throws IOException {
        String path = ProfilerConfig.class.getResource("/com/baidu/oped/apm/bootstrap/config/test.property").getPath();
        logger.debug("path:{}", path);

        ProfilerConfig profilerConfig = ProfilerConfig.load(path);

        Assert.assertFalse(profilerConfig.isTcpDataSenderCommandAcceptEnable());
    }

    @Test
    public void tcpCommandAcceptrConfigTest2() throws IOException {
        String path = ProfilerConfig.class.getResource("/com/baidu/oped/apm/bootstrap/config/test2.property").getPath();
        logger.debug("path:{}", path);

        ProfilerConfig profilerConfig = ProfilerConfig.load(path);

        Assert.assertTrue(profilerConfig.isTcpDataSenderCommandAcceptEnable());
    }

}
