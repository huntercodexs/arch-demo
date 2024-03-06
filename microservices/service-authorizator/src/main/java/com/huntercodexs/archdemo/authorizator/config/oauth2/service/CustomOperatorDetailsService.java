package com.huntercodexs.archdemo.authorizator.config.oauth2.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CustomOperatorDetailsService implements UserDetailsService {

	private final Logger LOGGER = LoggerFactory.getLogger(CustomClientDetailsService.class);

	private String usernameCred;
	private String passwordCred;
	private List<GrantedAuthority> authorities;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LoggedOperator loggedOperator = new LoggedOperator(this.usernameCred, this.passwordCred, this.authorities);

		UserDetails userDetails = new User(
				username,
				loggedOperator.getPassword(),
				true,
				false,
				false,
				false,
				this.authorities);

		if (!loggedOperator.isAccountNonExpired() || !loggedOperator.isCredentialsNonExpired() || !loggedOperator.isAccountNonLocked()) {

			return new User(
					this.usernameCred,
					this.passwordCred,
					false,
					true,
					true,
					true,
					AuthorityUtils.NO_AUTHORITIES);

		}

		return userDetails;

	}

	public void setUserCredentialsFromDatabase(String userRole, String usernameCred, String passwordCred) {
		this.usernameCred = usernameCred;
		this.passwordCred = passwordCred;
		this.authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(userRole);
	}

}

