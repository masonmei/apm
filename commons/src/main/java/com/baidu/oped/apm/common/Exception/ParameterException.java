package com.baidu.oped.apm.common.Exception;

/**
 * ***** description *****
 * Created with IntelliJ IDEA.
 * User: yangbolin
 * Date: 15/8/13
 * Time: 16:13
 * To change this template use File | Settings | File Templates.
 */
public class ParameterException extends Exception {

    private static final String PREFIX = "Parameter Exception : ";

    public ParameterException(Exception e) {
        super(PREFIX + e.getLocalizedMessage(), e);
    }

    public ParameterException(String msg) {
        super(PREFIX + msg);
    }

}
