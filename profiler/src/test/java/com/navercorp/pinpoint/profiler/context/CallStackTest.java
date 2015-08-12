
package com.baidu.oped.apm.profiler.context;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.oped.apm.profiler.context.CallStack;
import com.baidu.oped.apm.profiler.context.DefaultTraceId;
import com.baidu.oped.apm.profiler.context.Span;
import com.baidu.oped.apm.profiler.context.SpanEvent;
import com.baidu.oped.apm.profiler.context.SpanEventStackFrame;
import com.baidu.oped.apm.profiler.context.StackFrame;

/**
 * class CallStackTest 
 *
 * @author meidongxu@baidu.com
 */
public class CallStackTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Span createSpan() {
        DefaultTraceId traceId = new DefaultTraceId("test", 0, 1);
        Span span = new Span();
        span.setAgentId("agentId");
        span.recordTraceId(traceId);
        return span;
    }

    private SpanEventStackFrame createSpanEventStackFrame(Span span) {
        SpanEvent spanEvent = new SpanEvent(span);
        SpanEventStackFrame spanEventStackFrame = new SpanEventStackFrame(spanEvent);
        return spanEventStackFrame;
    }


    @Test
    public void testPush() throws Exception {
        final Span span = createSpan();

        CallStack callStack = new CallStack(span);
        int initialIndex = callStack.getStackFrameIndex();
        Assert.assertEquals("initial index", initialIndex, -1);

        callStack.push();

        SpanEventStackFrame spanEventStackFrame = createSpanEventStackFrame(span);
        callStack.setStackFrame(spanEventStackFrame);

        callStack.popRoot();
    }

    @Test
    public void testLargePush() {
        final Span span = createSpan();

        CallStack callStack = new CallStack(span);
        int initialIndex = callStack.getStackFrameIndex();
        Assert.assertEquals("initial index", initialIndex, -1);

        final int pushCount = 32;
        for(int i = 0; i< pushCount; i++) {
            int push = callStack.push();
            Assert.assertEquals("push index", i, push);

            SpanEventStackFrame stackFrame = createSpanEventStackFrame(span);
            callStack.setStackFrame(stackFrame);

            int index = callStack.getIndex();
            Assert.assertEquals("index", i, index);
        }
        for(int i = 0; i< pushCount-1; i++) {
            callStack.pop();
        }
        callStack.popRoot();
    }



    @Test
    public void testPushPop1() {
        CallStack callStack = new CallStack(new Span());

        callStack.push();
        callStack.popRoot();

    }

    @Test
    public void testPushPop2() {
        CallStack callStack = new CallStack(new Span());

        callStack.push();
        callStack.push();

        callStack.pop();
        callStack.popRoot();

    }

    @Test
    public void testRootPop_fail() {
        CallStack callStack = new CallStack(new Span());

        callStack.push();
        callStack.push();

        try {
            callStack.popRoot();
            Assert.fail("invalid popRoot");
        } catch (Exception e) {
        }

    }

    @Test
    public void testPop_Fail() {
        CallStack callStack = new CallStack(new Span());

        callStack.push();
        callStack.push();

        callStack.pop();

        StackFrame lastPop = callStack.pop();
        Assert.assertNull(lastPop);

        try {
            callStack.pop();
            Assert.fail("invalid pop");
        } catch (Exception e) {
        }
    }

    @Test
    public void testCopyStack() {
        Span span = new Span();
        CallStack callStack = new CallStack(span);

        callStack.push();
        callStack.setStackFrame(createSpanEventStackFrame(span));
        callStack.push();
        callStack.setStackFrame(createSpanEventStackFrame(span));

        StackFrame[] stackFrames = callStack.copyStackFrame();
        int lastIndex = 0;
        for (; lastIndex < stackFrames.length; lastIndex++) {
            if (stackFrames[lastIndex] == null) {
                break;
            }
        }
        Assert.assertEquals(lastIndex, 2);
    }

}