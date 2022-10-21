package com.huntercodexs.archdemo.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import java.util.Base64;

@EnableOAuth2Client
@Configuration
public class OAuth2Client {

	@Value("${oauth2.url.token}")
	String accessTokenUri;
	@Value("${oauth2.client-id}")
	String clientId;
	@Value("${oauth2.client-secret}")
	String clientSecret;
	@Value("${oauth2.grant-type:password}")
	String grantType;
    
    @Autowired
    private OAuth2ClientContext oauth2Context;

    @Bean
    public OAuth2ProtectedResourceDetails credentials() {

        ClientCredentialsResourceDetails details = new ClientCredentialsResourceDetails();
        details.setAccessTokenUri(accessTokenUri);
        details.setClientId(clientId);
        details.setClientSecret(new String(Base64.getUrlDecoder().decode(clientSecret)));
        details.setGrantType(grantType);

        return details;

    }

    @Bean
    public OAuth2RestTemplate restTemplate() {
        return new OAuth2RestTemplate(credentials(), oauth2Context);
    }

}
