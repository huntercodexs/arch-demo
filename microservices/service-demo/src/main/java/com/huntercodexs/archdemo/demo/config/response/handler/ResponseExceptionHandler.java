package com.huntercodexs.archdemo.demo.config.response.handler;

import com.huntercodexs.archdemo.demo.config.response.dto.ResponseExceptionHandlerDto;
import com.huntercodexs.archdemo.demo.config.response.errors.ResponseErrors;
import com.huntercodexs.archdemo.demo.config.response.exception.ResponseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    protected ResponseEntity<Object> handler(RuntimeException ex, WebRequest request) {

        ResponseExceptionHandlerDto responseExceptionHandlerDto = new ResponseExceptionHandlerDto();

        if (ex instanceof ResponseException) {

            ResponseException responseException = (ResponseException) ex;
            responseExceptionHandlerDto.setErrorCode(responseException.getErrorCode());
            responseExceptionHandlerDto.setMessage(responseException.getMessage());

            return handleExceptionInternal(
                    ex,
                    responseExceptionHandlerDto,
                    new HttpHeaders(),
                    responseException.getHttpStatus(),
                    request
            );

        }

        responseExceptionHandlerDto.setMessage("Unknown error");

        return handleExceptionInternal(
                ex,
                responseExceptionHandlerDto,
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
        ResponseExceptionHandlerDto responseExceptionHandlerDto = new ResponseExceptionHandlerDto();
        responseExceptionHandlerDto.setErrorCode(ResponseErrors.SERVICE_ERROR_MISSING_DATA.getErrorCode());
        responseExceptionHandlerDto.setMessage(getCauseError(ex));

        return handleExceptionInternal(
                ex,
                responseExceptionHandlerDto,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                request
        );

    }

    private String getCauseError(MethodArgumentNotValidException ex) {
        return ResponseErrors.SERVICE_ERROR_MISSING_DATA.getMessage().replace("@{data}", ex.getMessage()
                .split("; default message")[1].replaceAll("]|\\[| ", ""));
    }
}
