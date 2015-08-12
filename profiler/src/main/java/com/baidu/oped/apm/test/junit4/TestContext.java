
package com.baidu.oped.apm.test.junit4;

import org.junit.runners.model.TestClass;

import com.baidu.oped.apm.test.TestClassLoader;

/**
 * class TestContext 
 *
 * @author meidongxu@baidu.com
 */
public class TestContext {

    private final TestClass testClass;
    private final Object baseTestClass;

    <T extends TestClassLoader> TestContext(final T testClassLoader, Class<?> clazz) throws ClassNotFoundException {
        this.testClass = new TestClass(testClassLoader.loadClass(clazz.getName()));
        this.baseTestClass = testClassLoader.loadClass(BasePinpointTest.class.getName());
    }

    public TestClass getTestClass() {
        return this.testClass;
    }

    public Object getBaseTestClass() {
        return this.baseTestClass;
    }
}
