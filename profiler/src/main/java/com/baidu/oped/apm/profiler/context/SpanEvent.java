
package com.baidu.oped.apm.profiler.context;

import com.baidu.oped.apm.thrift.dto.TIntStringValue;
import com.baidu.oped.apm.thrift.dto.TSpanEvent;

/**
 * Span represent RPC
 *
 * @author netspider
 * @author emeroad
 */
public class SpanEvent extends TSpanEvent {

    private final Span span;

    public SpanEvent(Span span) {
        if (span == null) {
            throw new NullPointerException("span must not be null");
        }
        this.span = span;
    }

    public Span getSpan() {
        return span;
    }

    public void addAnnotation(Annotation annotation) {
        this.addToAnnotations(annotation);
    }

    public void setExceptionInfo(int exceptionClassId, String exceptionMessage) {
        final TIntStringValue exceptionInfo = new TIntStringValue(exceptionClassId);
        if (exceptionMessage != null && !exceptionMessage.isEmpty()) {
            exceptionInfo.setStringValue(exceptionMessage);
        }
        super.setExceptionInfo(exceptionInfo);
    }


    public void markStartTime() {
//        spanEvent.setStartElapsed((int) (startTime - parentSpanStartTime));
        final int startElapsed = (int)(System.currentTimeMillis() - span.getStartTime());
        
        // If startElapsed is 0, logic without mark is useless. Don't do that.
        // The first SpaneEvent of a Sapn could result in 0. Not likely afterwards.
        this.setStartElapsed(startElapsed);
    }

    public long getStartTime() {
        return span.getStartTime() + getStartElapsed();
    }

    public void markAfterTime() {
        if (!isSetStartElapsed()) {
            throw new PinpointTraceException("startTime is not set");
        }
        final int endElapsed = (int)(System.currentTimeMillis() - getStartTime());
        if (endElapsed != 0) {
            this.setEndElapsed(endElapsed);
        }
    }

    public long getAfterTime() {
        if (!isSetStartElapsed()) {
            throw new PinpointTraceException("startTime is not set");
        }
        return span.getStartTime() + getStartElapsed() + getEndElapsed();
    }


}
