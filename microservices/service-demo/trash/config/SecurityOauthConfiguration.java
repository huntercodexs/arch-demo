package com.huntercodexs.archdemo.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

import java.util.Base64;

@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityOauthConfiguration extends ResourceServerConfigurerAdapter {
	
	@Value("${oauth2.url.check-token}")
	private String oauthServerUrl;
	@Value("${oauth2.client-id}")
	private String user;
	@Value("${oauth2.client-secret}")
	private String password;
	
	@Override
	public void configure( ResourceServerSecurityConfigurer resources ) throws Exception {
		resources.tokenServices( remoteTokenServices() );
	}
	
	@Bean
	public RemoteTokenServices remoteTokenServices() {
	    
		RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
	    remoteTokenServices.setCheckTokenEndpointUrl(oauthServerUrl);
	    remoteTokenServices.setClientId(user);

		String secret = new String(Base64.getDecoder().decode(password));
		remoteTokenServices.setClientSecret(secret);
		remoteTokenServices.setAccessTokenConverter(accessTokenConverter());

		return remoteTokenServices;
		
	}
	
	@Bean
	public AccessTokenConverter accessTokenConverter() {
	    return new DefaultAccessTokenConverter();
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.requestMatcher(new OAuthRequestedMatcher())
				.authorizeRequests()
				.antMatchers("/actuator/**").permitAll().anyRequest().authenticated();
	}

}
