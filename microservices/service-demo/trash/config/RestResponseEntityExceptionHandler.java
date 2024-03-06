package com.huntercodexs.archdemo.demo.config;

import com.huntercodexs.archdemo.demo.dto.response.RestServiceErrorResponseDto;
import com.huntercodexs.archdemo.demo.exception.MMBISException;
import com.huntercodexs.archdemo.demo.exception.MMBISExceptionZipcode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
 
    @ExceptionHandler(value = {MMBISExceptionZipcode.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {

    	RestServiceErrorResponseDto restServiceErrorResponseDto = new RestServiceErrorResponseDto();

		if (ex instanceof MMBISException) {
    		
    		MMBISException mmbisException = (MMBISException) ex;
    		restServiceErrorResponseDto.setCodeError(mmbisException.getCodeError());
    		restServiceErrorResponseDto.setMessage(mmbisException.getMessage());
    		restServiceErrorResponseDto.setSubCodeError(mmbisException.getSubCodeError());

            return handleExceptionInternal(
					ex,
					restServiceErrorResponseDto,
                    new HttpHeaders(),
					HttpStatus.CONFLICT,
					request);

    	} else {

    		restServiceErrorResponseDto.setMessage("Unknown error");

    		return handleExceptionInternal(
					ex,
					ex.getMessage(),
                    new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR,
					request);

    	}
    }
}
