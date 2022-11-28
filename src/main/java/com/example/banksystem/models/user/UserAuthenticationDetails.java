package com.example.banksystem.models.user;

import com.example.banksystem.models.entities.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class UserAuthenticationDetails implements UserDetails {
	private final UserEntity user;

	public UserAuthenticationDetails(UserEntity user) {
		this.user = user;
	}
	public Long getId() {
		return this.user.getId();
	}

	public String getFullName() {
		return this.user.getFullName();
	}

	public String getEmail() {
		return this.user.getEmail();
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.user.getRoles()
						.stream().map(role -> new SimpleGrantedAuthority(role.getRole()))
						.collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return this.user.getPassword();
	}

	@Override
	public String getUsername() {
		return this.user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
