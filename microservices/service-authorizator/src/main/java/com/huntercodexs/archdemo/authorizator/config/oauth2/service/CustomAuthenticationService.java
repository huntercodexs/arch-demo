package com.huntercodexs.archdemo.authorizator.config.oauth2.service;

import com.huntercodexs.archdemo.authorizator.config.oauth2.model.OperatorEntity;
import com.huntercodexs.archdemo.authorizator.config.oauth2.repository.OperatorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomAuthenticationService implements AuthenticationManager {

    private final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationService.class);

    @Autowired
    CustomOperatorDetailsService customOperatorDetailsService;

    @Autowired
    OperatorRepository operatorRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return authenticateWithDatabase(authentication.getPrincipal().toString(), authentication.getCredentials().toString());
    }

    private Authentication authenticateWithDatabase(String usernameCred, String passwordCred) {
        if (usernameCred.equals("") || passwordCred.equals("")) {
            throw new BadCredentialsException("Missing Credentials");
        }

        try {

            OperatorEntity operatorEntity = operatorRepository.findByUsername(usernameCred);

            if (operatorEntity == null || operatorEntity.getUsername().equals("")) {
                throw new BadCredentialsException("Not Found Operator");
            }

            if (operatorEntity.getStatus() == 0) {
                throw new BadCredentialsException("Inactive Operator");
            }

            if (operatorEntity.getDeleted() == 1) {
                throw new BadCredentialsException("Deleted Operator");
            }

            if (!isValidCredentials(passwordCred, operatorEntity.getPassword())) {
                throw new BadCredentialsException("Invalid Credentials");
            }

            customOperatorDetailsService.setUserCredentialsFromDatabase(operatorEntity.getRole(), usernameCred, passwordCred);
            UserDetails userDetails = customOperatorDetailsService.loadUserByUsername(usernameCred);
            return new UsernamePasswordAuthenticationToken(userDetails, passwordCred, userDetails.getAuthorities());

        } catch (BadCredentialsException bc) {
            bc.printStackTrace();
            throw new BadCredentialsException("Credentials Fail: " + bc.getMessage());
        }

    }

    private boolean isValidCredentials(String passwordCredentials, String passwordAuthenticate) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String passwordBCrypt = bCryptPasswordEncoder.encode(passwordAuthenticate);
        return bCryptPasswordEncoder.matches(passwordCredentials, passwordBCrypt);
    }

}
