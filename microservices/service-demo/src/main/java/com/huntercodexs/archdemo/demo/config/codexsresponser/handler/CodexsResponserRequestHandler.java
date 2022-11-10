package com.huntercodexs.archdemo.demo.config.codexsresponser.handler;

import com.huntercodexs.archdemo.demo.config.codexsresponser.dto.CodexsResponserDto;
import com.huntercodexs.archdemo.demo.config.codexsresponser.exception.CodexsResponserException;
import com.huntercodexs.archdemo.demo.config.codexsresponser.settings.CodexsResponserSettings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class CodexsResponserRequestHandler extends CodexsResponserSettings {

    @ExceptionHandler
    protected ResponseEntity<Object> handlerException(RuntimeException ex, WebRequest request) {

        CodexsResponserDto codexsResponserDto = new CodexsResponserDto();

        if (ex instanceof CodexsResponserException) {

            CodexsResponserException codexsResponserException = (CodexsResponserException) ex;
            codexsResponserDto.setErrorCode(codexsResponserException.getErrorCode());
            codexsResponserDto.setMessage(codexsResponserException.getMessage());

            return handleExceptionInternal(
                    ex,
                    codexsResponserDto,
                    new HttpHeaders(),
                    codexsResponserException.getHttpStatus(),
                    request
            );

        }

        codexsResponserDto.setErrorCode(unknownErrorCode);
        codexsResponserDto.setMessage(unknownErrorMessage);

        return handleExceptionInternal(
                ex,
                codexsResponserDto,
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {

        CodexsResponserDto codexsResponserDto = new CodexsResponserDto();
        codexsResponserDto.setErrorCode(405);
        codexsResponserDto.setMessage(ex.getMessage());

        return handleExceptionInternal(
                ex,
                codexsResponserDto,
                new HttpHeaders(),
                HttpStatus.METHOD_NOT_ALLOWED,
                request
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {

        CodexsResponserDto codexsResponserDto = new CodexsResponserDto();
        codexsResponserDto.setErrorCode(409);
        codexsResponserDto.setMessage(getCauseArgumentError(ex));

        return handleExceptionInternal(
                ex,
                codexsResponserDto,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                request
        );

    }

    private String getCauseArgumentError(MethodArgumentNotValidException ex) {
        return missingDataMessage.replace(replacementString, ex.getMessage()
                .split("; default message")[1].replaceAll("]|\\[| ", ""));
    }
}
