package com.example.banksystem.controllers;

import com.example.banksystem.models.entities.CDAccountEntity;
import com.example.banksystem.models.requsts.CDAccountCreationRequest;
import com.example.banksystem.models.requsts.TransactionCreationRequest;
import com.example.banksystem.models.responses.CDAccountResponse;
import com.example.banksystem.models.responses.CheckingAccountResponse;
import com.example.banksystem.models.responses.TransactionResponse;
import com.example.banksystem.models.user.UserAuthenticationDetails;
import com.example.banksystem.services.interfaces.CDAccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class CDAccountController {
    private final Logger logger = LogManager.getLogger(this.getClass());

    private final CDAccountService cdAccountService;

    public CDAccountController(CDAccountService cdAccountService) {
        this.cdAccountService = cdAccountService;
    }

    /**
     * Creates an {@link CDAccountEntity} with data from a given entity and assigns it to the
     * given user.
     *
     * @param cdAccountCreationRequest an entity in which the data for creating the new account is inserted.
     * @param userDetails       an entity with which we take the ID of the user to whom the account will be assigned.
     * @return an {@link CDAccountResponse} with the data of the saved account.
     */
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/certification-of-deposit-accounts")
    public ResponseEntity<CDAccountResponse> createCDAccount(
            @Valid @RequestBody CDAccountCreationRequest cdAccountCreationRequest,
            @AuthenticationPrincipal UserAuthenticationDetails userDetails, Errors errors) {

        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.getFieldErrors().get(0).getDefaultMessage());
        }

        CDAccountResponse cdAccountResponse = cdAccountService
                .createCdAccount(cdAccountCreationRequest, userDetails.getId());
        logger.info("User with username {} created a certification of deposit successfully.",
                userDetails.getUsername());
        return ResponseEntity.created(URI.create("/certification-of-deposit-accounts/" +
                cdAccountResponse.getId())).body(cdAccountResponse);
    }

    /**
     * Gets a {@link List} of all {@link CDAccountResponse}s from the database and outputs it
     * @return a {@link ResponseEntity} containing {@link List} of all {@link CDAccountResponse}s and
     * an HTTPS status of FOUND if everything went correctly.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/certification-of-deposit-accounts")
    public ResponseEntity<List<CDAccountResponse>> showAllCdAccounts() {
        return ResponseEntity.ok(cdAccountService.getAllCDAccountEntities());
    }

    /**
     * Gets a {@link CDAccountResponse} of an account, corresponding to the given Iban and returns it.
     * @param id the ID of the sought account.
     * @return a {@link ResponseEntity} containing the {@link CheckingAccountResponse} of the account
     * corresponding to the given ID.
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/certification-of-deposit-accounts/{id}")
    public ResponseEntity<CDAccountResponse> showCdAccountById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserAuthenticationDetails userDetails) {
        return ResponseEntity.ok(cdAccountService.getCDAccountEntityByID(id,userDetails.getId()));
    }

    /**
     * Gets a {@link CDAccountResponse} of an account, corresponding to the given Iban and returns it.
     * @param iban the iban of the sought account.
     * @return a {@link ResponseEntity} containing the {@link CDAccountResponse} of the account
     * corresponding to the given Iban.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/certification-of-deposit-accounts/")
    public ResponseEntity<CDAccountResponse> showCdAccountByIban(@RequestParam String iban) {
        return ResponseEntity.ok(cdAccountService.getCDAccountByIban(iban));
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/certification-of-deposit-accounts/withdrawals")
    public ResponseEntity<TransactionResponse> makeWithdrawal(
                                                    @Valid @RequestBody TransactionCreationRequest body,
                                                    @AuthenticationPrincipal UserAuthenticationDetails userDetails) {
        TransactionResponse model = cdAccountService.withdraw(userDetails.getId(), body);

        logger.info("Successfully transformed certificate of deposit account with {} iban to checking account",
                body.getIban());
        logger.info("User with name {} successfully made withdrawal ", userDetails.getFullName());
        return ResponseEntity.created(URI.create("/transactions/withdrawals/" + model.getId())).body(model);
    }
}
