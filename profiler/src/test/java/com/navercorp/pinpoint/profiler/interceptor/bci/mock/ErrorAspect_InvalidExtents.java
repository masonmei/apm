
package com.baidu.oped.apm.profiler.interceptor.bci.mock;

import com.baidu.oped.apm.profiler.interceptor.aspect.Aspect;
import com.baidu.oped.apm.profiler.interceptor.aspect.JointPoint;
import com.baidu.oped.apm.profiler.interceptor.aspect.PointCut;

/**
 * class ErrorAspect_InvalidExtents 
 *
 * @author meidongxu@baidu.com
 */
@Aspect
public abstract class ErrorAspect_InvalidExtents extends Thread {

    @PointCut
    public void testVoid() {
        __testVoid();
    }

    @JointPoint
    abstract void __testVoid();



}
