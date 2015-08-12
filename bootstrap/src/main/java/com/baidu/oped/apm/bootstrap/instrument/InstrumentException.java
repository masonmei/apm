package com.baidu.oped.apm.bootstrap.instrument;

/**
 * class InstrumentException
 *
 * @author meidongxu@baidu.com
 */
// TODO Separate this class when hierarchical layers are needed

public class InstrumentException extends Exception {

    private static final long serialVersionUID = 7594176009977030312L;

    public InstrumentException() {
    }

    public InstrumentException(String message) {
        super(message);
    }

    public InstrumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public InstrumentException(Throwable cause) {
        super(cause);
    }
}
