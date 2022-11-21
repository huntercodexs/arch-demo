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
@Schema(description = "This object refers to Address Request", name = "Address Request")
public class AddressRequestDto {

    @Schema(
            description = "Rules code to access rules server according with the rules defined.",
            example = "XYZ-123",
            required = true)
    @NotNull @NotEmpty @NotBlank
    String rulesCode;

    @Schema(
            description = "Postal code number (only numbers).",
            example = "12090002",
            required = true)
    @NotNull @NotEmpty @NotBlank
    String postalCode;

    @Schema(
            description = "Webhook callback.",
            example = "http://api.sample.com/receptor",
            required = false)
    String webhook;
}
