package com.example.banksystem.services;

import com.example.banksystem.exceptions.EntityNotFoundException;
import com.example.banksystem.exceptions.NoRecordsOfEntityInTheDatabase;
import com.example.banksystem.models.entities.AccountTypeEntity;
import com.example.banksystem.models.responses.AccountTypeResponse;
import com.example.banksystem.repositories.AccountTypeRepository;
import com.example.banksystem.services.interfaces.AccountTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * An implementation class of {@link AccountTypeService} interface.
 */
@Service
public class AccountTypeServiceImpl implements AccountTypeService {

    private final AccountTypeRepository accountTypeRepository;
    private final ModelMapper modelMapper;

    public AccountTypeServiceImpl(AccountTypeRepository accountTypeRepository, ModelMapper modelMapper) {
        this.accountTypeRepository = accountTypeRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * {@inheritDoc}
     * @param type the name of the type.
     * @return an {@link AccountTypeEntity}.
     * @throws EntityNotFoundException when an AccountType with the given type is not found.
     */
    @Override
    public AccountTypeEntity findAccountTypeByType(String type) {
        return accountTypeRepository.findByType(type)
                .orElseThrow(() ->new EntityNotFoundException("AccountType"));
    }


    /**
     * {@inheritDoc}
     * @param id the id of the type.
     * @return an {@link AccountTypeEntity}.
     * @throws EntityNotFoundException when an AccountType with the given type is not found.
     */
    @Override
    public AccountTypeResponse getAccountTypeById(Long id) {
        AccountTypeEntity accountTypeEntity = accountTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AccountType"));
        return modelMapper.map(accountTypeEntity, AccountTypeResponse.class);
    }

    /**
     * {@inheritDoc}
     * @return a List of {@link AccountTypeEntity}.
     * @throws NoRecordsOfEntityInTheDatabase when there are no records of {@link AccountTypeEntity} in the database.
     */
    @Override
    public List<AccountTypeResponse> getAllAccountTypes() {
        List<AccountTypeEntity> accountTypeEntities = accountTypeRepository.findAll();
        if(accountTypeEntities.isEmpty()){
            throw new NoRecordsOfEntityInTheDatabase("Account type");
        }

        return accountTypeEntities.stream()
                .map(accountTypeEntity -> modelMapper.map(accountTypeEntity, AccountTypeResponse.class))
                .collect(Collectors.toList());
    }
}
