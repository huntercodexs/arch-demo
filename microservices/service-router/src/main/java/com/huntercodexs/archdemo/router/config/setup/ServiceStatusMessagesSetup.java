package com.huntercodexs.archdemo.router.config.setup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ServiceStatusMessagesSetup {

    SERVICE_STATUS_OK(
            HttpStatus.OK,
            20000,
            "service is up"),

    SERVICE_STATUS_BASIC_AUTH_ERROR(
            HttpStatus.UNAUTHORIZED,
            50001,
            "status service (basic auth error)"),

    SERVICE_STATUS_BAD_REQUEST_ERROR(
            HttpStatus.BAD_REQUEST,
            50002,
            "status service (bad request)"),

    SERVICE_STATUS_NOT_FOUND_ERROR(
            HttpStatus.NOT_FOUND,
            50003,
            "status service (not found)"),

    SERVICE_STATUS_INTERNAL_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR,
            50004,
            "status service (internal error)"),

    SERVICE_STATUS_BAD_GATEWAY_ERROR(
            HttpStatus.BAD_GATEWAY,
            50005,
            "status service (unavailable)"),

    SERVICE_STATUS_UNKNOWN_ERROR(
            HttpStatus.NOT_IMPLEMENTED,
            50006,
            "status service (unknown error)");

    public HttpStatus httpStatus;
    public int code;
    public String message;
}
