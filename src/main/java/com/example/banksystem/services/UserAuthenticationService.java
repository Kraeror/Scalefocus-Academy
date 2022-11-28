package com.example.banksystem.services;

import com.example.banksystem.models.entities.UserEntity;
import com.example.banksystem.models.user.UserAuthenticationDetails;
import com.example.banksystem.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationService implements UserDetailsService {
	private final UserRepository userRepository;

	public UserAuthenticationService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user = this.userRepository
						.findByUsername(username)
						.orElseThrow(() ->
								new UsernameNotFoundException(String.format("Username %s does not exists!", username)));

		return new UserAuthenticationDetails(user);
	}
}
