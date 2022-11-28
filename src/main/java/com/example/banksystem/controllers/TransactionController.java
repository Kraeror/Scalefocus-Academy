package com.example.banksystem.controllers;

import com.example.banksystem.models.requsts.TransactionCreationRequest;
import com.example.banksystem.models.requsts.TransferCreationRequest;
import com.example.banksystem.models.responses.TransactionResponse;
import com.example.banksystem.models.responses.TransferResponse;
import com.example.banksystem.models.user.UserAuthenticationDetails;
import com.example.banksystem.services.interfaces.TransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class TransactionController {
	private final TransactionService transactionService;
	private final Logger logger = LogManager.getLogger(TransactionController.class);

	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	/**
	 * Creates a deposit transaction and add the given funds to the balance of an account.
	 *
	 * @param body        a {@link TransactionCreationRequest} entity in which all deposit information is inserted.
	 * @param userDetails a {@link UserAuthenticationDetails} entity with which we authenticate the user.
	 * @return a {@link TransactionResponse} containing the information of the created deposit.
	 */
	@PreAuthorize("hasAuthority('USER')")
	@PostMapping("/transactions/deposits")
	public ResponseEntity<TransactionResponse> makeDeposit(
													@Valid @RequestBody TransactionCreationRequest body,
													@AuthenticationPrincipal UserAuthenticationDetails userDetails) {
		TransactionResponse model = transactionService.deposit(userDetails.getId(), body);

		logger.info("User with name {} successfully made a deposit", userDetails.getFullName());
		return ResponseEntity.created(URI.create("/transactions/deposits/" + model.getId())).body(model);
	}

	/**
	 * Creates a withdrawal transaction and deduces the given funds from the balance of an account.
	 *
	 * @param body        a {@link TransactionCreationRequest} entity in which all withdraw information is inserted.
	 * @param userDetails a {@link UserAuthenticationDetails} entity with which we authenticate the user.
	 * @return a {@link TransactionResponse} containing the information of the created withdraw.
	 */
	@PreAuthorize("hasAuthority('USER')")
	@PostMapping("/transactions/withdrawals")
	public ResponseEntity<TransactionResponse> makeWithdrawal(
													@Valid @RequestBody TransactionCreationRequest body,
													@AuthenticationPrincipal UserAuthenticationDetails userDetails) {
		TransactionResponse model = transactionService.withdraw(userDetails.getId(), body);

		logger.info("User with name {} successfully made withdrawal", userDetails.getFullName());
		return ResponseEntity.created(URI.create("/transactions/withdrawals/" + model.getId())).body(model);
	}

	/**
	 * Creates a transfer transaction between two accounts.
	 *
	 * @param body        a {@link TransferCreationRequest} entity in which all transfer information is inserted.
	 * @param userDetails a {@link UserAuthenticationDetails} entity with which we authenticate the user.
	 * @return a {@link TransactionResponse} containing the information of the created transfer.
	 */
	@PreAuthorize("hasAuthority('USER')")
	@PostMapping("/transactions/transfers")
	public ResponseEntity<TransferResponse> makeTransfer(
													@Valid @RequestBody TransferCreationRequest body,
													@AuthenticationPrincipal UserAuthenticationDetails userDetails) {
		TransferResponse model = this.transactionService.transfer(userDetails.getId(), body);

		logger.info("User with name {} successfully made a transfer", userDetails.getFullName());
		return ResponseEntity.created(URI.create("/transactions/transfers/" + model.getId())).body(model);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/transactions")
	public ResponseEntity<List<TransactionResponse>> findTransactionsByCriteria(
					@RequestParam(required = false) String type,
					String dateOn,
					@RequestParam(required = false)
					String dateBefore,
					@RequestParam(required = false)
					String dateAfter) {
		return ResponseEntity.ok(transactionService.getAllTransactionsByGivenCriteria(type,
						dateOn, dateBefore, dateAfter));
	}

}