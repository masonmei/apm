package com.baidu.oped.apm.mvc.vo.callstack;

import com.baidu.oped.apm.common.jpa.entity.Trace;
import com.baidu.oped.apm.common.jpa.entity.TraceEvent;
import com.google.common.base.MoreObjects;
import org.springframework.util.Assert;

/**
 * Created by mason on 10/13/15.
 */
public class CallTreeAlign {
    private Trace trace;
    private TraceEvent traceEvent;
    private boolean isTrace = true;
    private boolean hasChild = false;

    private int id;
    private long gap;
    private int depth;
    private long executionInMillis;

    public CallTreeAlign(Trace trace) {
        Assert.notNull(trace, "Trace must not be null");
        this.trace = trace;
        this.isTrace = true;
    }

    public CallTreeAlign(Trace trace, TraceEvent traceEvent) {
        Assert.notNull(trace, "Trace must not be null");
        Assert.notNull(traceEvent, "TraceEvent must not be null");
        this.trace = trace;
        this.traceEvent = traceEvent;
        this.isTrace = false;
    }

    public Trace getTrace() {
        return trace;
    }

    public void setTrace(Trace trace) {
        this.trace = trace;
    }

    public TraceEvent getTraceEvent() {
        return traceEvent;
    }

    public void setTraceEvent(TraceEvent traceEvent) {
        this.traceEvent = traceEvent;
    }

    public boolean isTrace() {
        return isTrace;
    }

    public void setTrace(boolean trace) {
        isTrace = trace;
    }

    public boolean isHasChild() {
        return hasChild;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getGap() {
        return gap;
    }

    public void setGap(long gap) {
        this.gap = gap;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public long getExecutionInMillis() {
        return executionInMillis;
    }

    public void setExecutionInMillis(long executionInMillis) {
        this.executionInMillis = executionInMillis;
    }

    public long getStartTime() {
        if (isTrace()) {
            return trace.getStartTime();
        } else {
            return trace.getStartTime() + traceEvent.getStartElapsed();
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("trace", trace)
                .add("traceEvent", traceEvent)
                .add("isTrace", isTrace)
                .add("hasChild", hasChild)
                .add("id", id)
                .add("gap", gap)
                .add("depth", depth)
                .add("executionInMillis", executionInMillis)
                .toString();
    }
}
