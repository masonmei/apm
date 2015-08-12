
package com.baidu.oped.apm.profiler.interceptor.bci.mock;

import com.baidu.oped.apm.profiler.interceptor.aspect.Aspect;
import com.baidu.oped.apm.profiler.interceptor.aspect.JointPoint;
import com.baidu.oped.apm.profiler.interceptor.aspect.PointCut;

/**
 * class ErrorAspect 
 *
 * @author meidongxu@baidu.com
 */
@Aspect
public abstract class ErrorAspect {

    @PointCut
    public void testSignatureMiss() {
        __testVoid();
    }

    @JointPoint
    abstract void __testVoid();






}
