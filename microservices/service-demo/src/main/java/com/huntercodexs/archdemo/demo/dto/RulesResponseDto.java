package com.huntercodexs.archdemo.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "This object refers to RulesResponse", name = "RulesResponse")
public class RulesResponseDto {

    @Schema(
            description = "Describe the status false to wrong rules code.",
            example = "false",
            required = true)
    public Boolean status;

    @Schema(
            description = "Describe the message to wrong rules code.",
            example = "Rules is not ok",
            required = true)
    public String message;
}
