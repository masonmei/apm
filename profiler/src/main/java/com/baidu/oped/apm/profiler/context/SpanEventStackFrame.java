
package com.baidu.oped.apm.profiler.context;

import com.baidu.oped.apm.common.ServiceType;

/**
 * class SpanEventStackFrame 
 *
 * @author meidongxu@baidu.com
 */
public class SpanEventStackFrame implements StackFrame {
    private final SpanEvent spanEvent;
    private int stackId;
    private Object frameObject;

    public SpanEventStackFrame(SpanEvent spanEvent) {
        if (spanEvent == null) {
            throw new NullPointerException("spanEvent must not be null");
        }
        this.spanEvent = spanEvent;
    }

    @Override
    public int getStackFrameId() {
        return stackId;
    }

    @Override
    public void setStackFrameId(int stackId) {
        this.stackId = stackId;
    }

    @Override
    public void markBeforeTime() {
        spanEvent.markStartTime();
    }

    @Override
    public long getBeforeTime() {
        return spanEvent.getStartTime();
    }

    @Override
    public void markAfterTime() {
        spanEvent.markAfterTime();
    }

    @Override
    public long getAfterTime() {
        return spanEvent.getAfterTime();
    }

    @Override
    public int getElapsedTime() {
        return spanEvent.getEndElapsed();
    }

    public void setSequence(short sequence) {
        spanEvent.setSequence(sequence);
    }

    public SpanEvent getSpanEvent() {
        return spanEvent;
    }

    @Override
    public void setEndPoint(String endPoint) {
        this.spanEvent.setEndPoint(endPoint);
    }

    @Override
    public void setRpc(String rpc) {
        this.spanEvent.setRpc(rpc);
    }

    @Override
    public void setApiId(int apiId) {
        this.spanEvent.setApiId(apiId);
    }

    @Override
    public void setExceptionInfo(int exceptionId, String exceptionMessage) {
        this.spanEvent.setExceptionInfo(exceptionId, exceptionMessage);
    }

    @Override
    public void setServiceType(short serviceType) {
        spanEvent.setServiceType(serviceType);
    }

    @Override
    public void addAnnotation(Annotation annotation) {
        this.spanEvent.addAnnotation(annotation);
    }

    public void setDestinationId(String destinationId) {
        this.spanEvent.setDestinationId(destinationId);
    }

    public void setNextSpanId(long nextSpanId) {
        this.spanEvent.setNextSpanId(nextSpanId);
    }

    @Override
    public void attachFrameObject(Object frameObject) {
        this.frameObject = frameObject;
    }

    @Override
    public Object getFrameObject() {
        return this.frameObject;
    }

    @Override
    public Object detachFrameObject() {
        Object copy = this.frameObject;
        this.frameObject = null;
        return copy;
    }
    
    @Override
    public ServiceType getServiceType() {
        return ServiceType.findServiceType(spanEvent.getServiceType());
    }


}
