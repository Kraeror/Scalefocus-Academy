package com.example.banksystem.services;

import com.example.banksystem.exceptions.EntityNotFoundException;
import com.example.banksystem.exceptions.NoRecordsOfEntityInTheDatabase;
import com.example.banksystem.models.entities.LoanTypeEntity;
import com.example.banksystem.models.responses.LoanTypeResponse;
import com.example.banksystem.repositories.LoanTypeRepository;
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
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class LoanTypeServiceImplTest {
    LoanTypeEntity loanTypeEntity;
    LoanTypeResponse loanTypeResponse;

    @Mock
    LoanTypeRepository loanTypeRepository;
    @Mock
    ModelMapper modelMapper;
    @InjectMocks
    LoanTypeServiceImpl loanTypeService;

    @BeforeEach
    void setUp() {
        loanTypeEntity = new LoanTypeEntity().setId(1L).setConsiderationFee(BigDecimal.ONE)
                .setMonthlyFee(BigDecimal.ONE)
                .setName("credit");
        loanTypeResponse  = new LoanTypeResponse().setId(1L).setConsiderationFee(BigDecimal.ONE)
                .setMonthlyFee(BigDecimal.ONE)
                .setName("credit");

    }

    @Test
    void getLoanTypeById_throw() {
        when(loanTypeRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> loanTypeService.getLoanTypeById(anyLong()));
    }

    @Test
    void getLoanTypeById_okay() {
        when(loanTypeRepository.findById(anyLong())).thenReturn(Optional.of(loanTypeEntity));
        when(modelMapper.map(loanTypeEntity,LoanTypeResponse.class)).thenReturn(loanTypeResponse);

        assertEquals(loanTypeResponse,loanTypeService.getLoanTypeById(anyLong()));
    }

    @Test
    void getAllLoanTypes_throws() {
        when(loanTypeRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(NoRecordsOfEntityInTheDatabase.class,
                () -> loanTypeService.getAllLoanTypes());
    }

    @Test
    void getAllLoanTypes_okay() {
        when(loanTypeRepository.findAll()).thenReturn(List.of(loanTypeEntity));
        when(modelMapper.map(loanTypeEntity,LoanTypeResponse.class)).thenReturn(loanTypeResponse);

        assertEquals(List.of(loanTypeResponse),loanTypeService.getAllLoanTypes());
    }
}