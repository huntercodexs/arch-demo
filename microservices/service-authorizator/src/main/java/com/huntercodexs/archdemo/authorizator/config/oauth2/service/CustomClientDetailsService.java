package com.huntercodexs.archdemo.authorizator.config.oauth2.service;

import com.huntercodexs.archdemo.authorizator.config.oauth2.model.AuthorizatorEntity;
import com.huntercodexs.archdemo.authorizator.config.oauth2.repository.AuthorizationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.huntercodexs.archdemo.authorizator.config.oauth2.service.RoleOperator.ROLE_CLIENT;

@Service
public class CustomClientDetailsService implements ClientDetailsService {

    private final Logger LOGGER = LoggerFactory.getLogger(CustomClientDetailsService.class);

	@Autowired
    AuthorizationRepository authorizationRepository;

    @Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

        BaseClientDetails details = new BaseClientDetails();

        AuthorizatorEntity authorizatorEntity = authorizationRepository.findByClient(clientId);
        String secret = new String(Base64.getDecoder().decode(authorizatorEntity.getSecret()));

        details.setClientId(clientId);
        details.setClientSecret(secret);
        details.setAuthorizedGrantTypes(Arrays.asList("password", "authorization_code", "refresh_token"));

        details.setScope(Collections.singletonList("read"));
        if (authorizatorEntity.getScope().equals("read-write")) {
            details.setScope(Arrays.asList("read", "write")); //trust
        }

        details.setResourceIds(Collections.singletonList("oauth2-resource"));
        details.setAccessTokenValiditySeconds(authorizatorEntity.getAccessTokenValiditySeconds());
        details.setRefreshTokenValiditySeconds(authorizatorEntity.getRefreshTokenValiditySeconds());

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(ROLE_CLIENT.name()));

        details.setAuthorities(authorities);

        return details;
	}
	
}
