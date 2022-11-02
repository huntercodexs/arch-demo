package com.huntercodexs.archdemo.demo.config.response.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ResponseErrors {

    SERVICE_ERROR_MISSING_DATA(
            HttpStatus.BAD_REQUEST,
            100,
            "Missing Data [@{data}], please check the request"),

    SERVICE_ERROR_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            120,
            "Address not found"),

    SERVICE_ERROR_RULES_NOK(
            HttpStatus.UNAUTHORIZED,
            140,
            "Rules is not OK"),

    SERVICE_ERROR_RULES_DOWN(
            HttpStatus.INTERNAL_SERVER_ERROR,
            150,
            "Rules Server is DOWN"),

    SERVICE_ERROR_TEST(
            HttpStatus.ACCEPTED,
            160,
            "This is only a test"),

    SERVICE_ERROR_INTERNAL(
            HttpStatus.INTERNAL_SERVER_ERROR,
            180,
            "Internal Server Error");;

    public HttpStatus statusCode;
    public int errorCode;
    public String message;
}
