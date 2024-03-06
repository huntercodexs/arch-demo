package com.huntercodexs.archdemo.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "This object refers to Address Response", name = "Address Response")
public class AddressResponseDto {

    @Schema(
            description = "Postal number refer to query.",
            example = "12090002",
            required = true)
    public String cep;

    @Schema(
            description = "Name of the place found",
            example = "R Alameda Santos",
            required = true)
    public String logradouro;

    @Schema(
            description = "Addtional information about address found.",
            example = "Ao lado do shopping BigHouse",
            required = true)
    public String complemento;

    @Schema(
            description = "District name.",
            example = "Campo Belo",
            required = true)
    public String bairro;

    @Schema(
            description = "City or downtown.",
            example = "Sao Paulo",
            required = true)
    public String localidade;

    @Schema(
            description = "State Acronym.",
            example = "PR",
            required = true)
    public String uf;

    @Schema(
            description = "Others information about address (ibge).",
            example = "12345",
            required = false)
    public String ibge;

    @Schema(
            description = "Username of the user to be create (email or cpf).",
            example = "12345",
            required = false)
    public String gia;

    @Schema(
            description = "Phone Digit Code to address found (ddd).",
            example = "88888888888",
            required = false)
    public String ddd;

    @Schema(
            description = "Others information about address (siafi).",
            example = "12345",
            required = false)
    public String siafi;
}
