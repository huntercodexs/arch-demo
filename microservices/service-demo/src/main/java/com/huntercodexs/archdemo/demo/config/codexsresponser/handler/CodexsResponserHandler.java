package com.huntercodexs.archdemo.demo.config.codexsresponser.handler;

import com.huntercodexs.archdemo.demo.config.codexsresponser.errors.CodexsResponserEditableErrors;
import com.huntercodexs.archdemo.demo.config.codexsresponser.exception.CodexsResponserException;
import com.huntercodexs.archdemo.demo.config.codexsresponser.dto.CodexsResponserDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CodexsResponserHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    protected ResponseEntity<Object> handler(RuntimeException ex, WebRequest request) {

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

        codexsResponserDto.setMessage("Unknown error");

        return handleExceptionInternal(
                ex,
                codexsResponserDto,
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR,
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
        codexsResponserDto.setErrorCode(CodexsResponserEditableErrors.SERVICE_ERROR_MISSING_DATA.getErrorCode());
        codexsResponserDto.setMessage(getCauseError(ex));

        System.out.println("METHOD ARGUMENT VALID RUNNING: " + codexsResponserDto);

        return handleExceptionInternal(
                ex,
                codexsResponserDto,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                request
        );

    }

    private String getCauseError(MethodArgumentNotValidException ex) {
        return CodexsResponserEditableErrors.SERVICE_ERROR_MISSING_DATA.getMessage().replace("@{data}", ex.getMessage()
                .split("; default message")[1].replaceAll("]|\\[| ", ""));
    }
}
