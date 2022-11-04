package com.huntercodexs.archdemo.demo.rules;

import com.huntercodexs.archdemo.demo.config.response.errors.ResponseErrors;
import com.huntercodexs.archdemo.demo.config.response.exception.ResponseException;
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
            log.error("Rules Server Contact Failed: " + re.getMessage());
            throw new ResponseException(ResponseErrors.SERVICE_ERROR_RULES_FAIL);
        }

        if (!Objects.requireNonNull(response.getBody()).getStatus()) {
            log.error("Invalid Rules: " + response.getBody().getMessage());
            throw new ResponseException(ResponseErrors.SERVICE_ERROR_RULES_NOK);
        }

    }

}
