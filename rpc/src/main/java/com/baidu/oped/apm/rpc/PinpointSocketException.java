
package com.baidu.oped.apm.rpc;

/**
 * class PinpointSocketException 
 *
 * @author meidongxu@baidu.com
 */
public class PinpointSocketException extends RuntimeException {
    public PinpointSocketException() {
    }

    public PinpointSocketException(String message) {
        super(message);
    }

    public PinpointSocketException(String message, Throwable cause) {
        super(message, cause);
    }

    public PinpointSocketException(Throwable cause) {
        super(cause);
    }
}
