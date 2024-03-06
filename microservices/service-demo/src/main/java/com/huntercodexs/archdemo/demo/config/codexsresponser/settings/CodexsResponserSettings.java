package com.huntercodexs.archdemo.demo.config.codexsresponser.settings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

public abstract class CodexsResponserSettings extends ResponseEntityExceptionHandler {

    /**
     * DO NOT REMOVE THESE ATTRIBUTES AND ENUMERATOR
     * Just change it as needed
     * */

    protected static final int unknownErrorCode = 999;
    protected static final String unknownErrorMessage = "Unknown error";
    protected static final String replacementString = "@{data}";
    protected static final String missingDataMessage = "Missing Data ["+replacementString+"], please check the request";

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public enum codexsResponserExpectedErrors {

        SERVICE_ERROR_ADDRESS_CLIENT(
                HttpStatus.INTERNAL_SERVER_ERROR,
                10,
                "[EXCEPTION] Occours an error an AddressClient: "),

        SERVICE_ERROR_ACCESS_DENIED(
                HttpStatus.UNAUTHORIZED,
                111,
                "Access Denied"),

        SERVICE_ERROR_NOT_FOUND(
                HttpStatus.NOT_FOUND,
                120,
                "Address not found"),

        SERVICE_ERROR_RULES_NOK(
                HttpStatus.BAD_REQUEST,
                140,
                "Rules is not OK"),

        SERVICE_ERROR_RULES_FAIL(
                HttpStatus.INTERNAL_SERVER_ERROR,
                150,
                "Rules Server Contact Failed"),

        SERVICE_ERROR_TEST(
                HttpStatus.NOT_ACCEPTABLE,
                160,
                "This is only a test"),

        SERVICE_ERROR_INTERNAL(
                HttpStatus.INTERNAL_SERVER_ERROR,
                180,
                "Internal Server Error");

        public HttpStatus statusCode;
        public int errorCode;
        public String message;
    }

}
