package com.baidu.oped.apm.bootstrap;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.Callable;

import com.baidu.oped.apm.bootstrap.config.ProfilerConfig;

/**
 * class AgentClassLoader
 *
 * @author meidongxu@baidu.com
 */
public class AgentClassLoader {

    private static final SecurityManager SECURITY_MANAGER = System.getSecurityManager();

    private final URLClassLoader classLoader;
    private final ContextClassLoaderExecuteTemplate<Object> executeTemplate;
    private String bootClass;
    private Agent agentBootStrap;

    public AgentClassLoader(URL[] urls) {
        if (urls == null) {
            throw new NullPointerException("urls");
        }

        ClassLoader bootStrapClassLoader = AgentClassLoader.class.getClassLoader();
        this.classLoader = createClassLoader(urls, bootStrapClassLoader);

        this.executeTemplate = new ContextClassLoaderExecuteTemplate<Object>(classLoader);
    }

    private PinpointURLClassLoader createClassLoader(final URL[] urls, final ClassLoader bootStrapClassLoader) {
        if (SECURITY_MANAGER != null) {
            return AccessController.doPrivileged(new PrivilegedAction<PinpointURLClassLoader>() {
                public PinpointURLClassLoader run() {
                    return new PinpointURLClassLoader(urls, bootStrapClassLoader);
                }
            });
        } else {
            return new PinpointURLClassLoader(urls, bootStrapClassLoader);
        }
    }

    public void setBootClass(String bootClass) {
        this.bootClass = bootClass;
    }

    public void boot(final String agentPath, final String agentArgs, final Instrumentation instrumentation,
                     final ProfilerConfig profilerConfig) {

        final Class<?> bootStrapClazz = getBootStrapClass();

        final Object agent = executeTemplate.execute(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                try {
                    Constructor<?> constructor = bootStrapClazz.getConstructor(String.class, String.class,
                                                                                      Instrumentation.class,
                                                                                      ProfilerConfig.class);
                    return constructor.newInstance(agentPath, agentArgs, instrumentation, profilerConfig);
                } catch (InstantiationException e) {
                    throw new BootStrapException("boot create failed. Error:" + e.getMessage(), e);
                } catch (IllegalAccessException e) {
                    throw new BootStrapException("boot method invoke failed. Error:" + e.getMessage(), e);
                }
            }
        });

        if (agent instanceof Agent) {
            this.agentBootStrap = (Agent) agent;
        } else {
            String agentClassName;
            if (agent == null) {
                agentClassName = "Agent is null";
            } else {
                agentClassName = agent.getClass().getName();
            }
            throw new BootStrapException("Invalid AgentType. boot failed. AgentClass:" + agentClassName);
        }
    }

    private Class<?> getBootStrapClass() {
        try {
            return this.classLoader.loadClass(bootClass);
        } catch (ClassNotFoundException e) {
            throw new BootStrapException("boot class not found. bootClass:" + bootClass + " Error:" + e.getMessage(),
                                                e);
        }
    }

    @Deprecated
    public Object initializeLoggerBinder() {
        if (agentBootStrap != null) {
            return reflectionInvoke(this.agentBootStrap, "initializeLogger", null, null);
        }
        return null;
    }

    private Object reflectionInvoke(Object target, String method, Class[] type, final Object[] args) {
        final Method findMethod = findMethod(target.getClass(), method, type);
        return executeTemplate.execute(new Callable<Object>() {
            @Override
            public Object call() {
                try {
                    return findMethod.invoke(agentBootStrap, args);
                } catch (InvocationTargetException e) {
                    throw new BootStrapException(findMethod.getName() + "() failed. Error:" + e.getMessage(), e);
                } catch (IllegalAccessException e) {
                    throw new BootStrapException("boot method invoke failed. Error:" + e.getMessage(), e);
                }
            }
        });

    }

    private Method findMethod(Class<?> clazz, String method, Class[] type) {
        try {
            return clazz.getDeclaredMethod(method, type);
        } catch (NoSuchMethodException e) {
            throw new BootStrapException("(" + method + ") boot method not found. Error:" + e.getMessage(), e);
        }
    }

}
