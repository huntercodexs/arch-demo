package com.huntercodexs.archdemo.demo.service;

import com.huntercodexs.archdemo.demo.dto.AddressRequestDto;
import com.huntercodexs.archdemo.demo.dto.AddressResponseDto;
import com.huntercodexs.archdemo.demo.mapper.AddressResponseMapper;
import com.huntercodexs.archdemo.demo.rules.RulesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@Slf4j
public class AddressService {

    @Value("${spring.application.name}")
    String serviceId;

    @Autowired
    RulesService rulesService;

    @Autowired
    SyncService syncService;

    @Autowired
    AsyncService asyncService;

    public ResponseEntity<AddressResponseDto> getAddress(AddressRequestDto addressRequestDto) {

        /*Check Module Name Permissions*/
        rulesService.isRulesValid(addressRequestDto.getRulesCode(), serviceId);

        AddressResponseDto addressResponseDto;

        /*If Async Request*/
        if (addressRequestDto.getWebhook() != null && !addressRequestDto.getWebhook().equals("")) {
            asyncService.runAddressAsync(addressRequestDto.getPostalCode(), addressRequestDto.getWebhook());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(AddressResponseMapper.mapperInitialResponseDto());
        }

        /*If Sync Request*/
        addressResponseDto = syncService.runAddressSync(addressRequestDto.getPostalCode());
        return ResponseEntity.status(HttpStatus.OK).body(addressResponseDto);
    }
}
