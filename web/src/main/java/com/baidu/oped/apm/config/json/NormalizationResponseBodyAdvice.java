package com.baidu.oped.apm.config.json;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.baidu.oped.apm.config.BasicResponse;
import com.baidu.oped.apm.utils.RequestUtils;

/**
 * Created by mason on 8/13/15.
 */
@ControllerAdvice(basePackages = {"com.baidu.oped.apm.mvc.controller"})
public class NormalizationResponseBodyAdvice implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        String requestId = RequestUtils.getRequestId(((ServletServerHttpRequest) request).getServletRequest(),
                                                     ((ServletServerHttpResponse) response).getServletResponse());
        return BasicResponse.builder().requestId(requestId).result(body).build();
    }
}
