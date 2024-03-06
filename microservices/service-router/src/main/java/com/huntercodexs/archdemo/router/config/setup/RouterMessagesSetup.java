package com.huntercodexs.archdemo.router.config.setup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum RouterMessagesSetup {

    SERVICE_TOKEN_OK(
            HttpStatus.OK,
            20000,
            "service token ok"),

    SERVICE_TOKEN_ACCESS_CODE_ERROR(
            HttpStatus.UNAUTHORIZED,
            50001,
            "invalid access code"),

    SERVICE_TOKEN_ACCESS_TOKEN_ERROR(
            HttpStatus.UNAUTHORIZED,
            50002,
            "invalid access token"),

    SERVICE_TOKEN_BAD_REQUEST_ERROR(
            HttpStatus.BAD_REQUEST,
            50003,
            "service token (bad request)"),

    SERVICE_TOKEN_NOT_FOUND_ERROR(
            HttpStatus.NOT_FOUND,
            50004,
            "service token (not found)"),

    SERVICE_TOKEN_INTERNAL_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR,
            50005,
            "service token (internal error)"),

    SERVICE_TOKEN_BAD_GATEWAY_ERROR(
            HttpStatus.BAD_GATEWAY,
            50006,
            "service token (bad gateway)"),

    SERVICE_TOKEN_UNKNOWN_ERROR(
            HttpStatus.NOT_IMPLEMENTED,
            50007,
            "service token (unknown error)");

    public HttpStatus httpStatus;
    public int code;
    public String message;
}
