package com.huntercodexs.archdemo.demo.config.codexsresponser.exception;

import com.huntercodexs.archdemo.demo.config.codexsresponser.errors.CodexsResponserEditableErrors;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CodexsResponserException extends RuntimeException {
    public HttpStatus httpStatus;
    public int errorCode;
    public String message;

    public CodexsResponserException(CodexsResponserEditableErrors codexsResponserEditableErrors) {
        this.httpStatus = codexsResponserEditableErrors.getStatusCode();
        this.errorCode = codexsResponserEditableErrors.getErrorCode();
        this.message = codexsResponserEditableErrors.getMessage();
    }
}
