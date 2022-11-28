package com.example.banksystem.services.interfaces;

import com.example.banksystem.exceptions.EntityNotFoundException;
import com.example.banksystem.exceptions.NoRecordsOfEntityInTheDatabase;
import com.example.banksystem.models.entities.AccountTypeEntity;
import com.example.banksystem.models.responses.AccountTypeResponse;
import com.example.banksystem.repositories.AccountTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * A Service interface for {@link AccountTypeEntity}.
 * <p>Has methods for retrieval from
 * the database repository.
 */
@Service
public interface AccountTypeService {
    /**
     * Retrieves a {@link AccountTypeEntity} with the given type from {@link AccountTypeRepository}.
     * @param type the name of the type.
     * @return an {@link AccountTypeEntity}.
     * @throws EntityNotFoundException when an AccountType with the given type is not found.
     */
    AccountTypeEntity findAccountTypeByType(String type);

    /**
     * Retrieves a {@link AccountTypeEntity} with the given id from {@link AccountTypeRepository}.
     * @param id is the id of the entity.
     * @return an {@link AccountTypeEntity}.
     * @throws EntityNotFoundException when an AccountType with the given id is not found.
     */
    AccountTypeResponse getAccountTypeById(Long id);

    /**
     * Retrieves all {@link AccountTypeEntity}s  from {@link AccountTypeRepository}.
     * @return a List of {@link AccountTypeEntity}.
     * @throws NoRecordsOfEntityInTheDatabase when there are no records of {@link AccountTypeEntity} in the database.
     */
    List<AccountTypeResponse> getAllAccountTypes();
}
