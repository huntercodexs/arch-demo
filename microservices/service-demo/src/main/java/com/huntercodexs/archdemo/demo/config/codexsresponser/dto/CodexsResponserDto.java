package com.huntercodexs.archdemo.demo.config.codexsresponser.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CodexsResponserDto {
    public int errorCode;
    public String message;
}
