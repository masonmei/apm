package com.baidu.oped.apm.bootstrap.instrument;

/**
 * class NotFoundInstrumentException
 *
 * @author meidongxu@baidu.com
 */
public class NotFoundInstrumentException extends InstrumentException {

    private static final long serialVersionUID = -9079014055408569735L;

    public NotFoundInstrumentException() {
    }

    public NotFoundInstrumentException(String message) {
        super(message);
    }

    public NotFoundInstrumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundInstrumentException(Throwable cause) {
        super(cause);
    }
}
