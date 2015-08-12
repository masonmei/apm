
package com.baidu.oped.apm.profiler.util;


import com.baidu.oped.apm.bootstrap.instrument.Scope;
import org.junit.Test;
import org.junit.Assert;

/**
 * class ThreadLocalScopeTest 
 *
 * @author meidongxu@baidu.com
 */
public class ThreadLocalScopeTest {
    @Test
    public void pushPop() {
        Scope scope = new ThreadLocalScope(new SimpleScopeFactory("test"));
        Assert.assertEquals(scope.push(), 0);
        Assert.assertEquals(scope.push(), 1);
        Assert.assertEquals(scope.push(), 2);

        Assert.assertEquals(scope.depth(), 3);

        Assert.assertEquals(scope.pop(), 2);
        Assert.assertEquals(scope.pop(), 1);
        Assert.assertEquals(scope.pop(), 0);
    }

    @Test
    public void pushPopError() {
        Scope scope = new ThreadLocalScope(new SimpleScopeFactory("test"));
        Assert.assertEquals(scope.pop(), -1);
        Assert.assertEquals(scope.pop(), -2);

        Assert.assertEquals(scope.push(), -2);
        Assert.assertEquals(scope.push(), -1);

        Assert.assertEquals(scope.depth(), 0);


    }

    @Test
    public void getName() {
        Scope scope = new ThreadLocalScope(new SimpleScopeFactory("test"));
        Assert.assertEquals(scope.getName(), "test");

    }
}

