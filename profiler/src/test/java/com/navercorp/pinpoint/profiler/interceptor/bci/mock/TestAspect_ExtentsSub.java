
package com.baidu.oped.apm.profiler.interceptor.bci.mock;

import com.baidu.oped.apm.profiler.interceptor.aspect.Aspect;
import com.baidu.oped.apm.profiler.interceptor.aspect.JointPoint;
import com.baidu.oped.apm.profiler.interceptor.aspect.PointCut;

/**
 * class TestAspect_ExtentsSub 
 *
 * @author meidongxu@baidu.com
 */
@Aspect
public abstract class TestAspect_ExtentsSub extends OriginalSub {

    @PointCut
    public void testVoid() {
        touchBefore();
        __testVoid();
        touchAfter();
    }

    @JointPoint
    abstract void __testVoid();



}
