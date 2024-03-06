package com.huntercodexs.archdemo.router.config.oauth2.client;

import com.huntercodexs.archdemo.router.config.oauth2.repository.Oauth2ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

@Slf4j
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class Oauth2Client extends ResourceServerConfigurerAdapter {

	@Value("${oauth2.client-id}")
	private String username;

	@Value("${oauth2.client-secret}")
	private String password;

	@Value("${oauth2.url.check-token}")
	private String oauth2ServerCheckTokenUrl;

	@Autowired
	private Oauth2ClientRepository oauth2ClientRepository;

	@Bean
	public AccessTokenConverter accessTokenConverter() {
		DefaultAccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();
	    return tokenConverter;
	}

	@Bean
	public RemoteTokenServices remoteTokenServices() {
		RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
		AccessTokenConverter accessTokenConverter = accessTokenConverter();
		String secret = new String(Base64.getDecoder().decode(password));

		remoteTokenServices.setCheckTokenEndpointUrl(oauth2ServerCheckTokenUrl);
		remoteTokenServices.setClientId(username);
		remoteTokenServices.setClientSecret(secret);
		remoteTokenServices.setAccessTokenConverter(accessTokenConverter);

		return remoteTokenServices;
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		RemoteTokenServices remoteToken = remoteTokenServices();
		resources.tokenServices(remoteToken);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/admin/**").permitAll()
			.antMatchers("/huntercodexs/arch-demo/service-authorizator/api/rest/oauth/v1/oauth/**").permitAll()
			.antMatchers("/actuator/**").permitAll()
			.anyRequest().authenticated();
	}

}
