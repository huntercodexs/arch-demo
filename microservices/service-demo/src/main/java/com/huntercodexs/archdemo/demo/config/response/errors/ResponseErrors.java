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
            140,
            "Address not found");

    public HttpStatus statusCode;
    public int errorCode;
    public String message;
}
