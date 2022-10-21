package com.huntercodexs.archdemo.demo.rules;

import com.huntercodexs.archdemo.demo.dto.RulesRequestDto;
import com.huntercodexs.archdemo.demo.dto.RulesResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RefreshScope
@Service
@Transactional
@Slf4j
public class RulesService {

    @Value("${service.rules.url}")
    String rulesUrl;

    @Value("${router.access-code}")
    String accessCode;

    @Autowired
    HttpServletRequest httpServletRequest;

    private final RestTemplate restTemplate = new RestTemplate();

    private String getAccessToken() {
        /*Get OAuth2 Access Token*/
        return httpServletRequest.getHeader("Authorization");
    }

    private HttpHeaders httpRequestHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", getAccessToken());
        httpHeaders.set("Access-Code", accessCode);
        return httpHeaders;
    }

    public void isRulesValid(String code, String serviceId) {

        RulesRequestDto rulesRequestDto = new RulesRequestDto();
        rulesRequestDto.setRulesCode(code);
        rulesRequestDto.setServiceId(serviceId);

        HttpEntity<RulesRequestDto> httpEntity = new HttpEntity<>(rulesRequestDto, httpRequestHeaders());
        ResponseEntity<RulesResponseDto> response = null;

        try {
            response = restTemplate.postForEntity(rulesUrl, httpEntity, RulesResponseDto.class);
        } catch (RuntimeException re) {
            throw new RuntimeException("[Exception] Invalid Rules: " + re.getMessage());
        }

        if (!Objects.requireNonNull(response.getBody()).getStatus()) {
            throw new RuntimeException("Invalid Rules: " + response.getBody().getMessage());
        }
    }

}
