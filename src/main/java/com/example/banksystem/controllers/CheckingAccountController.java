package com.example.banksystem.controllers;

import com.example.banksystem.models.entities.CheckingAccountEntity;
import com.example.banksystem.models.responses.CheckingAccountResponse;
import com.example.banksystem.models.user.UserAuthenticationDetails;
import com.example.banksystem.services.interfaces.CheckingAccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class CheckingAccountController {
	private final CheckingAccountService checkingAccountService;
	private final Logger logger = LogManager.getLogger(CheckingAccountController.class);
	public CheckingAccountController(CheckingAccountService checkingAccountService) {
		this.checkingAccountService = checkingAccountService;
	}

	/**
	 * Gets a {@link CheckingAccountResponse} of an account, corresponding to the given id and returns it.
	 * @param id the ID of the sought account.
	 * @return a {@link ResponseEntity} containing the {@link CheckingAccountResponse} of the account
	 * corresponding to the given ID.
	 */
	@PreAuthorize("hasAuthority('USER')")
	@GetMapping("/checking-accounts/{id}")
	public ResponseEntity<CheckingAccountResponse> showCheckingAccountById(
			@PathVariable Long id,
			@AuthenticationPrincipal UserAuthenticationDetails userDetails) {
		return ResponseEntity.ok(checkingAccountService.getCheckingAccountById(id,userDetails.getId()));
	}

	/**
	 * Creates an {@link CheckingAccountEntity} and assigns it to the given user.
	 *
	 * @param userDetails                 an entity with which we take the ID of the user to whom the account will be
	 * assigned.
	 * @return an {@link CheckingAccountResponse} with the data of the saved account.
	 */
	@PreAuthorize("hasAuthority('USER')")
	@PostMapping("/checking-accounts")
	public ResponseEntity<CheckingAccountResponse> createCheckingAccount(
			@AuthenticationPrincipal UserAuthenticationDetails userDetails) {

		CheckingAccountResponse checkingAccountResponse = checkingAccountService.createAccount(userDetails.getId());

		logger.info("User with username {} created a checking account successfully.", userDetails.getUsername());
		return ResponseEntity.created
				(URI.create("/checking-accounts/" + checkingAccountResponse.getId())).body(checkingAccountResponse);
	}

	/**
	 * Gets a {@link List} of all {@link CheckingAccountResponse}s from the database and outputs it
	 * @return a {@link ResponseEntity} containing {@link List} of all {@link CheckingAccountResponse}s and
	 * an HTTPS status of FOUND if everything went correctly.
	 */
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/checking-accounts")
	public ResponseEntity<List<CheckingAccountResponse>> getAllAccounts() {
		return new ResponseEntity<>(checkingAccountService.getAllAccountResponses(),HttpStatus.OK);
	}

	/**
	 * Gets a {@link CheckingAccountResponse} of an account, corresponding to the given Iban and returns it.
	 * @param iban the iban of the sought account.
	 * @return a {@link ResponseEntity} containing the {@link CheckingAccountResponse} of the account
	 * corresponding to the given Iban.
	 */
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/checking-accounts/")
	public ResponseEntity<CheckingAccountResponse> getAccountByIban(@RequestParam String iban){
		return new ResponseEntity<>(checkingAccountService.getAccountResponseByIban(iban),HttpStatus.OK);
	}
}
