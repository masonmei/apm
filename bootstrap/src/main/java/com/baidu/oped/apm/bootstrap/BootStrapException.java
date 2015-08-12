package com.baidu.oped.apm.bootstrap;

import com.baidu.oped.apm.exception.PinpointException;

/**
 * class BootStrapException
 *
 * @author meidongxu@baidu.com
 */
public class BootStrapException extends PinpointException {

    public BootStrapException(String message, Throwable cause) {
        super(message, cause);
    }

    public BootStrapException(String message) {
        super(message);
    }
}
