package com.example.banksystem.controllers;

import com.example.banksystem.models.responses.LoanTypeResponse;
import com.example.banksystem.services.interfaces.LoanTypeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoanTypeController {

    private final LoanTypeService loanTypeService;
    private final Logger logger = LogManager.getLogger(LoanTypeController.class);

    public LoanTypeController(LoanTypeService loanTypeService) {
        this.loanTypeService = loanTypeService;
    }

    /**
     * Gets a {@link LoanTypeResponse} of a loan type, corresponding to the given id and returns it.
     * @param id the ID of the loan type.
     * @return a {@link ResponseEntity} containing the {@link LoanTypeResponse} of the loan type
     * corresponding to the given ID.
     */
    @GetMapping("/loan-types/{id}")
    public ResponseEntity<LoanTypeResponse> getLoanTypeById(@PathVariable Long id) {
        LoanTypeResponse loanTypeResponse = loanTypeService.getLoanTypeById(id);
        logger.info("Returns the loan type with id {}", id);

        return ResponseEntity.ok().body(loanTypeResponse);
    }


    /**
     * Gets a {@link List} of all {@link LoanTypeResponse}s from the database and outputs it
     *
     * @return a {@link ResponseEntity} containing {@link List} of all {@link LoanTypeResponse}s and
     * an HTTPS status of FOUND if everything went correctly.
     */
    @GetMapping(value = "/loan-types")
    public ResponseEntity<List<LoanTypeResponse>> getAllLoanTypes() {
        List<LoanTypeResponse> loanTypeResponses = loanTypeService.getAllLoanTypes();
        logger.info("Returns a list with all loan types");

        return ResponseEntity.ok().body(loanTypeResponses);
    }
}
