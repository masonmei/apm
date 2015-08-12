
package com.baidu.oped.apm.profiler.context;

import com.baidu.oped.apm.common.ServiceType;

/**
 * class StackFrame 
 *
 * @author meidongxu@baidu.com
 */
public interface StackFrame {

    int getStackFrameId();

    void setStackFrameId(int stackId);

    void markBeforeTime();

    long getBeforeTime();

    void markAfterTime();

    long getAfterTime();

    int getElapsedTime();

    void setEndPoint(String endPoint);

    void setRpc(String rpc);

    void setApiId(int apiId);

    void setExceptionInfo(int exceptionId, String exceptionMessage);

    void setServiceType(short serviceType);

    void addAnnotation(Annotation annotation);
    
    ServiceType getServiceType();

    void attachFrameObject(Object frameObject);

    Object getFrameObject();

    Object detachFrameObject();
}
