package com.example.banksystem.controllers;

import com.example.banksystem.models.requsts.PasswordChangeRequest;
import com.example.banksystem.models.requsts.UserUpdateRequest;
import com.example.banksystem.models.responses.CDAccountResponse;
import com.example.banksystem.models.responses.CheckingAccountResponse;
import com.example.banksystem.models.responses.TransactionResponse;
import com.example.banksystem.models.responses.UserResponse;
import com.example.banksystem.models.user.UserAuthenticationDetails;
import com.example.banksystem.services.interfaces.CDAccountService;
import com.example.banksystem.services.interfaces.CheckingAccountService;
import com.example.banksystem.services.interfaces.TransactionService;
import com.example.banksystem.services.interfaces.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

	private final UserService userService;
	private final CheckingAccountService checkingAccountService;
	private final CDAccountService cdAccountService;
	private final TransactionService transactionService;
	private final Logger logger = LogManager.getLogger(UserController.class);

	public UserController(UserService userService, CheckingAccountService checkingAccountService,
						  CDAccountService cdAccountService, TransactionService transactionService) {
		this.userService = userService;
		this.checkingAccountService = checkingAccountService;
		this.cdAccountService = cdAccountService;
		this.transactionService = transactionService;
	}

	/**
	 * Retrieves the user with a given username.
	 *
	 * @param username the username of the selected UserEntity.
	 * @return a {@link UserResponse}.
	 */
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(value = "/users", params = {"username"})
	public ResponseEntity<UserResponse> getUserByUsername(@RequestParam String username) {

		logger.info("Displays user with username " + username);
		return ResponseEntity.ok(userService.getUserByUsername(username));
	}

	/**
	 * Retrieves the user with a given ID.
	 *
	 * @param id the id of the selected UserEntity.
	 * @return a {@link UserResponse}.
	 */
	@PreAuthorize("hasAuthority('USER')")
	@GetMapping(value = "/users/{id}")
	public ResponseEntity<UserResponse> getUserById(
			@PathVariable Long id,@AuthenticationPrincipal UserAuthenticationDetails userDetails) {

		logger.info("Displays user with ID " + id);
		return ResponseEntity.ok(userService.getUserById(id,userDetails.getId()));
	}

	/**
	 * Retrieves a list of all users.
	 *
	 * @return a list of {@link UserResponse}.
	 */
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(value = "/users")
	public ResponseEntity<List<UserResponse>> getAllUsers() {
		List<UserResponse> userResponseList = userService.getAllUser();

		logger.info("Displays a list of users");
		return ResponseEntity.ok(userResponseList);
	}

	/**
	 * Updates the details of an already existing user.
	 *
	 * @param userUpdateRequest an {@link UserUpdateRequest} an entity that holds the parameters
	 *                          to update the fields.
	 * @param id                the ID of the user which will be updated.
	 * @return a {@link UserResponse} with the updated fields.
	 */
	@PreAuthorize("hasAuthority('USER')")
	@PatchMapping(value = "/users/{id}")
	public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UserUpdateRequest userUpdateRequest,
	                                               @PathVariable Long id,
												   @AuthenticationPrincipal UserAuthenticationDetails userDetails) {

		UserResponse userResponse = userService.updateUser(userUpdateRequest, id ,userDetails.getId());
		logger.info("User {} changed his personal information.", userResponse.getFullName());
		return ResponseEntity.ok(userResponse);
	}

	/**
	 * Changes the user's password.
	 *
	 * @param passwordChangeRequest a {@link PasswordChangeRequest} with the parameters for the password change.
	 * @param userDetails           a {@link UserAuthenticationDetails} entity from which we receive an ID of the user
	 *                              whose password will be changed.
	 * @param errors                stores and exposes information about data-binding and validation errors for a user.
	 * @return a message.
	 */
	@PreAuthorize("hasAuthority('USER')")
	@PatchMapping("/users/password-change")
	public ResponseEntity<String> changePassword(@Valid @RequestBody PasswordChangeRequest passwordChangeRequest,
	                                             @AuthenticationPrincipal UserAuthenticationDetails userDetails,
	                                             Errors errors) {
		if (errors.hasErrors()) {
			throw new IllegalArgumentException(errors.getFieldErrors().get(0).getDefaultMessage());
		}
		userService.changePassword(userDetails.getId(), passwordChangeRequest);

		logger.info("User with the name {} changed his password", userDetails.getFullName());
		return ResponseEntity.ok("Password changed successfully.");
	}

	/**
	 * Gets a  {@link List} of {@link CheckingAccountResponse}s with a given userId from the database and outputs it.
	 *
	 * @param userId the id of the user to whom the accounts are assigned.
	 * @return a {@link ResponseEntity} containing {@link List} of {@link CheckingAccountResponse}s and an HTTPS status
	 * of FOUND if everything went correctly.
	 */
	@PreAuthorize("hasAuthority('USER')")
	@GetMapping("/users/{userId}/checking-accounts")
	public ResponseEntity<List<CheckingAccountResponse>> getAllCheckingAccountsOfUser(
													@PathVariable Long userId,
													@AuthenticationPrincipal UserAuthenticationDetails userDetails) {
		logger.info("Returns all checking accounts of user with ID " + userId);
		return new ResponseEntity<>(checkingAccountService
				.getAllAccountResponsesByUserId(userId, userDetails.getId()), HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('USER')")
	@GetMapping("/users/{userId}/certification-of-deposit-accounts")
	public ResponseEntity<List<CDAccountResponse>> getAllCDAccountsOfUser(
			@PathVariable Long userId,
			@AuthenticationPrincipal UserAuthenticationDetails userDetails) {
		logger.info("Returns all checking accounts of user with ID " + userId);
		return new ResponseEntity<>(cdAccountService
				.getAllCDAccountResponsesByUserId(userId, userDetails.getId()), HttpStatus.OK);
	}

	/**
	 * Returns a {@link List} of {@link TransactionResponse} of a given account that belongs to the currently logged-in user.
	 * <p> A variety of query params can be given to filter through the transactions.
	 * @param userId the id of the user.
	 * @param accountId the id of the user's account.
	 * @param type type of transaction to filter by.
	 * @param dateOn date on which the transactions are made.
	 * @param dateBefore a date before which the transactions were made.
	 * @param dateAfter a date after which the transactions are made.
	 * @return a {@link List} of {@link TransactionResponse}, filtered depending on the given params.
	 */
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/users/{userId}/accounts/{accountId}/transactions")
	public ResponseEntity<List<TransactionResponse>> findTransactionsByCriteria
			(@PathVariable Long userId, @PathVariable Long accountId, @RequestParam(required = false) String type,
			 @RequestParam(required = false) String dateOn,
			 @RequestParam(required = false) String dateBefore,
			 @RequestParam(required = false) String dateAfter) {
		return ResponseEntity.ok(transactionService.getAllTransactionsForUserByGivenCriteria(userId, accountId, type,
				dateOn, dateBefore, dateAfter));
	}


	/**
	 * Returns a {@link List} of {@link TransactionResponse} of a given account, admin only method.
	 * <p> A variety of query params can be given to filter through the transactions.
	 * @param userDetails the details of the logged-in user.
	 * @param accountId the id of the logged-in user.
	 * @param type type of transaction to filter by.
	 * @param dateOn date on which the transactions are made.
	 * @param dateBefore a date before which the transactions were made.
	 * @param dateAfter a date after which the transactions are made.
	 * @return a {@link List} of {@link TransactionResponse}, filtered depending on the given params.
	 */
	@PreAuthorize("hasAuthority('USER')")
	@GetMapping("/users/accounts/{accountId}/transactions")
	public ResponseEntity<List<TransactionResponse>> findTransactionsByCriteriaForUser
			(@AuthenticationPrincipal UserAuthenticationDetails userDetails,
			 @PathVariable Long accountId, @RequestParam(required = false) String type,
			 @RequestParam(required = false) String dateOn,
			 @RequestParam(required = false) String dateBefore,
			 @RequestParam(required = false) String dateAfter) {
		return ResponseEntity.ok(transactionService.getAllTransactionsForUserByGivenCriteria(userDetails.getId(),
				accountId, type, dateOn, dateBefore, dateAfter));
	}

}
