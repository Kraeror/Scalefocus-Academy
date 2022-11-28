package com.example.banksystem.controllers;

import com.example.banksystem.models.requsts.CalculateLoanRequest;
import com.example.banksystem.models.requsts.LoanApplyRequest;
import com.example.banksystem.models.responses.CalculateLoanResponse;
import com.example.banksystem.models.responses.LoanResponse;
import com.example.banksystem.models.user.UserAuthenticationDetails;
import com.example.banksystem.services.LoanServiceImpl;
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
public class LoanController {

    private final LoanServiceImpl loanService;
    private final Logger logger = LogManager.getLogger(LoanController.class);

    public LoanController(LoanServiceImpl loanService) {
        this.loanService = loanService;
    }

    /**
     * Creates an {@link CalculateLoanResponse} with data from a given entity and returns it for the user to only see.
     *
     * @param calculateLoanRequest an entity in which only shows the user the calculations for the loan parameters
     *                             he has given .
     * @return an {@link CalculateLoanResponse} with the calculations of the  loan by the given parameters.
     */
    @PostMapping("/loans/calculator")
    public ResponseEntity<CalculateLoanResponse> calculationLoan(
            @Valid @RequestBody CalculateLoanRequest calculateLoanRequest, Errors errors) {
        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.getFieldErrors().get(0).getDefaultMessage());
        }

        CalculateLoanResponse calculateLoanResponse = loanService.calculateLoan(calculateLoanRequest);
        logger.info("Calculate request was answered with calculate response for loan type {}."
                , calculateLoanResponse.getLoanType());
        return ResponseEntity.ok().body(calculateLoanResponse);
    }

    /**
     * Creates an {@link LoanResponse} with data from a given entity and returns the loan that was created.
     *
     * @param loanApplyRequest an entity that takes the info by the user and creates the loan he is applying to.
     * @param userDetails      an entity with which we take the ID of the user to whom the loan will be assigned.
     * @return an {@link LoanResponse} with the calculations of the created loan by the given parameters.
     */
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/loans/approval")
    public ResponseEntity<LoanResponse> applyingForLoan(
            @AuthenticationPrincipal UserAuthenticationDetails userDetails,
            @Valid @RequestBody LoanApplyRequest loanApplyRequest, Errors errors) {
        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.getFieldErrors().get(0).getDefaultMessage());
        }

        LoanResponse loanResponse = loanService.createLoan(loanApplyRequest, userDetails.getId());
        logger.info("Loan response was given after a request.");
        return ResponseEntity.created(URI.create("/loans/" + loanResponse.getId())).body(loanResponse);
    }

    /**
     * Gets a {@link LoanResponse} of a loan, corresponding to the given id and returns it.
     *
     * @param id the ID of the loan type.
     * @return a {@link ResponseEntity} containing the {@link LoanResponse} of the loan
     * corresponding to the given ID.
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(value = "/loans/{id}")
    public ResponseEntity<LoanResponse> getLoanById(@PathVariable Long id,
                                                    @AuthenticationPrincipal UserAuthenticationDetails userDetails) {
        LoanResponse loanResponse = loanService.getLoanResponseById(id, userDetails.getId());
        logger.info("Returns a loan with id: {}", id);
        return ResponseEntity.ok().body(loanResponse);
    }

    /**
     * Gets a {@link List} of all {@link LoanResponse}s from the database and outputs it
     *
     * @return a {@link ResponseEntity} containing {@link List} of all {@link LoanResponse}s and
     * an HTTPS status of FOUND if everything went correctly.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/loans")
    public ResponseEntity<List<LoanResponse>> getAllLoans() {
        List<LoanResponse> loans = loanService.getAllLoans();
        logger.info("Returns a list with all loans");
        return ResponseEntity.ok().body(loans);
    }
}
