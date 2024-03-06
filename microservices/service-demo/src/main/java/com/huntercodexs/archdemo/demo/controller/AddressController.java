package com.huntercodexs.archdemo.demo.controller;

import com.huntercodexs.archdemo.demo.config.codexsresponser.dto.CodexsResponserDto;
import com.huntercodexs.archdemo.demo.dto.AddressRequestDto;
import com.huntercodexs.archdemo.demo.dto.AddressResponseDto;
import com.huntercodexs.archdemo.demo.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("${api.prefix}")
@Tag(name = "Address Service")
public class AddressController {

    @Autowired
    AddressService addressService;

    @Operation(
            summary = "Find Address",
            description = "Microservice to get an address from anyone postal code"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address found successfull", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AddressResponseDto.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CodexsResponserDto.class))
            }),
            @ApiResponse(responseCode = "401", description = "Access denied", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CodexsResponserDto.class))
            }),
            @ApiResponse(responseCode = "404", description = "Address not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CodexsResponserDto.class))
            }),
            @ApiResponse(responseCode = "406", description = "Not accceptable", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CodexsResponserDto.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal error", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CodexsResponserDto.class))
            })
    })
    @PostMapping(path = "/address", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<AddressResponseDto> getAddress(
            @Valid @RequestBody(required = true) AddressRequestDto addressRequestDto
    ) {
        /*
        * POST http://localhost:33001/huntercodexs/arch-demo/service-demo/api/address
        * BODY {"rulesCode":"XYZ12345","postalCode":"12090002","webhook":""}
        */
        System.out.println("CHEGOU AQUI <<<<<<<<<<<<<<<<<<<<<!");
        return addressService.getAddress(addressRequestDto);
    }

}
