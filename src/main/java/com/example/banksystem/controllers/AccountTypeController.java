package com.example.banksystem.controllers;

import com.example.banksystem.models.responses.AccountTypeResponse;
import com.example.banksystem.services.interfaces.AccountTypeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountTypeController {

    private final AccountTypeService accountTypeService;
    private final Logger logger = LogManager.getLogger(AccountTypeController.class);

    public AccountTypeController(AccountTypeService accountTypeService) {
        this.accountTypeService = accountTypeService;
    }

    /**
     * Gets a {@link AccountTypeResponse} of an account type, corresponding to the given id and returns it.
     * @param id the ID of the account type.
     * @return a {@link ResponseEntity} containing the {@link AccountTypeResponse} of the account type
     * corresponding to the given ID.
     */
    @GetMapping(value = "/account-types/{id}")
    public ResponseEntity<AccountTypeResponse> getAccountTypeById(@PathVariable Long id) {
        AccountTypeResponse accountTypeResponse = accountTypeService.getAccountTypeById(id);
        logger.info("Returns an account type with id " + id);
        return ResponseEntity.ok().body(accountTypeResponse);
    }


    /**
     * Gets a {@link List} of all {@link AccountTypeResponse}s from the database and outputs it
     * @return a {@link ResponseEntity} containing {@link List} of all {@link AccountTypeResponse}s and
     * an HTTPS status of FOUND if everything went correctly.
     */
    @GetMapping(value = "/account-types")
    public ResponseEntity<List<AccountTypeResponse>> getAllAccountTypes() {
        List<AccountTypeResponse> accountTypeResponses = accountTypeService.getAllAccountTypes();
        logger.info("Returns a list with all account types");
        return ResponseEntity.ok().body(accountTypeResponses);
    }
}
