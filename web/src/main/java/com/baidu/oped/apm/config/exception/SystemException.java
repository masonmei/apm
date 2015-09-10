package com.baidu.oped.apm.config.exception;

/**
 * class SystemException
 *
 * @author meidongxu@baidu.com
 */

public class SystemException extends RuntimeException {

    private SystemCode code;

    public SystemException(SystemCode code, String message) {
        super(message);
        this.code = code;
    }

    public SystemException(SystemCode code) {
        super(code.toString());
        this.code = code;
    }

    public SystemCode getCode() {
        return code;
    }

    public void setCode(SystemCode code) {
        this.code = code;
    }
}
