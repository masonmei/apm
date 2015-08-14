package com.baidu.oped.apm.config;

import com.baidu.oped.apm.config.exception.SystemCode;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * class BasicResponse
 *
 * @author meidongxu@baidu.com
 */

/**
 * Basic Response
 *
 * @author: cloudwatch@baidu.com
 */
public class BasicResponse<T> {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String requestId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SystemCode code = SystemCode.OK;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean success = true;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message = "";

    private T result;

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public SystemCode getCode() {
        return code;
    }

    public void setCode(SystemCode code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class Builder<T> {
        private String requestId;
        private SystemCode code;
        private Boolean success;
        private String message;
        private T result;

        public Builder requestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        public Builder code(SystemCode code) {
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder success(Boolean success) {
            this.success = success;
            return this;
        }

        public Builder result(T result) {
            this.result = result;
            return this;
        }

        public BasicResponse build() {
            BasicResponse basicResponse = new BasicResponse();
            if (requestId != null) {
                basicResponse.setRequestId(requestId);
            }
            if (code != null) {
                basicResponse.setCode(code);
            }

            if (message != null) {
                basicResponse.setMessage(message);
            }

            if (success != null) {
                basicResponse.setSuccess(success);
            }

            basicResponse.<T>setResult(result);
            return basicResponse;
        }
    }
}
