package com.example.banksystem.services;

import com.example.banksystem.exceptions.*;
import com.example.banksystem.models.entities.AccountTypeEntity;
import com.example.banksystem.models.entities.CheckingAccountEntity;
import com.example.banksystem.models.entities.RoleEntity;
import com.example.banksystem.models.entities.UserEntity;
import com.example.banksystem.models.responses.CheckingAccountResponse;
import com.example.banksystem.repositories.CheckingAccountRepository;
import com.example.banksystem.services.interfaces.AccountTypeService;
import com.example.banksystem.services.interfaces.UserService;
import org.iban4j.Iban;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckingAccountServiceImplTest {

    @Mock
    CheckingAccountRepository checkingAccountRepository;
    @Mock
    UserService userService;
    @Mock
    AccountTypeService accountTypeService;
    @Mock
    ModelMapper modelMapper;
    @InjectMocks
    CheckingAccountServiceImpl checkingAccountService;

    UserEntity user;
    AccountTypeEntity accountType;
    CheckingAccountEntity checkingAccountEntity;
    CheckingAccountResponse checkingAccountResponse;
    private final Iban defaultIban = Iban.valueOf("DE89370400440532013000");

    private final Long ID = 1L;
    private final String TEST_TYPE = "TestType";

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setFullName("TestFullName");
        user.setUsername("TestUser");
        user.setId(Long.parseLong("1"));
        user.setRoles(Set.of(new RoleEntity().setRole("USER")));

        accountType = new AccountTypeEntity();
        accountType.setType("TestType");
        accountType.setId(Long.parseLong("1"));
        accountType.setMonthlyFee(BigDecimal.valueOf(30));
        accountType.setTransactionFee(BigDecimal.valueOf(1));

        checkingAccountEntity = new CheckingAccountEntity();
        checkingAccountEntity.setId(1L);
        checkingAccountEntity.setType(accountType);
        checkingAccountEntity.setIban("DE89370400440532013000");
        checkingAccountEntity.setBalance(BigDecimal.ZERO);
        checkingAccountEntity.setUser(user);

        checkingAccountResponse = new CheckingAccountResponse();
        checkingAccountResponse.setId(1L);
        checkingAccountResponse.setUserFullName("TestFullName");
        checkingAccountResponse.setType("TestType");
        checkingAccountResponse.setIban("DE89370400440532013000");
        checkingAccountResponse.setBalance(BigDecimal.ZERO);
    }

    @Test
    void createAccount_ValidInput_Ok() {
        when(accountTypeService.findAccountTypeByType(anyString())).thenReturn(accountType);
        when(userService.getUserEntityById(anyLong())).thenReturn(user);

        try (MockedStatic<Iban> mockedIban = Mockito.mockStatic(Iban.class)) {

            mockedIban.when(Iban::random).thenReturn(defaultIban);
            when(checkingAccountRepository.save(checkingAccountEntity)).thenReturn(checkingAccountEntity);
            when(modelMapper.map(checkingAccountEntity, CheckingAccountResponse.class)).thenReturn(checkingAccountResponse);

            assertEquals(checkingAccountService.createAccount(1L),
                    checkingAccountResponse);
        }
    }

    @Test
    void getAccountByIban_noSuckIban_Throw() {
        when(checkingAccountRepository.findByIban(anyString())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> checkingAccountService.getAccountByIban(anyString()));
    }

    @Test
    void getAccountByIban_noSuchIban_Okay() {
        when(checkingAccountRepository.findByIban(anyString())).thenReturn(Optional.of(checkingAccountEntity));
        assertEquals(checkingAccountService.getAccountByIban(anyString()), checkingAccountEntity);
    }

    @Test
    void getAccountByUserIdAndIban_noSuchIban_Throw() {
        when(checkingAccountRepository.findByUserIdAndIban(anyLong(), anyString())).thenReturn(Optional.empty());
        assertThrows(AccountNotBelongToUserException.class,
                () -> checkingAccountService.getAccountByUserIdAndIban(anyLong(), anyString()));
    }

    @Test
    void getAccountByUserIdAndIban_noSuchIban_Okay() {
        when(checkingAccountRepository.findByUserIdAndIban(anyLong(), anyString()))
                .thenReturn(Optional.of(checkingAccountEntity));
        assertEquals(checkingAccountService.getAccountByUserIdAndIban(anyLong(), anyString()), checkingAccountEntity);
    }

    @Test
    void withdraw_NotEnoughMoney_throw() {
        when(accountTypeService.findAccountTypeByType(anyString())).thenReturn(accountType);
        assertThrows(InsufficientFundsException.class,
                () -> checkingAccountService.withdraw(checkingAccountEntity, BigDecimal.valueOf(20)));
    }

    @Test
    void withdraw_EnoughMoney_okay() {
        checkingAccountEntity.setBalance(BigDecimal.TEN);
        when(accountTypeService.findAccountTypeByType(anyString())).thenReturn(accountType);
        checkingAccountService.withdraw(checkingAccountEntity, BigDecimal.valueOf(2));
        assertEquals(checkingAccountEntity.getBalance(), BigDecimal.valueOf(7));
    }

    @Test
    void deposit_EnoughMoney_okay() {
        checkingAccountEntity.setBalance(BigDecimal.TEN);

        checkingAccountService.deposit(checkingAccountEntity, BigDecimal.valueOf(2));
        assertEquals(checkingAccountEntity.getBalance(), BigDecimal.valueOf(12));
    }

    @Test
    void getAllAccounts_EmptyList_throw() {
        when(checkingAccountRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(NoRecordsOfEntityInTheDatabase.class,
                () -> checkingAccountService.getAllAccounts());
    }

    @Test
    void getAllAccounts_ListOfOne_okay() {
        when(checkingAccountRepository.findAll()).thenReturn(List.of(checkingAccountEntity, checkingAccountEntity));
        assertEquals(checkingAccountService.getAllAccounts().size(), 2);
    }

    @Test
    void saveAccount_okay() {
        when(checkingAccountRepository.save(checkingAccountEntity)).thenReturn(checkingAccountEntity);
        assertEquals(checkingAccountService.saveAccount(checkingAccountEntity), checkingAccountEntity);
    }

    @Test
    void getCheckingAccountById_noSuchEntityWithThisId_Throw() {
        when(checkingAccountRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> checkingAccountService.getCheckingAccountById(1L, 1L));
    }

    @Test
    void getCheckingAccountById_AccountNotBelonginhg_Throw() {
        UserEntity user = new UserEntity();
        user.setId(2L);
        user.setRoles(Set.of(new RoleEntity().setRole("USER")));
        checkingAccountEntity.setUser(user);
        when(checkingAccountRepository.findById(1L)).thenReturn(Optional.of(checkingAccountEntity));
        when(userService.getUserEntityById(1L)).thenReturn(user);
        assertThrows(ResourceNotBelongingToUser.class,
                () -> checkingAccountService.getCheckingAccountById(1L, 1L));
    }

    @Test
    void getCheckingAccountById_okay() {
        when(checkingAccountRepository.findById(1L)).thenReturn(Optional.of(checkingAccountEntity));
        when(userService.getUserEntityById(1L)).thenReturn(user);
        when(modelMapper.map(checkingAccountEntity, CheckingAccountResponse.class)).thenReturn(checkingAccountResponse);
        assertEquals(checkingAccountService.getCheckingAccountById(1L, 1L), checkingAccountResponse);
    }


    @Test
    void getAccountByTypeAndUserId_Found() {
        when(accountTypeService.findAccountTypeByType(anyString())).thenReturn(accountType);
        when(checkingAccountRepository.findByTypeAndUserId(any(AccountTypeEntity.class), anyLong()))
                .thenReturn(List.of(checkingAccountEntity));
        when(modelMapper.map(any(Object.class), any(Class.class))).thenReturn(checkingAccountResponse);
        assertEquals(checkingAccountResponse, checkingAccountService.getAccountByTypeAndUserId(TEST_TYPE, ID));
    }

    @Test
    void getAllAccountResponses_Successfully() {
        when(checkingAccountRepository.findAll()).thenReturn(List.of(checkingAccountEntity));
        when(modelMapper.map(checkingAccountEntity, CheckingAccountResponse.class)).thenReturn(checkingAccountResponse);
        assertEquals(List.of(checkingAccountResponse), checkingAccountService.getAllAccountResponses());
    }

    @Test
    void getAccountResponseByIban_okay() {
        when(checkingAccountRepository.findByIban("DE89370400440532013000"))
                .thenReturn(Optional.of(checkingAccountEntity));
        when(modelMapper.map(checkingAccountEntity, CheckingAccountResponse.class))
                .thenReturn(checkingAccountResponse);
        assertEquals(checkingAccountService.getAccountResponseByIban("DE89370400440532013000"), checkingAccountResponse);
    }

    @Test
    void getAccountById_noSuchAccount_throws() {
        when(checkingAccountRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> checkingAccountService.getAccountById(anyLong()));
    }

    @Test
    void getAccountById_okay() {
        when(checkingAccountRepository.findById(anyLong())).thenReturn(Optional.of(checkingAccountEntity));
        when(modelMapper.map(checkingAccountEntity, CheckingAccountResponse.class))
                .thenReturn(checkingAccountResponse);
        assertEquals(checkingAccountService.getAccountById(anyLong()),checkingAccountResponse);
    }

    @Test
    void getAllAccountResponsesByUserId(){
        when(userService.getUserEntityById(anyLong())).thenReturn(user);
        when(checkingAccountRepository.findByUserId(1L)).thenReturn(new ArrayList<>());
        assertThrows(NoRecordsOfEntityInTheDatabase.class,
                () -> checkingAccountService.getAllAccountResponsesByUserId(1L,1L));
    }
}