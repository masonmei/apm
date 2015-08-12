
package com.baidu.oped.apm.profiler.interceptor.bci.mock;

import com.baidu.oped.apm.profiler.interceptor.aspect.Aspect;
import com.baidu.oped.apm.profiler.interceptor.aspect.JointPoint;
import com.baidu.oped.apm.profiler.interceptor.aspect.PointCut;

/**
 * class TestAspect_NoExtents 
 *
 * @author meidongxu@baidu.com
 */
@Aspect
public abstract class TestAspect_NoExtents {

    @PointCut
    public void testVoid() {
        __testVoid();
    }

    @JointPoint
    abstract void __testVoid();



}
