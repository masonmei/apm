
package com.baidu.oped.apm.profiler.interceptor.bci.mock;

import com.baidu.oped.apm.profiler.interceptor.aspect.Aspect;
import com.baidu.oped.apm.profiler.interceptor.aspect.JointPoint;
import com.baidu.oped.apm.profiler.interceptor.aspect.PointCut;

/**
 * class ErrorAspect2 
 *
 * @author meidongxu@baidu.com
 */
@Aspect
public abstract class ErrorAspect2 {

    @PointCut
    public int testInternalTypeMiss() {
        __testVoid();
        return 0;
    }

    @JointPoint
    abstract void __testVoid();






}
