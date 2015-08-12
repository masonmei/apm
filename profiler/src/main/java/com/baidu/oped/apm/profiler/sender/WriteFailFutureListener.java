
package com.baidu.oped.apm.profiler.sender;

import com.baidu.oped.apm.rpc.Future;
import com.baidu.oped.apm.rpc.FutureListener;

import org.slf4j.Logger;

/**
 * class WriteFailFutureListener 
 *
 * @author meidongxu@baidu.com
 */
public class WriteFailFutureListener implements FutureListener {

    private final Logger logger;
    private final String message;
    private final String host;
    private final String port;
    private final boolean isWarn;


    public WriteFailFutureListener(Logger logger, String message, String host, int port) {
        if (logger == null) {
            throw new NullPointerException("logger must not be null");
        }
        this.logger = logger;
        this.message = message;
        this.host = host;
        this.port = String.valueOf(port);
        this.isWarn = logger.isWarnEnabled();
    }

    @Override
    public void onComplete(Future future) {
        if (!future.isSuccess()) {
            if (isWarn) {
                logger.warn("{} {}/{}", message, host, port);
            }
        }
    }
}