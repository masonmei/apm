package com.baidu.oped.apm.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baidu.oped.apm.config.SystemConstant;

/**
 * Created by mason on 8/13/15.
 */
public abstract class RequestUtils {

    public static String getRequestId(HttpServletRequest request, HttpServletResponse response) {
        if (request.getHeader(SystemConstant.X_BCE_REQUEST_ID) != null) {
            return request.getHeader(SystemConstant.X_BCE_REQUEST_ID);
        } else if (response.containsHeader(SystemConstant.X_BCE_REQUEST_ID)) {
            return response.getHeader(SystemConstant.X_BCE_REQUEST_ID);
        }
        return null;
    }

}
