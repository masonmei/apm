
package com.baidu.oped.apm.test;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.BeforeClass;

import com.baidu.oped.apm.bootstrap.context.TraceContext;
import com.baidu.oped.apm.bootstrap.interceptor.ByteCodeMethodDescriptorSupport;
import com.baidu.oped.apm.bootstrap.interceptor.Interceptor;
import com.baidu.oped.apm.bootstrap.interceptor.MethodDescriptor;
import com.baidu.oped.apm.bootstrap.interceptor.TraceContextSupport;
import com.baidu.oped.apm.bootstrap.logging.PLoggerFactory;
import com.baidu.oped.apm.profiler.logging.Slf4jLoggerBinder;

/**
 * class BaseInterceptorTest 
 *
 * @author meidongxu@baidu.com
 */
public class BaseInterceptorTest {

    Interceptor interceptor;
    MethodDescriptor descriptor;

    public void setInterceptor(Interceptor interceptor) {
        this.interceptor = interceptor;
    }

    public void setMethodDescriptor(MethodDescriptor methodDescriptor) {
        this.descriptor = methodDescriptor;
    }

    @BeforeClass
    public static void before() {
        PLoggerFactory.initialize(new Slf4jLoggerBinder());
    }

    @Before
    public void beforeEach() {
        if (interceptor == null) {
            Assert.fail("set the interceptor first.");
        }

        if (interceptor instanceof TraceContextSupport) {
            // sampler

            // trace context
            TraceContext traceContext = new MockTraceContextFactory().create();
            ((TraceContextSupport) interceptor).setTraceContext(traceContext);
        }

        if (interceptor instanceof ByteCodeMethodDescriptorSupport) {
            if (descriptor != null) {
                ((ByteCodeMethodDescriptorSupport)interceptor).setMethodDescriptor(descriptor);
            }
        }
    }

}
