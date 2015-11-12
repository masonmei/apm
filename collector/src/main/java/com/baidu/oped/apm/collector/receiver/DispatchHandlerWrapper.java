package com.baidu.oped.apm.collector.receiver;

import com.baidu.oped.apm.collector.manage.HandlerManager;
import com.baidu.oped.apm.thrift.dto.TResult;
import org.apache.thrift.TBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Taejin Koo
 */
public class DispatchHandlerWrapper implements DispatchHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private final DispatchHandler delegate;

    @Autowired
    private HandlerManager handlerManager;

    public DispatchHandlerWrapper(DispatchHandler dispatchHandler) {
        if (dispatchHandler == null) {
            throw new NullPointerException("dispatchHandler may note be null.");
        }
        this.delegate = dispatchHandler;
    }

    @Override
    public void dispatchSendMessage(TBase<?, ?> tBase) {
        if (checkAvaiable()) {
            this.delegate.dispatchSendMessage(tBase);
            return;
        }

        logger.debug("Handler is disabled. Skipping send message {}.", tBase);
        return;
    }

    @Override
    public TBase dispatchRequestMessage(TBase<?, ?> tBase) {
        if (checkAvaiable()) {
            return this.delegate.dispatchRequestMessage(tBase);
        }

        logger.debug("Handler is disabled. Skipping request message {}.", tBase);
        
        TResult result = new TResult(false);
        result.setMessage("Handler is disabled. Skipping request message.");
        return result;
    }
    
    private boolean checkAvaiable() {
        if (handlerManager == null) {
            return true;
        }
        
        if (handlerManager.isEnable()) {
            return true;
        }
        
        return false;
    }

}
