package com.huntercodexs.archdemo.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "This object refers to Rules Request", name = "RulesRequest")
public class RulesRequestDto {

    @Schema(
            description = "The service identification on platform.",
            example = "SERVICE-DEMO-ADDRESS",
            required = true)
    @NotNull @NotEmpty @NotBlank
    String serviceId;

    @Schema(
            description = "The rule code to check if service id has a correct permission to access platform resources.",
            example = "XYZ12345",
            required = true)
    @NotNull @NotEmpty @NotBlank
    String rulesCode;
}
