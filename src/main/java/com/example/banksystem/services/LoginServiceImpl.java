package com.example.banksystem.services;

import com.example.banksystem.models.requsts.UserLoginRequest;
import com.example.banksystem.models.responses.UserResponse;
import com.example.banksystem.services.interfaces.LoginService;
import com.example.banksystem.services.interfaces.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class LoginServiceImpl implements LoginService {

	private final UserDetailsService userDetailsService;
	private final UserService userService;
	private final AuthenticationManager authenticationManager;

	public LoginServiceImpl(UserDetailsService userDetailsService,
	                        UserService userService, AuthenticationManager authenticationManager) {
		this.userDetailsService = userDetailsService;
		this.userService = userService;
		this.authenticationManager = authenticationManager;
	}

	@Override
	public UserResponse authenticateUser(HttpServletRequest request, UserLoginRequest userLoginRequest) {
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(userLoginRequest.getUsername());

		UsernamePasswordAuthenticationToken token =
						new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
										userLoginRequest.getPassword(), userDetails.getAuthorities());
		Authentication authentication = authenticationManager.authenticate(token);

		if (!authentication.isAuthenticated()) {
			throw new BadCredentialsException("Invalid password!");
		}

		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(authentication);
		HttpSession session = request.getSession(true);
		session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);

		return userService.getUserByUsername(userDetails.getUsername());
	}
}
