package com.baidu.oped.apm.bootstrap;

import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.oped.apm.bootstrap.config.ProfilerConfig;

/**
 * class AgentClassLoaderTest
 *
 * @author meidongxu@baidu.com
 */
public class AgentClassLoaderTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void boot() throws IOException, ClassNotFoundException {
        AgentClassLoader agentClassLoader = new AgentClassLoader(new URL[0]);
        agentClassLoader.setBootClass("com.baidu.oped.apm.bootstrap.DummyAgent");
        agentClassLoader.boot("agentPath", "test", new DummyInstrumentation(), new ProfilerConfig());
        // TODO need verification - implementation for obtaining logger changed
        //        PLoggerBinder loggerBinder = (PLoggerBinder) agentClassLoader.initializeLoggerBinder();
        //        PLogger test = loggerBinder.getLogger("test");
        //        test.info("slf4j logger test");

    }

    private String getProjectLibDir() {
        // not really necessary, but useful for testing protectionDomain
        ProtectionDomain protectionDomain = AgentClassLoader.class.getProtectionDomain();
        CodeSource codeSource = protectionDomain.getCodeSource();
        URL location = codeSource.getLocation();

        logger.info("lib location:" + location);
        String path = location.getPath();
        // file:/D:/nhn_source/pinpoint_project/pinpoint-tomcat-profiler/target/classes/
        int dirPath = path.lastIndexOf("target/classes/");
        if (dirPath == -1) {
            throw new RuntimeException("target/classes/ not found");
        }
        String projectDir = path.substring(1, dirPath);
        return projectDir + "src/test/lib";
    }
}
