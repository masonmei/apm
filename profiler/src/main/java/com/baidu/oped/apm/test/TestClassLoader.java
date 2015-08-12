
package com.baidu.oped.apm.test;

import com.baidu.oped.apm.bootstrap.Agent;
import com.baidu.oped.apm.bootstrap.instrument.ByteCodeInstrumentor;
import com.baidu.oped.apm.profiler.DefaultAgent;
import com.baidu.oped.apm.profiler.interceptor.bci.JavaAssistByteCodeInstrumentor;
import com.baidu.oped.apm.profiler.modifier.AbstractModifier;

import javassist.CannotCompileException;
import javassist.Loader;
import javassist.NotFoundException;

import org.junit.runners.model.InitializationError;

/**
 * class TestClassLoader 
 *
 * @author meidongxu@baidu.com
 */
public class TestClassLoader extends Loader {
    private Agent agent;
    private ByteCodeInstrumentor instrumentor;
    private InstrumentTranslator instrumentTranslator;

    public TestClassLoader(DefaultAgent agent) {
        if (agent == null) {
            throw new NullPointerException("agent must not be null");
        }
        this.agent = agent;
        this.instrumentor = agent.getByteCodeInstrumentor();
        this.instrumentTranslator = new InstrumentTranslator(this, agent);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }

    public void initialize() throws InitializationError {
        addDefaultDelegateLoadingOf();
        addTranslator();
    }

    public Agent getAgent() {
        if (this.agent == null) {
            throw new IllegalStateException("TestClassLoader is not initialized.");
        }
        return agent;
    }

    public ByteCodeInstrumentor getInstrumentor() {
        if (this.instrumentor == null) {
            throw new IllegalStateException("TestClassLoader is not initialized.");
        }
        return instrumentor;
    }

    public AbstractModifier addModifier(AbstractModifier modifier) {
        if (this.instrumentTranslator == null) {
            throw new IllegalStateException("TestClassLoader is not initialized.");
        }
        return this.instrumentTranslator.addModifier(modifier);
    }

    private void addDefaultDelegateLoadingOf() {
        this.delegateLoadingOf("com.baidu.oped.apm.bootstrap.");
        this.delegateLoadingOf("com.baidu.oped.apm.common.");
        this.delegateLoadingOf("com.baidu.oped.apm.thrift.");
        this.delegateLoadingOf("com.baidu.oped.apm.profiler.context.");
        this.delegateLoadingOf("com.baidu.oped.apm.test.PeekableDataSender");
        this.delegateLoadingOf("com.baidu.oped.apm.test.junit4.IsRootSpan");
        this.delegateLoadingOf("org.apache.thrift.TBase");
        this.delegateLoadingOf("junit.");
        this.delegateLoadingOf("org.hamcrest.");
        this.delegateLoadingOf("org.junit.");
    }

    @Override
    protected Class<?> loadClassByDelegation(String name) throws ClassNotFoundException {
        return super.loadClassByDelegation(name);
    }

    private void addTranslator() {
        try {
            addTranslator(((JavaAssistByteCodeInstrumentor)instrumentor).getClassPool(this), instrumentTranslator);
        } catch (NotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (CannotCompileException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void runTest(String className, String methodName) throws Throwable {
        Class<?> c = loadClass(className);
        Object o = c.newInstance();
        try {
            c.getDeclaredMethod(methodName).invoke(o);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw e.getTargetException();
        }
    }
}
