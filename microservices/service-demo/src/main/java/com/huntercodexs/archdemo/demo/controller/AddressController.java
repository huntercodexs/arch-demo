package com.huntercodexs.archdemo.demo.controller;

import com.huntercodexs.archdemo.demo.dto.AddressRequestDto;
import com.huntercodexs.archdemo.demo.dto.AddressResponseDto;
import com.huntercodexs.archdemo.demo.service.AddressService;
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
public class AddressController {

    @Autowired
    AddressService addressService;

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
