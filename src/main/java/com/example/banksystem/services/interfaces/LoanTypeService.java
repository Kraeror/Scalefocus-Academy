package com.example.banksystem.services.interfaces;

import com.example.banksystem.exceptions.EntityNotFoundException;
import com.example.banksystem.exceptions.NoRecordsOfEntityInTheDatabase;
import com.example.banksystem.models.entities.LoanTypeEntity;
import com.example.banksystem.models.responses.LoanTypeResponse;
import com.example.banksystem.repositories.LoanTypeRepository;

import java.util.List;

public interface LoanTypeService {

    /**
     * Retrieves a {@link LoanTypeEntity} with the given id from {@link LoanTypeRepository}.
     * @param id is the id of the entity.
     * @return an {@link LoanTypeEntity}.
     * @throws EntityNotFoundException when an AccountType with the given id is not found.
     */
    LoanTypeResponse getLoanTypeById(Long id);

    /**
     * Retrieves all {@link LoanTypeEntity}s  from {@link LoanTypeRepository}.
     * @return a List of {@link LoanTypeEntity}.
     * @throws NoRecordsOfEntityInTheDatabase when there are no records of {@link LoanTypeEntity} in the database.
     */
    List<LoanTypeResponse> getAllLoanTypes();
}
