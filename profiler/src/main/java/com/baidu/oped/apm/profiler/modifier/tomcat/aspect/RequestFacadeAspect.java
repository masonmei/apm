
package com.baidu.oped.apm.profiler.modifier.tomcat.aspect;

import com.baidu.oped.apm.bootstrap.context.Header;
import com.baidu.oped.apm.profiler.interceptor.aspect.Aspect;
import com.baidu.oped.apm.profiler.interceptor.aspect.JointPoint;
import com.baidu.oped.apm.profiler.interceptor.aspect.PointCut;

import java.util.Enumeration;

/**
 * filtering pinpoint header
 * @author emeroad
 */
@Aspect
public abstract class RequestFacadeAspect {

    @PointCut
    public String getHeader(String name) {
        if (Header.hasHeader(name)) {
            return null;
        }
        return __getHeader(name);
    }

    @JointPoint
    abstract String __getHeader(String name);


    @PointCut
    public Enumeration getHeaders(String name) {
        final Enumeration headers = Header.getHeaders(name);
        if (headers != null) {
            return headers;
        }
        return __getHeaders(name);
    }

    @JointPoint
    abstract Enumeration __getHeaders(String name);


    @PointCut
    public Enumeration getHeaderNames() {
        final Enumeration enumeration = __getHeaderNames();
        return Header.filteredHeaderNames(enumeration);
    }

    @JointPoint
    abstract Enumeration __getHeaderNames();

}
