package com.huntercodexs.archdemo.rules.config.archdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

@Slf4j
@Service
public class AliveService {

    @Value("${eureka.security.login}")
    String basicAuthService;

    public String alive(HttpServletRequest request) {
        String basicAuthRequest = new String(Base64.getDecoder().decode(
                request.getHeader("Authorization").replaceFirst("Basic ", "")));
        if (basicAuthRequest.equals(basicAuthService)) {
            return "alive";
        }
        return null;
    }

}
