
package com.baidu.oped.apm.profiler.modifier.arcus.interceptor;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import com.baidu.oped.apm.bootstrap.context.Trace;
import com.baidu.oped.apm.bootstrap.context.TraceContext;
import com.baidu.oped.apm.bootstrap.interceptor.*;
import com.baidu.oped.apm.bootstrap.logging.PLogger;
import com.baidu.oped.apm.bootstrap.logging.PLoggerFactory;
import com.baidu.oped.apm.bootstrap.util.MetaObject;
import com.baidu.oped.apm.common.ServiceType;

import net.spy.memcached.MemcachedNode;
import net.spy.memcached.ops.Operation;

/**
 * class FutureGetInterceptor 
 *
 * @author meidongxu@baidu.com
 */
public class FutureGetInterceptor implements SimpleAroundInterceptor, ByteCodeMethodDescriptorSupport, TraceContextSupport, TargetClassLoader {

    private final PLogger logger = PLoggerFactory.getLogger(this.getClass());
    private final boolean isDebug = logger.isDebugEnabled();

    private MetaObject<Object> getOperation = new MetaObject<Object>("__getOperation");
    private MetaObject<Object> getServiceCode = new MetaObject<Object>("__getServiceCode");
    
    private MethodDescriptor methodDescriptor;
    private TraceContext traceContext;

    @Override
    public void before(Object target, Object[] args) {
        if (isDebug) {
            logger.beforeInterceptor(target, args);
        }

        final Trace trace = traceContext.currentTraceObject();
        if (trace == null) {
            return;
        }

        trace.traceBlockBegin();
        trace.markBeforeTime();
    }

    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
        if (isDebug) {
            logger.afterInterceptor(target, args, result, throwable);
        }

        final Trace trace = traceContext.currentTraceObject();
        if (trace == null) {
            return;
        }

        try {
            trace.recordApi(methodDescriptor);
//            Do not record because it's not important.
//            String annotation = "future.get() timeout:" + args[0] + " " + ((TimeUnit)args[1]).name();
//            trace.recordAttribute(AnnotationKey.ARCUS_COMMAND, annotation);

            // find the target node
            final Operation op = (Operation) getOperation.invoke(target);
            if (op != null) {
                MemcachedNode handlingNode = op.getHandlingNode();
                if (handlingNode != null) {
                    SocketAddress socketAddress = handlingNode.getSocketAddress();
                    if (socketAddress instanceof InetSocketAddress) {
                        InetSocketAddress address = (InetSocketAddress) socketAddress;
                        trace.recordEndPoint(address.getHostName() + ":" + address.getPort());
                    }
                } else {
                    logger.info("no handling node");
                }
            } else {
                logger.info("operation not found");
            }

            // determine the service type
            String serviceCode = (String) getServiceCode.invoke((Operation)op);
            if (serviceCode != null) {
                trace.recordDestinationId(serviceCode);
                trace.recordServiceType(ServiceType.ARCUS_FUTURE_GET);
            } else {
                trace.recordDestinationId("MEMCACHED");
                trace.recordServiceType(ServiceType.MEMCACHED_FUTURE_GET);
            }

            if (op != null) {
                trace.recordException(op.getException());
            }
//            When it's canceled, doen't it throw exception?
//            if (op.isCancelled()) {
//                trace.recordAttribute(AnnotationKey.EXCEPTION, "cancelled by user");
//            }

            trace.markAfterTime();
        } finally {
            trace.traceBlockEnd();
        }
    }
    
    @Override
    public void setMethodDescriptor(MethodDescriptor descriptor) {
        this.methodDescriptor = descriptor;
        this.traceContext.cacheApi(descriptor);
    }

    @Override
    public void setTraceContext(TraceContext traceContext) {
        this.traceContext = traceContext;
    }

}
