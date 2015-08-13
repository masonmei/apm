package com.baidu.oped.apm.config.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.baidu.oped.apm.config.BasicResponse;
import com.baidu.oped.apm.config.SystemConstant;
import com.baidu.oped.apm.utils.RequestUtils;

/**
 * class SystemExceptionHandler 
 *
 * @author meidongxu@baidu.com
 */


@ControllerAdvice
public class SystemExceptionHandler {

    Logger log = LoggerFactory.getLogger(getClass());


    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<BasicResponse> handleDataAccessException(HttpServletRequest request,
                                                            HttpServletResponse response,
                                                            DataAccessException exception) {
        log.warn("DataAccessException, error : {}", exception.getMessage());
        SystemCode code = SystemCode.INVALID_PARAMETER_VALUE;
        BasicResponse error = new BasicResponse();
        String requestId = RequestUtils.getRequestId(request, response);
        error.setRequestId(requestId);
        error.setCode(code);
        error.setMessage("Invalid Parameters.");
        log.info("[exception] {}", error.getCode());

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SystemException.class)
    public ResponseEntity<BasicResponse> handleSystemException(HttpServletRequest request,
            HttpServletResponse response, SystemException exception) {
        log.warn("SystemException handled", exception);
        SystemCode code = exception.getCode();
        BasicResponse error = new BasicResponse();
        String requestId = RequestUtils.getRequestId(request, response);
        error.setRequestId(requestId);
        error.setCode(code);
        error.setMessage(exception.getMessage());
        log.info("[exception] {}", error.getCode());
        if (code == SystemCode.INTERNAL_ERROR) {
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (code == SystemCode.INVALID_PARAMETER || code == SystemCode.INVALID_PARAMETER_VALUE
                || code == SystemCode.EXCEED_MAX_RETURN_DATA_POINTS
                || code == SystemCode.EXCEED_MAX_QUERY_DATA_POINTS) {
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        if (code == SystemCode.AUTHENTICATION_ERROR || code == SystemCode.AUTHORIZATION_ERROR) {
            return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<BasicResponse> handleMissingServletRequestParameterException(HttpServletRequest request,
            HttpServletResponse response, MissingServletRequestParameterException exception) {
        log.warn("MissingServletRequestParameterException handled", exception);
        BasicResponse error = new BasicResponse();
        String requestId = RequestUtils.getRequestId(request, response);
        error.setRequestId(requestId);
        error.setCode(SystemCode.INVALID_PARAMETER);
        error.setMessage(exception.getMessage());
        log.info("[exception] {}", error.getCode());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BasicResponse> handleException(HttpServletRequest request,
            HttpServletResponse response, Exception exception) {
        log.error("Exception handled", exception);
        BasicResponse error = new BasicResponse();
        String requestId = RequestUtils.getRequestId(request, response);
        error.setRequestId(requestId);
        error.setCode(SystemCode.INTERNAL_ERROR);
        error.setMessage(exception.getMessage());
        log.info("[exception] {}", error.getCode());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
