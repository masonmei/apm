
package com.baidu.oped.apm.collector.receiver;

import com.baidu.oped.apm.collector.handler.Handler;
import com.baidu.oped.apm.collector.handler.RequestResponseHandler;
import com.baidu.oped.apm.collector.handler.SimpleHandler;
import com.baidu.oped.apm.collector.util.AcceptedTimeService;

import org.apache.thrift.TBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * class AbstractDispatchHandler 
 *
 * @author meidongxu@baidu.com
 */
public abstract class AbstractDispatchHandler implements DispatchHandler {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AcceptedTimeService acceptedTimeService;

    public AbstractDispatchHandler() {
    }


    @Override
    public void dispatchSendMessage(TBase<?, ?> tBase, byte[] packet, int offset, int length) {

        // mark accepted time
        acceptedTimeService.accept();
        // TODO consider to change dispatch table automatically
        SimpleHandler simpleHandler = getSimpleHandler(tBase);
        if (simpleHandler != null) {
            if (logger.isTraceEnabled()) {
                logger.trace("simpleHandler name:{}", simpleHandler.getClass().getName());
            }
            simpleHandler.handleSimple(tBase);
            return;
        }

        Handler handler = getHandler(tBase);
        if (handler != null) {
            if (logger.isTraceEnabled()) {
                logger.trace("handler name:{}", handler.getClass().getName());
            }
            handler.handle(tBase, packet, offset, length);
            return;
        }

        throw new UnsupportedOperationException("Handler not found. Unknown type of data received. tBase=" + tBase);
    }
    
    public TBase dispatchRequestMessage(TBase<?,?> tBase, byte[] packet, int offset, int length) {
        // mark accepted time
        acceptedTimeService.accept();

        RequestResponseHandler requestResponseHandler = getRequestResponseHandler(tBase);
        if (requestResponseHandler != null) {
            if (logger.isTraceEnabled()) {
                logger.trace("requestResponseHandler name:{}", requestResponseHandler.getClass().getName());
            }
            return requestResponseHandler.handleRequest(tBase);
        }

        throw new UnsupportedOperationException("Handler not found. Unknown type of data received. tBase=" + tBase);
    }

    Handler getHandler(TBase<?, ?> tBase) {
        return null;
    }


    RequestResponseHandler getRequestResponseHandler(TBase<?, ?> tBase) {
        return null;
    }


    SimpleHandler getSimpleHandler(TBase<?, ?> tBase) {
        return null;
    }
}
