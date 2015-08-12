package com.baidu.oped.apm.bootstrap.interceptor.http;

/**
 * class HttpCallContext
 *
 * @author meidongxu@baidu.com
 */
public class HttpCallContext {
    private int statusCode;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

}
