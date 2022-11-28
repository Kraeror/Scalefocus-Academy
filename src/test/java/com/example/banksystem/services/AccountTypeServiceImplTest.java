package com.example.banksystem.services;

import com.example.banksystem.exceptions.EntityNotFoundException;
import com.example.banksystem.exceptions.NoRecordsOfEntityInTheDatabase;
import com.example.banksystem.models.entities.AccountTypeEntity;
import com.example.banksystem.models.responses.AccountTypeResponse;
import com.example.banksystem.repositories.AccountTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountTypeServiceImplTest {

    AccountTypeEntity accountTypeEntity;
    AccountTypeResponse accountTypeResponse;

    @Mock
    AccountTypeRepository accountTypeRepository;

    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    AccountTypeServiceImpl accountTypeService;

    @BeforeEach
    void setUp() {
        accountTypeEntity = new AccountTypeEntity();
        accountTypeEntity.setType("TestType");
        accountTypeEntity.setId(Long.parseLong("1")).setMonthlyFee(BigDecimal.ONE).setTransactionFee(BigDecimal.ONE);


        accountTypeResponse = new AccountTypeResponse();
        accountTypeResponse.setType("TestType");
        accountTypeResponse
                        .setId(Long.parseLong("1")).setMonthlyFee(BigDecimal.ONE).setTransactionFee(BigDecimal.ONE);

    }


    @Test
    void findAccountTypeByType_noSuchType_Throws() {
        when(accountTypeRepository.findByType(anyString())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> accountTypeService.findAccountTypeByType(anyString()));

    }

    @Test
    void findAccountTypeByType_okay() {
        when(accountTypeRepository.findByType(anyString())).thenReturn(Optional.of(accountTypeEntity));
        assertEquals(accountTypeService.findAccountTypeByType(anyString()), accountTypeEntity);

    }

    @Test
    void getAccountTypeById_wrongId_throw() {
        when(accountTypeRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> accountTypeService.getAccountTypeById(anyLong()));
    }

    @Test
    void findAccountTypeById_okay() {
        when(accountTypeRepository.findById(anyLong())).thenReturn(Optional.of(accountTypeEntity));
        when(modelMapper.map(accountTypeEntity, AccountTypeResponse.class)).thenReturn(accountTypeResponse);
        assertEquals(accountTypeService.getAccountTypeById(anyLong()), accountTypeResponse);

    }

    @Test
    void getAllAccountTypes_emptyList_throw() {
        when(accountTypeRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(NoRecordsOfEntityInTheDatabase.class,
                () -> accountTypeService.getAllAccountTypes());
    }

    @Test
    void getAllAccountTypes() {
        when(accountTypeRepository.findAll()).thenReturn(List.of(accountTypeEntity));
        when(modelMapper.map(accountTypeEntity, AccountTypeResponse.class)).thenReturn(accountTypeResponse);

        assertEquals(accountTypeService.getAllAccountTypes().size(), 1);
        assertEquals(accountTypeService.getAllAccountTypes().get(0), accountTypeResponse);
    }
}