package com.huntercodexs.archdemo.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CrossConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowedOrigins("http://localhost:9009")
                .allowedMethods("POST", "GET", "PUT", "DELETE", "PATCH");

        registry.addMapping("/api/users/**")
                .allowedOrigins("http://localhost:8080")
                .allowedMethods("POST", "GET", "PUT", "DELETE", "PATCH");
    }

}
