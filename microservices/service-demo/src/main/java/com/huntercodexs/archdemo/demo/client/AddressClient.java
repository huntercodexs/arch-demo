package com.huntercodexs.archdemo.demo.client;

import com.huntercodexs.archdemo.demo.dto.AddressResponseDto;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class AddressClient {

    @Value("${service.address-search.url}")
    String urlAddressSearch;

    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<AddressResponseDto> addressSearch(String postalCode) {
        String urlAddressSearch = this.urlAddressSearch.replaceFirst("@postal_code", postalCode);
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(null);

        try {
            return restTemplate.exchange(urlAddressSearch, HttpMethod.GET, httpEntity, AddressResponseDto.class);
        } catch (RuntimeException re) {
            System.out.println("[EXCEPTION] " + re.getMessage());
            return null;
        }
    }

}
