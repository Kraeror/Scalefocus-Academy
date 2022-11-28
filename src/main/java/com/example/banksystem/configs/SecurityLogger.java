package com.example.banksystem.configs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.security.access.event.AuthorizationFailureEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

@Component
public class SecurityLogger {

	private final Logger log = LogManager.getLogger(SecurityLogger.class);

	@EventListener
	public void authenticated(final @NonNull AuthenticationSuccessEvent event) {
		final String username = event.getAuthentication().getName();
		log.info(new ParameterizedMessage("Successful login - [username: {}]", username));
	}

	@EventListener
	public void authenticationFailure(final @NonNull AbstractAuthenticationFailureEvent event){
		final String username = event.getAuthentication().getName();
		log.info(new ParameterizedMessage("Unsuccessful login - [username: {}]", username));
	}

	@EventListener
	public void authorizationFailureEvent(final @NonNull AuthorizationFailureEvent event){
		final String username = event.getAuthentication().getName();
		final String message = event.getAccessDeniedException().getMessage();
		log.error(new ParameterizedMessage
							("Unauthorized access - [username: {}, message: {}]", username,
									Optional.ofNullable(message).map(Function.identity()).orElse("<null>")));
	}
}
