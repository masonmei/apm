
package com.baidu.oped.apm.profiler.interceptor.bci.mock;

import com.baidu.oped.apm.common.util.BytesUtils;
import com.baidu.oped.apm.profiler.interceptor.aspect.Aspect;
import com.baidu.oped.apm.profiler.interceptor.aspect.JointPoint;
import com.baidu.oped.apm.profiler.interceptor.aspect.PointCut;

import java.util.Map;

/**
 * class TestAspect 
 *
 * @author meidongxu@baidu.com
 */
@Aspect
public abstract class TestAspect extends Original {

    @PointCut
    public void testVoid() {
        touchBefore();
        __testVoid();
        touchAfter();
    }

    @JointPoint
    abstract void __testVoid();


    @PointCut
    public int testInt() {
        touchBefore();
        final int result = __testInt();
        touchAfter();
        return result;
    }

    @JointPoint
    abstract int __testInt();


    @PointCut
    public String testString() {
        touchBefore();
        String s = __testString();
        touchAfter();
        return s;
    }

    @JointPoint
    abstract String __testString();

    @PointCut
    public int testUtilMethod() {
        touchBefore();
        int result = __testInt();
        utilMethod();
        touchAfter();
        return result;
    }

    private String utilMethod() {
        return "Util";
    }

    @PointCut
    public void testNoTouch() {
         __testVoid();
    }

    @PointCut
    public void testInternalMethod() {
        __testVoid();
    }

    @PointCut
    public void testMethodCall() {
        BytesUtils.toBytes("test");
        __testMethodCall();
    }

    @JointPoint
    abstract void __testMethodCall();

    @PointCut
    public Map<String, String> testGeneric() {
        return null;
    }

}
