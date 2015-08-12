
package com.baidu.oped.apm.profiler.context;

import com.baidu.oped.apm.exception.PinpointException;

/**
 * class PinpointTraceException 
 *
 * @author meidongxu@baidu.com
 */
public class PinpointTraceException extends PinpointException {

    public PinpointTraceException() {
    }

    public PinpointTraceException(String message) {
        super(message);
    }

    public PinpointTraceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PinpointTraceException(Throwable cause) {
        super(cause);
    }
}
