package com.baidu.oped.apm.statistics.exceptions;

/**
 * Created by mason on 9/22/15.
 */
public class NeedRunLaterException extends RuntimeException {
    public NeedRunLaterException(String msg) {
        super(msg);
    }
}
