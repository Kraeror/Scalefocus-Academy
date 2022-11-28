package com.example.banksystem.services;

import com.example.banksystem.exceptions.EntityNotFoundException;
import com.example.banksystem.exceptions.NoRecordsOfEntityInTheDatabase;
import com.example.banksystem.models.entities.*;
import com.example.banksystem.models.requsts.CDAccountCreationRequest;
import com.example.banksystem.models.requsts.TransactionCreationRequest;
import com.example.banksystem.models.responses.CDAccountResponse;
import com.example.banksystem.models.responses.TransactionResponse;
import com.example.banksystem.repositories.CDAccountRepository;
import com.example.banksystem.services.interfaces.AccountTypeService;
import com.example.banksystem.services.interfaces.TransactionService;
import com.example.banksystem.services.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CDAccountServiceImplTest {
    Long ID = 1L;

    LocalDate DATE = LocalDate.parse("2022-09-09");

    @Mock
    CDAccountRepository cdAccountRepository;

    @Mock
    AccountTypeService accountTypeService;

    @Mock
    UserService userService;

    @Mock
    ModelMapper modelMapper;

    @Mock
    CheckingAccountServiceImpl checkingAccountService;

    @Mock
    TransactionService transactionService;

    @InjectMocks
    CDAccountServiceImpl cdAccountService;

    CDAccountEntity cdAccount;

    AccountTypeEntity accountType;

    UserEntity user;

    CDAccountResponse cdAccountResponse;

    CDAccountCreationRequest cdAccountCreationRequest;

    @BeforeEach
    void setup()
    {
        accountType = new AccountTypeEntity();
        accountType.setType("TestType");
        accountType.setId(Long.parseLong("1"));
        accountType.setMonthlyFee(BigDecimal.valueOf(30));
        accountType.setTransactionFee(BigDecimal.valueOf(1));

        user = new UserEntity();
        user.setFullName("TestFullName");
        user.setUsername("TestUser");
        user.setId(Long.parseLong("1"));
        user.setRoles(Set.of(new RoleEntity().setRole("USER")));

        cdAccount = new CDAccountEntity();
        cdAccount.setId(1L);
        cdAccount.setInterest(BigDecimal.valueOf(2));
        cdAccount.setBalance(BigDecimal.valueOf(1000));
        cdAccount.setPeriod(12);
        cdAccount.setExpirationDate(LocalDate.now().plusYears(1));
        cdAccount.setOutcomeAmount(BigDecimal.valueOf(1020));
        cdAccount.setCreatedOn(LocalDateTime.now());
        cdAccount.setIban("DE89370400440532013054");
        cdAccount.setUser(user);
        cdAccount.setType(accountType);

        cdAccountResponse = new CDAccountResponse();
        cdAccountResponse.setId(1L);
        cdAccountResponse.setInterest(BigDecimal.valueOf(2));
        cdAccountResponse.setBalance(BigDecimal.valueOf(1000));
        cdAccountResponse.setPeriodInYears(12);
        cdAccountResponse.setOutcomeAmount(BigDecimal.valueOf(1020));
        cdAccountResponse.setIban("DE89370400440532013054");
        cdAccountResponse.setFullName("Test Name");

        cdAccountCreationRequest = new CDAccountCreationRequest()
                .setAmount(BigDecimal.valueOf(1000))
                .setPeriod(12);
    }

    @Test
    void getAllCDAccountEntities_EmptyList_Throws()
    {
        when(cdAccountRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(NoRecordsOfEntityInTheDatabase.class,
                ()-> cdAccountService.getAllCDAccountEntities());
    }

    @Test
    void getAllCDAccountEntities_Ok()
    {
        when(cdAccountRepository.findAll()).thenReturn(List.of(cdAccount));
        when(modelMapper.map(cdAccount, CDAccountResponse.class)).thenReturn(cdAccountResponse);
        assertEquals(List.of(cdAccountResponse), cdAccountService.getAllCDAccountEntities());
    }

    @Test
    void getCDAccountByIban_InvalidInput_Throws()
    {
        when(cdAccountRepository.findByIban(anyString())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                ()-> cdAccountService.getCDAccountByIban("Test"));
    }

    @Test
    void getCDAccountByIban_ValidInput_Ok()
    {
        when(cdAccountRepository.findByIban(anyString())).thenReturn(Optional.of(cdAccount));
        when(modelMapper.map(cdAccount, CDAccountResponse.class)).thenReturn(cdAccountResponse);
        assertEquals(cdAccountResponse, cdAccountService.getCDAccountByIban("DE89370400440532013054"));

    }

    @Test
    void getCDAccountEntityByID_InvalidInput_Throws() {
        when(cdAccountRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                ()-> cdAccountService.getCDAccountEntityByID(1L,2L));
    }

    @Test
    void getCDAccountById_ValidInput_Ok() {
        when(cdAccountRepository.findById(1L)).thenReturn(Optional.of(cdAccount));
        when(userService.getUserEntityById(1L)).thenReturn(user);
        when(modelMapper.map(cdAccount, CDAccountResponse.class)).thenReturn(cdAccountResponse);
        assertEquals(cdAccountResponse,cdAccountService.getCDAccountEntityByID(1L,1L));
    }

    @Test
    void createCdAccount_Successfully() {
        when(accountTypeService.findAccountTypeByType(anyString())).thenReturn(accountType);
        when(userService.getUserEntityById(anyLong())).thenReturn(user);
        when(cdAccountRepository.save(any(CDAccountEntity.class))).thenReturn(cdAccount);
        when(modelMapper.map(any(CDAccountEntity.class), eq(CDAccountResponse.class))).thenReturn(cdAccountResponse);
        assertEquals(cdAccountResponse, cdAccountService.createCdAccount(cdAccountCreationRequest, ID));
    }
    @Test
    void withdral_Ok() {
        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse = new TransactionResponse();
        transactionResponse.setId(Long.parseLong("1"));
        transactionResponse.setUuid(UUID.fromString("56d477c0-3271-11ed-a261-0242ac120002"))
                .setAmount(BigDecimal.ONE).setUserName("TestFullName")
                .setCreatedOn(LocalDateTime.now());

        TransactionCreationRequest transactionCreationRequest = new TransactionCreationRequest();
        transactionCreationRequest.setIban("DE89370400440532013000");
        transactionCreationRequest.setAmount(BigDecimal.ONE);
        transactionCreationRequest.setReason("-");

        CheckingAccountEntity checkingAccount = new CheckingAccountEntity();
        checkingAccount.setId(1L);
        checkingAccount.setType(accountType);
        checkingAccount.setIban("DE89370400440532013000");
        checkingAccount.setBalance(BigDecimal.valueOf(1000));
        checkingAccount.setUser(user);

        when(cdAccountRepository.findByIban(any())).thenReturn(Optional.of(cdAccount));
        when(transactionService.withdraw(Long.parseLong("1"),transactionCreationRequest))
                .thenReturn(transactionResponse);

        assertEquals(cdAccountService.withdraw(Long.parseLong("1"),transactionCreationRequest),
                                                                            transactionResponse.setType("withdraw"));
    }

    @Test
    void getAllCDAccountsByDate_ReturningValues() {
        when(cdAccountRepository.findAllByExpirationDate(DATE)).thenReturn(List.of(cdAccount));
        assertEquals(List.of(cdAccount), cdAccountService.getAllCDAccountsByDate(DATE));
    }

    @Test
    void getAllCDAccountsByDate_ThrowingException() {
        when(cdAccountRepository.findAllByExpirationDate(DATE)).thenReturn(Collections.emptyList())
                .thenThrow(new NoRecordsOfEntityInTheDatabase("test"));
        assertThrows(NoRecordsOfEntityInTheDatabase.class,() -> cdAccountService.getAllCDAccountsByDate(DATE));
    }

    @Test
    void getAllCDAccountResponsesByUserId_ReturningValues() {
        when(cdAccountRepository.findAllByUser_Id(anyLong())).thenReturn(List.of(cdAccount));
        when(modelMapper.map(cdAccount, CDAccountResponse.class)).thenReturn(cdAccountResponse);
        when(userService.getUserEntityById(anyLong())).thenReturn(user);
        assertEquals(List.of(cdAccountResponse), cdAccountService.getAllCDAccountResponsesByUserId(ID, ID));
    }

    @Test
    void getAllCDAccountResponsesByUserId_ReturningEmptyList() {
        when(cdAccountRepository.findAllByUser_Id(anyLong()))
                .thenReturn(Collections.emptyList())
                .thenThrow(new NoRecordsOfEntityInTheDatabase("test"));
        when(userService.getUserEntityById(anyLong())).thenReturn(user);
        assertThrows(NoRecordsOfEntityInTheDatabase.class,
                                            () -> cdAccountService.getAllCDAccountResponsesByUserId(ID, ID));
    }
}