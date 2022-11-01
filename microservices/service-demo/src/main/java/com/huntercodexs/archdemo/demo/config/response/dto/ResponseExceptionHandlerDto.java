package com.huntercodexs.archdemo.demo.config.response.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseExceptionHandlerDto {
    public int errorCode;
    public String message;
}
