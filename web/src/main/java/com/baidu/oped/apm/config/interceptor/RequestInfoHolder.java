package com.baidu.oped.apm.config.interceptor;

/**
 * class RequestInfoHolder 
 *
 * @author meidongxu@baidu.com
 */



public class RequestInfoHolder {

    public static final ThreadLocal<String> THREAD_REQUEST_ID = new ThreadLocal<>();

    public static final ThreadLocal<String> THREAD_CURRENT_USER = new ThreadLocal<>();

    public static final ThreadLocal<Boolean> THREAD_IGNORE_IAM = new ThreadLocal<>();

    public static String getThreadRequestId() {
        return THREAD_REQUEST_ID.get();
    }

    public static void setThreadRequestId(String requestId) {
        THREAD_REQUEST_ID.set(requestId);
    }

    public static void removeThreadRequestId() {
        THREAD_REQUEST_ID.remove();
    }

    public static String getThreadCurrentUser() {
        return THREAD_CURRENT_USER.get();
    }

    public static void setThreadCurrentUser(String currentUser) {
        THREAD_CURRENT_USER.set(currentUser);
    }

    public static void removeThreadCurrentUser() {
        THREAD_CURRENT_USER.remove();
    }

    public static Boolean getThreadIgnoreIam() {
        return THREAD_IGNORE_IAM.get();
    }

    public static void setThreadIgnoreIam(Boolean isIgnoreIam) {
        THREAD_IGNORE_IAM.set(isIgnoreIam);
    }

    public static void removeThreadIgnoreIam() {
        THREAD_IGNORE_IAM.remove();
    }
}
