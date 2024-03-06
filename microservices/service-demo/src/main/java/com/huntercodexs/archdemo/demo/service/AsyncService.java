package com.huntercodexs.archdemo.demo.service;

import com.huntercodexs.archdemo.demo.dto.AddressResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RefreshScope
@Service
@Slf4j
public class AsyncService {

    @Autowired
    SyncService syncService;

    @Async
    public void runAddressAsync(String postalCode, String webhook) {
        sendAsyncResponse(syncService.runAddressSync(postalCode), webhook);
    }

    public void sendAsyncResponse(AddressResponseDto addressResponseDto, String endpointWebhook) {

        try {

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<AddressResponseDto> request = new HttpEntity<>(addressResponseDto);
            ResponseEntity<String> response = restTemplate.exchange(endpointWebhook, HttpMethod.POST, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED) {
                log.info("Webhook ["+endpointWebhook+"] received response ");
                System.out.println("Webhook ["+endpointWebhook+"] received response ");
            }

        }  catch (Exception e) {
            log.error("[Exception] " + e.getCause() + " Message " + e.getMessage());
            System.out.println("[Exception] " + e.getCause() + " Message " + e.getMessage());
        }

    }

}
