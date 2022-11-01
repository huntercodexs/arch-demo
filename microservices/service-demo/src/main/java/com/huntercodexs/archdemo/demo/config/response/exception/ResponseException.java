package com.huntercodexs.archdemo.demo.config.response.exception;

import com.huntercodexs.archdemo.demo.config.response.errors.ResponseErrors;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseException extends RuntimeException {
    public HttpStatus httpStatus;
    public int errorCode;
    public String message;

    public ResponseException(ResponseErrors responseErrors) {
        this.httpStatus = responseErrors.getStatusCode();
        this.errorCode = responseErrors.getErrorCode();
        this.message = responseErrors.getMessage();
    }
}
