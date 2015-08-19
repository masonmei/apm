package com.baidu.oped.apm.bootstrap;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.baidu.oped.apm.ProductInfo;
import com.baidu.oped.apm.bootstrap.config.ProfilerConfig;
import com.baidu.oped.apm.bootstrap.util.IdValidateUtils;
import com.baidu.oped.apm.common.PinpointConstants;
import com.baidu.oped.apm.common.util.BytesUtils;

/**
 * class PinpointBootStrap
 *
 * @author meidongxu@baidu.com
 */
public class PinpointBootStrap {

    public static final String BOOT_CLASS = "com.baidu.oped.apm.profiler.DefaultAgent";
    private static final Logger logger = Logger.getLogger(PinpointBootStrap.class.getName());
    private static final boolean STATE_NONE = false;
    private static final boolean STATE_STARTED = true;
    private static final AtomicBoolean LOAD_STATE = new AtomicBoolean(STATE_NONE);

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        if (agentArgs != null) {
            logger.info(ProductInfo.CAMEL_NAME + " agentArgs:" + agentArgs);
        }
        final boolean duplicated = checkDuplicateLoadState();
        if (duplicated) {
            logAgentLoadFail();
            return;
        }
        // 1st find boot-strap.jar
        final ClassPathResolver classPathResolver = new ClassPathResolver();
        boolean agentJarNotFound = classPathResolver.findAgentJar();
        if (!agentJarNotFound) {
            logger.severe("bootstrap-x.x.x(-SNAPSHOT).jar not found.");
            logAgentLoadFail();
            return;
        }
        // 2st find bootstrap-core.jar
        final String bootStrapCoreJar = classPathResolver.getBootStrapCoreJar();
        if (bootStrapCoreJar == null) {
            logger.severe("bootstrap-core-x.x.x(-SNAPSHOT).jar not found");
            logAgentLoadFail();
            return;
        }
        JarFile bootStrapCoreJarFile = getBootStrapJarFile(bootStrapCoreJar);
        if (bootStrapCoreJarFile == null) {
            logger.severe("bootstrap-core-x.x.x(-SNAPSHOT).jar not found");
            logAgentLoadFail();
            return;
        }
        logger.info("load bootstrap-core-x.x.x(-SNAPSHOT).jar :" + bootStrapCoreJar);
        instrumentation.appendToBootstrapClassLoaderSearch(bootStrapCoreJarFile);

        if (!isValidId("pinpoint.agentId", PinpointConstants.AGENT_NAME_MAX_LEN)) {
            logAgentLoadFail();
            return;
        }
        if (!isValidId("pinpoint.applicationName", PinpointConstants.APPLICATION_NAME_MAX_LEN)) {
            logAgentLoadFail();
            return;
        }

        String configPath = getConfigPath(classPathResolver);
        if (configPath == null) {
            logAgentLoadFail();
            return;
        }

        // set the path of log file as a system property
        saveLogFilePath(classPathResolver);

        try {
            // Is it right to load the configuration in the bootstrap?
            ProfilerConfig profilerConfig = ProfilerConfig.load(configPath);

            // this is the library list that must be loaded
            List<URL> libUrlList = resolveLib(classPathResolver);
            AgentClassLoader agentClassLoader = new AgentClassLoader(libUrlList.toArray(new URL[libUrlList.size()]));
            agentClassLoader.setBootClass(BOOT_CLASS);
            logger.info("agent starting...");
            agentClassLoader.boot(classPathResolver.getAgentDirPath(), agentArgs, instrumentation, profilerConfig);
            logger.info("agent started normally.");
        } catch (Exception e) {
            // unexpected exception that did not be checked above
            logger.log(Level.SEVERE, ProductInfo.CAMEL_NAME + " start failed. Error:" + e.getMessage(), e);
            logAgentLoadFail();
        }

    }

    private static JarFile getBootStrapJarFile(String bootStrapCoreJar) {
        try {
            return new JarFile(bootStrapCoreJar);
        } catch (IOException ioe) {
            logger.log(Level.SEVERE, bootStrapCoreJar + " file not found.", ioe);
            return null;
        }
    }

    private static void logAgentLoadFail() {
        final String errorLog = "*****************************************************************************\n" +
                                        "* Agent load failure\n" +
                                        "*****************************************************************************";
        System.err.println(errorLog);
    }

    // for test
    static boolean getLoadState() {
        return LOAD_STATE.get();
    }

    private static boolean checkDuplicateLoadState() {
        final boolean startSuccess = LOAD_STATE.compareAndSet(STATE_NONE, STATE_STARTED);
        if (startSuccess) {
            return false;
        } else {
            if (logger.isLoggable(Level.SEVERE)) {
                logger.severe("bootstrap already started. skipping agent loading.");
            }
            return true;
        }
    }

    private static boolean isValidId(String propertyName, int maxSize) {
        logger.info("check -D" + propertyName);
        String value = System.getProperty(propertyName);
        if (value == null) {
            logger.severe("-D" + propertyName + " is null. value:null");
            return false;
        }
        // blanks not permitted around value
        value = value.trim();
        if (value.isEmpty()) {
            logger.severe("-D" + propertyName + " is empty. value:''");
            return false;
        }

        if (!IdValidateUtils.validateId(value, maxSize)) {
            logger.severe("invalid Id. " + propertyName + " can only contain [a-zA-Z0-9], '.', '-', '_'. maxLength:"
                                  + maxSize + " value:" + value);
            return false;
        }
        if (logger.isLoggable(Level.INFO)) {
            logger.info("check success. -D" + propertyName + ":" + value + " length:" + getLength(value));
        }
        return true;
    }

    private static int getLength(String value) {
        final byte[] bytes = BytesUtils.toBytes(value);
        if (bytes == null) {
            return 0;
        } else {
            return bytes.length;
        }
    }

    private static void saveLogFilePath(ClassPathResolver classPathResolver) {
        String agentLogFilePath = classPathResolver.getAgentLogFilePath();
        logger.info("logPath:" + agentLogFilePath);

        System.setProperty(ProductInfo.NAME + ".log", agentLogFilePath);
    }

    private static String getConfigPath(ClassPathResolver classPathResolver) {
        final String configName = ProductInfo.NAME + ".config";
        String configFormSystemProperty = System.getProperty(configName);
        if (configFormSystemProperty != null) {
            logger.info(configName + " systemProperty found. " + configFormSystemProperty);
            return configFormSystemProperty;
        }

        String classPathAgentConfigPath = classPathResolver.getAgentConfigPath();
        if (classPathAgentConfigPath != null) {
            logger.info("classpath " + configName + " found. " + classPathAgentConfigPath);
            return classPathAgentConfigPath;
        }

        logger.severe(configName + " file not found.");
        return null;
    }

    private static List<URL> resolveLib(ClassPathResolver classPathResolver) {
        // this method may handle only absolute path,  need to handle relative path (./..agentlib/lib)
        String agentJarFullPath = classPathResolver.getAgentJarFullPath();
        String agentLibPath = classPathResolver.getAgentLibPath();
        List<URL> urlList = classPathResolver.resolveLib();
        String agentConfigPath = classPathResolver.getAgentConfigPath();

        if (logger.isLoggable(Level.INFO)) {
            logger.info("agentJarPath:" + agentJarFullPath);
            logger.info("agentLibPath:" + agentLibPath);
            logger.info("agent lib list:" + urlList);
            logger.info("agent config:" + agentConfigPath);
        }

        return urlList;
    }

}
