package com.huntercodexs.archdemo.demo.config.codexsresponser.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "This object refers to Codexs Responser Handler", name = "Codexs Responser")
public class CodexsResponserDto {

    @Schema(
            description = "Runtime code error or excetion defined in the application.",
            example = "9000",
            required = true)
    public int errorCode;

    @Schema(
            description = "Message refer to errorCode defined in the application.",
            example = "Address not found",
            required = true)
    public String message;
}
