package com.baidu.oped.apm.config.interceptor;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.baidu.oped.apm.config.SystemConstant;

/**
 * class RequestIdInterceptor 
 *
 * @author meidongxu@baidu.com
 */
public class RequestIdInterceptor extends HandlerInterceptorAdapter {

    Logger log = LoggerFactory.getLogger(getClass());

    ThreadLocal<Long> beginTime = new ThreadLocal<>();
    ThreadLocal<String> threadLocalRequestId = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String requestId = request.getHeader(SystemConstant.X_BCE_REQUEST_ID);
        if (requestId == null) {
            requestId = UUID.randomUUID().toString();
            log.info("X_BCE_REQUEST_ID not found in header, generate requestId:{} ", requestId);
        }

        threadLocalRequestId.set(requestId);
        RequestInfoHolder.setThreadRequestId(requestId);
        response.addHeader(SystemConstant.X_BCE_REQUEST_ID, requestId);
        MDC.put("requestId", requestId);

        beginTime.set(System.currentTimeMillis());
        log.info("request preHandle,method:{},url:{}", request.getMethod(), request.getRequestURI());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) throws Exception {
        RequestInfoHolder.removeThreadRequestId();

        String requestId = threadLocalRequestId.get();
        String responseRequestId = response.getHeader(SystemConstant.X_BCE_REQUEST_ID);
        if (!requestId.equals(responseRequestId)) {
            log.error("requestId changed,requestId:{},responseRequestId:{}", requestId, responseRequestId);
        }
        response.setHeader(SystemConstant.X_BCE_REQUEST_ID, requestId);

        long timeUsed = System.currentTimeMillis() - beginTime.get();
        log.info("request afterCompletion,method:{},url:{},status:{},time:{}ms",
                request.getMethod(), request.getRequestURI(), response.getStatus(), timeUsed);
        MDC.remove("requestId");
    }
}
