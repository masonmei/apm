package com.baidu.oped.apm.exception;

/**
 * class PinpointException
 *
 * @author meidongxu@baidu.com
 */
public class PinpointException extends RuntimeException {
    public PinpointException() {
    }

    public PinpointException(String message) {
        super(message);
    }

    public PinpointException(String message, Throwable cause) {
        super(message, cause);
    }

    public PinpointException(Throwable cause) {
        super(cause);
    }
}
