package com.example.banksystem.services;

import com.example.banksystem.exceptions.AccountNotBelongToUserException;
import com.example.banksystem.exceptions.IncorrectDateFilteringException;
import com.example.banksystem.exceptions.NoRecordsOfEntityInTheDatabase;
import com.example.banksystem.models.entities.AccountTypeEntity;
import com.example.banksystem.models.entities.CheckingAccountEntity;
import com.example.banksystem.models.entities.TransactionEntity;
import com.example.banksystem.models.entities.UserEntity;
import com.example.banksystem.models.requsts.TransactionCreationRequest;
import com.example.banksystem.models.responses.TransactionResponse;
import com.example.banksystem.models.responses.UserResponse;
import com.example.banksystem.repositories.TransactionRepository;
import com.example.banksystem.services.interfaces.CheckingAccountService;
import com.example.banksystem.services.interfaces.UserService;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    CheckingAccountEntity accountEntity;
    TransactionCreationRequest transactionCreationRequest;
    TransactionEntity transactionEntity;
    TransactionEntity transactionEntity2;
    TransactionResponse transactionResponse;
    TransactionResponse transactionResponse2;
    UserEntity user;
    UserResponse userResponse;
    LocalDate dateOn;
    LocalDate dateBefore;
    LocalDate dateAfter;
    String dateBeforeString;
    String dateAfterString;



    private final Long ID = 1L;
    private final UUID uuid = UUID.fromString("56d477c0-3271-11ed-a261-0242ac120002");

    private final String TYPE = "deposit";

    private final LocalDate DATE = LocalDate.parse("2022-09-22");

    private final LocalDateTime START_TIME = DATE.atTime(LocalTime.MIN);
    private final LocalDateTime END_TIME = DATE.atTime(LocalTime.MAX);

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    CheckingAccountService checkingAccountService;

    @Mock
    UserService userService;

    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    TransactionServiceImpl transactionService;

    @BeforeEach
    public void setUp() {

        user = new UserEntity();
        user.setFullName("TestFullName");
        user.setUsername("TestUser");
        user.setId(1L);

        accountEntity = new CheckingAccountEntity();
        accountEntity.setId(1L);
        accountEntity.setType(new AccountTypeEntity().setType("withdraw"));
        accountEntity.setIban("DE89370400440532013000");
        accountEntity.setBalance(BigDecimal.TEN);
        accountEntity.setUser(user);

        transactionCreationRequest = new TransactionCreationRequest();
        transactionCreationRequest.setIban("DE89370400440532013000");
        transactionCreationRequest.setAmount(BigDecimal.ONE);
        transactionCreationRequest.setReason("-");

        transactionEntity = new TransactionEntity();
        transactionEntity.setId(1L);
        transactionEntity.setUUid(uuid).setAmount(BigDecimal.ONE).setReason("-")
                .setAccountEntity(accountEntity);

        transactionEntity2 = new TransactionEntity();
        transactionEntity2.setId(3L);
        transactionEntity2.setUUid(uuid).setAmount(BigDecimal.TEN).setAccountEntity(accountEntity);

        transactionResponse = new TransactionResponse();
        transactionResponse.setId(1L);
        transactionResponse.setUuid(uuid).setAmount(BigDecimal.ONE).setUserName("TestFullName")
                .setCreatedOn(LocalDateTime.of(LocalDate.of(1950, Month.JANUARY, 26),
                                                LocalTime.of(11, 11, 11)));

        transactionResponse2 = new TransactionResponse();
        transactionResponse2.setId(3L);
        transactionResponse2.setUuid(uuid).setAmount(BigDecimal.TEN).setUserName(accountEntity.getUser().getFullName())
                .setIban(accountEntity.getIban())
                .setCreatedOn(LocalDateTime.of(2020, Month.JANUARY, 13, 5, 0));

        dateOn = LocalDate.parse("2022-09-22");
        dateBefore = LocalDate.parse("1995-01-26", DateTimeFormatter.ofPattern("yyyy-MM-dd")); //"26/01/1995"
        dateAfter = LocalDate.parse("1999-02-25", DateTimeFormatter.ofPattern("yyyy-MM-dd")); //"26/02/1999"
        userResponse = new UserResponse().setId(user.getId()).setCreatedOn(user.getCreatedOn());

        dateBeforeString = "1995-01-26";
        dateAfterString = "1999-02-25";
    }


    @Test
    void withdraw_RightParams_okay() {
        when(checkingAccountService.getAccountByUserIdAndIban(1L, "DE89370400440532013000"))
                .thenReturn(accountEntity);

        try (MockedStatic<UUID> mockedUUID = Mockito.mockStatic(UUID.class)) {

            mockedUUID.when(UUID::randomUUID).thenReturn(uuid);

            when(transactionRepository.save(transactionEntity)).thenReturn(transactionEntity.setType("withdraw"));
            when(modelMapper.map(transactionEntity, TransactionResponse.class))
                    .thenReturn(transactionResponse.setType("withdraw"));

            assertEquals(transactionService.withdraw(1L, transactionCreationRequest),
                                                                            transactionResponse.setType("withdraw"));
        }
    }

    @Test
    void deposit_RightParams_okay() {
        when(checkingAccountService.getAccountByUserIdAndIban(1L, "DE89370400440532013000"))
                .thenReturn(accountEntity);

        try (MockedStatic<UUID> mockedUUID = Mockito.mockStatic(UUID.class)) {

            mockedUUID.when(UUID::randomUUID).thenReturn(uuid);

            when(transactionRepository.save(transactionEntity)).thenReturn(transactionEntity.setType("deposit"));
            when(modelMapper.map(transactionEntity, TransactionResponse.class))
                    .thenReturn(transactionResponse.setType("deposit"));

            assertEquals(transactionService.deposit(1L, transactionCreationRequest),
                                                                            transactionResponse.setType("deposit"));
        }
    }

    @Test
    void makeTransactionEntity_okay(){
        try (MockedStatic<UUID> mockedUUID = Mockito.mockStatic(UUID.class)) {
            mockedUUID.when(UUID::randomUUID).thenReturn(uuid);
            assertEquals(transactionService.makeTransactionEntity(BigDecimal.ONE, accountEntity, "monthly fee")
                            .setUUid(uuid),
                    transactionEntity.setUUid(uuid).setType("monthly fee"));
        }
    }

    @Test
    void getAllTransactionsForUserByGivenCriteria_accountNotBelongingToUser_throws(){
        UserResponse userResponse = new UserResponse();
        UserEntity otherUser = new UserEntity();
        otherUser.setId(2L);
        when(userService.getUserEntityById(1L)).thenReturn(user);
        when(userService.getUserByAccountId(1L)).thenReturn(otherUser);
        assertThrows(AccountNotBelongToUserException.class,
                () -> transactionService.getAllTransactionsForUserByGivenCriteria(1L, 1L,
                       "tip", null,
                        null,
                       null));
    }

    @Test
    void getAllTransactionsByGivenCriteria_noRecordsFound_throws() {
        assertThrows(NoRecordsOfEntityInTheDatabase.class,
                () -> transactionService.getAllTransactionsByGivenCriteria(
                        "deposit", null,
                        null,
                        null));
    }
    @Test
    void getAllTransactionsByGivenCriteria_accountNotBelongingToUser_throws(){
        UserEntity otherUser = new UserEntity();
        otherUser.setId(2L);
        when(userService.getUserEntityById(1L)).thenReturn(user);
        when(userService.getUserByAccountId(1L)).thenReturn(otherUser);
        assertThrows(AccountNotBelongToUserException.class,
                () -> transactionService.getAllTransactionsForUserByGivenCriteria(1L, 1L,
                        "tip", null,
                        null,
                        null));
    }
    @Test
    void getAllTransactionsByGivenCriteria_dateBetweenAndTypeFiltering_okay() {
        when(transactionRepository.findAllByCreatedOnBeforeAndCreatedOnAfterAndType(dateBefore.atTime(LocalTime.MIN),
                dateAfter.atTime(LocalTime.MAX),"deposit")).thenReturn(List.of(transactionEntity));
        when(modelMapper.map(transactionEntity, TransactionResponse.class)).thenReturn(transactionResponse);
        assertEquals(transactionService.getAllTransactionsByGivenCriteria("deposit", null,
                dateBeforeString, dateAfterString), List.of(transactionResponse));
    }
    @Test
    void getAllTransactionsByGivenCriteria_dateBetweenFiltering_okay() {
        when(transactionRepository.findAllByCreatedOnBeforeAndCreatedOnAfter(dateBefore.atTime(LocalTime.MIN),
                dateAfter.atTime(LocalTime.MAX))).thenReturn(List.of(transactionEntity));
        when(modelMapper.map(transactionEntity, TransactionResponse.class)).thenReturn(transactionResponse);
        assertEquals(transactionService.getAllTransactionsByGivenCriteria(null, null,
                dateBeforeString, dateAfterString), List.of(transactionResponse));
    }
    @Test
    void getAllTransactionsByGivenCriteria_dateBeforeAndTypeFiltering_okay() {
        when(transactionRepository.findAllByCreatedOnBeforeAndType(dateBefore.atTime(LocalTime.MIN), "deposit"))
                .thenReturn(List.of(transactionEntity));
        when(modelMapper.map(transactionEntity, TransactionResponse.class)).thenReturn(transactionResponse);
        assertEquals(transactionService.getAllTransactionsByGivenCriteria("deposit", null,
                dateBeforeString, null), List.of(transactionResponse));
    }
    @Test
    void getAllTransactionsByGivenCriteria_dateAfterAndTypeFiltering_okay() {
        when(transactionRepository.findAllByCreatedOnAfterAndType(dateAfter.atTime(LocalTime.MAX), "deposit"))
                .thenReturn(List.of(transactionEntity));
        when(modelMapper.map(transactionEntity, TransactionResponse.class)).thenReturn(transactionResponse);
        assertEquals(transactionService.getAllTransactionsByGivenCriteria("deposit", null,
                null, dateAfterString), List.of(transactionResponse));
    }
    @Test
    void getAllTransactionsByGivenCriteria_dateOnAndTypeFiltering_okay() {
        when(transactionRepository.findAllByCreatedOnBetweenAndType(dateBefore.atTime(LocalTime.MIN),
                dateBefore.atTime(LocalTime.MAX), "deposit"))
                .thenReturn(List.of(transactionEntity));
        when(modelMapper.map(transactionEntity, TransactionResponse.class)).thenReturn(transactionResponse);
        assertEquals(transactionService.getAllTransactionsByGivenCriteria("deposit", dateBeforeString,
                null, null), List.of(transactionResponse));
    }
    @Test
    void getAllTransactionsByGivenCriteria_dateBeforeFiltering_okay() {
        when(transactionRepository.findAllByCreatedOnBefore(dateBefore.atTime(LocalTime.MIN)))
                .thenReturn(List.of(transactionEntity));
        when(modelMapper.map(transactionEntity, TransactionResponse.class)).thenReturn(transactionResponse);
        assertEquals(transactionService.getAllTransactionsByGivenCriteria(null, null,
                dateBeforeString, null), List.of(transactionResponse));
    }
    @Test
    void getAllTransactionsByGivenCriteria_dateAfterFiltering_okay() {
        when(transactionRepository.findAllByCreatedOnAfter(dateAfter.atTime(LocalTime.MAX)))
                .thenReturn(List.of(transactionEntity));
        when(modelMapper.map(transactionEntity, TransactionResponse.class)).thenReturn(transactionResponse);
        assertEquals(transactionService.getAllTransactionsByGivenCriteria(null, null,
                null, dateAfterString), List.of(transactionResponse));
    }
    @Test
    void getAllTransactionsByGivenCriteria_dateOnFiltering_okay() {
        when(transactionRepository.findAllByCreatedOnBetween(dateBefore.atTime(LocalTime.MIN),
                dateBefore.atTime(LocalTime.MAX)))
                .thenReturn(List.of(transactionEntity));
        when(modelMapper.map(transactionEntity, TransactionResponse.class)).thenReturn(transactionResponse);
        assertEquals(transactionService.getAllTransactionsByGivenCriteria(null, dateBeforeString,
                null, null), List.of(transactionResponse));
    }
    @Test
    void getAllTransactionsByGivenCriteria_typeFiltering_okay() {
        when(transactionRepository.findAllByType("deposit"))
                .thenReturn(List.of(transactionEntity));
        when(modelMapper.map(transactionEntity, TransactionResponse.class)).thenReturn(transactionResponse);
        assertEquals(transactionService.getAllTransactionsByGivenCriteria("deposit", null,
                null, null), List.of(transactionResponse));
    }
    @Test
    void getAllTransactionsByGivenCriteria_allTransactions_okay() {
        when(transactionRepository.findAll())
                .thenReturn(List.of(transactionEntity));
        when(modelMapper.map(transactionEntity, TransactionResponse.class)).thenReturn(transactionResponse);
        assertEquals(transactionService.getAllTransactionsByGivenCriteria(null, null,
                null, null), List.of(transactionResponse));
    }
    @Test
    void getAllTransactionsByGivenCriteria_filterByDate_throws() {
        assertThrows(IllegalArgumentException.class, () -> transactionService
                .getAllTransactionsByGivenCriteria(null, dateBeforeString,
                                                    "27/01/1995", dateAfterString));
    }

    @Test
    void getAllTransactionsForUserByGivenCriteria_dateBetweenAndTypeFiltering_okay() {
        when(userService.getUserEntityById(1L)).thenReturn(user);
        when(userService.getUserByAccountId(1L)).thenReturn(user);
        when(transactionRepository.findAllByCreatedOnBeforeAndCreatedOnAfterAndTypeAndAccountEntity_id
                (dateBefore.atTime(LocalTime.MIN), dateAfter.atTime(LocalTime.MAX),"deposit", 1L))
                .thenReturn(List.of(transactionEntity));
        when(modelMapper.map(transactionEntity, TransactionResponse.class)).thenReturn(transactionResponse);
        assertEquals(transactionService.getAllTransactionsForUserByGivenCriteria(1L, 1L,"deposit",
                null, dateBeforeString, dateAfterString), List.of(transactionResponse));
    }
    @Test
    void getAllTransactionsForUserByGivenCriteria_dateBetweenFiltering_okay() {
        when(userService.getUserEntityById(1L)).thenReturn(user);
        when(userService.getUserByAccountId(1L)).thenReturn(user);
        when(transactionRepository.findAllByCreatedOnBeforeAndCreatedOnAfterAndAccountEntity_id
                (dateBefore.atTime(LocalTime.MIN), dateAfter.atTime(LocalTime.MAX),
                        1L)).thenReturn(List.of(transactionEntity));
        when(modelMapper.map(transactionEntity, TransactionResponse.class)).thenReturn(transactionResponse);
        assertEquals(transactionService.getAllTransactionsForUserByGivenCriteria(1L, 1L,null,
                null, dateBeforeString, dateAfterString), List.of(transactionResponse));
    }
    @Test
    void getAllTransactionsForUserByGivenCriteria_dateBeforeAndTypeFiltering_okay() {
        when(userService.getUserEntityById(1L)).thenReturn(user);
        when(userService.getUserByAccountId(1L)).thenReturn(user);
        when(transactionRepository.findAllByCreatedOnBeforeAndTypeAndAccountEntity_id( dateBefore.atTime(LocalTime.MIN),
                "deposit", 1L)).thenReturn(List.of(transactionEntity));
        when(modelMapper.map(transactionEntity, TransactionResponse.class)).thenReturn(transactionResponse);
        assertEquals(transactionService.getAllTransactionsForUserByGivenCriteria(1L, 1L,"deposit",
                null, dateBeforeString, null), List.of(transactionResponse));
    }
    @Test
    void getAllTransactionsForUserByGivenCriteria_dateAfterAndTypeFiltering_okay() {
        when(userService.getUserEntityById(1L)).thenReturn(user);
        when(userService.getUserByAccountId(1L)).thenReturn(user);
        when(transactionRepository.findAllByCreatedOnAfterAndTypeAndAccountEntity_id(dateAfter.atTime(LocalTime.MAX),
                "deposit", 1L)).thenReturn(List.of(transactionEntity));
        when(modelMapper.map(transactionEntity, TransactionResponse.class)).thenReturn(transactionResponse);
        assertEquals(transactionService.getAllTransactionsForUserByGivenCriteria(1L, 1L,"deposit",
                null, null, dateAfterString), List.of(transactionResponse));
    }
    @Test
    void getAllTransactionsForUserByGivenCriteria_dateOnAndTypeFiltering_okay() {
        when(userService.getUserEntityById(1L)).thenReturn(user);
        when(userService.getUserByAccountId(1L)).thenReturn(user);
        when(transactionRepository.findAllByCreatedOnBetweenAndTypeAndAccountEntity_id(dateBefore.atTime(LocalTime.MIN),
                dateBefore.atTime(LocalTime.MAX), "deposit", 1L))
                .thenReturn(List.of(transactionEntity));
        when(modelMapper.map(transactionEntity, TransactionResponse.class)).thenReturn(transactionResponse);
        assertEquals(transactionService.getAllTransactionsForUserByGivenCriteria(1L, 1L,"deposit",
                dateBeforeString, null, null), List.of(transactionResponse));
    }
    @Test
    void getAllTransactionsForUserByGivenCriteria_dateBeforeFiltering_okay() {
        when(userService.getUserEntityById(1L)).thenReturn(user);
        when(userService.getUserByAccountId(1L)).thenReturn(user);
        when(transactionRepository.findAllByCreatedOnBeforeAndAccountEntity_id(dateBefore.atTime(LocalTime.MIN),
                1L)).thenReturn(List.of(transactionEntity));
        when(modelMapper.map(transactionEntity, TransactionResponse.class)).thenReturn(transactionResponse);
        assertEquals(transactionService.getAllTransactionsForUserByGivenCriteria(1L, 1L, null,
                null, dateBeforeString, null), List.of(transactionResponse));
    }
    @Test
    void getAllTransactionsForUserByGivenCriteria_dateAfterFiltering_okay() {
        when(userService.getUserEntityById(1L)).thenReturn(user);
        when(userService.getUserByAccountId(1L)).thenReturn(user);
        when(transactionRepository.findAllByCreatedOnAfterAndAccountEntity_id(dateAfter.atTime(LocalTime.MAX),
                1L)).thenReturn(List.of(transactionEntity));
        when(modelMapper.map(transactionEntity, TransactionResponse.class)).thenReturn(transactionResponse);
        assertEquals(transactionService.getAllTransactionsForUserByGivenCriteria(1L, 1L, null,
                null, null, dateAfterString), List.of(transactionResponse));
    }
    @Test
    void getAllTransactionsForUserByGivenCriteria_dateOnFiltering_okay() {
        when(userService.getUserEntityById(1L)).thenReturn(user);
        when(userService.getUserByAccountId(1L)).thenReturn(user);
        when(transactionRepository.findAllByCreatedOnBetweenAndAccountEntity_id(dateBefore.atTime(LocalTime.MIN),
                dateBefore.atTime(LocalTime.MAX), 1L)).thenReturn(List.of(transactionEntity));
        when(modelMapper.map(transactionEntity, TransactionResponse.class)).thenReturn(transactionResponse);
        assertEquals(transactionService.getAllTransactionsForUserByGivenCriteria(1L, 1L,null,
                dateBeforeString, null, null), List.of(transactionResponse));
    }
    @Test
    void getAllTransactionsForUserByGivenCriteria_typeFiltering_okay() {
        when(userService.getUserEntityById(1L)).thenReturn(user);
        when(userService.getUserByAccountId(1L)).thenReturn(user);
        when(transactionRepository.findAllByTypeAndAccountEntity_id("deposit", 1L))
                .thenReturn(List.of(transactionEntity));
        when(modelMapper.map(transactionEntity, TransactionResponse.class)).thenReturn(transactionResponse);
        assertEquals(transactionService.getAllTransactionsForUserByGivenCriteria(1L, 1L,"deposit",
                null, null, null), List.of(transactionResponse));
    }
    @Test
    void getAllTransactionsForUserByGivenCriteria_allTransactions_okay() {
        when(userService.getUserEntityById(1L)).thenReturn(user);
        when(userService.getUserByAccountId(1L)).thenReturn(user);
        when(transactionRepository.findAllByAccountEntity_id(1L))
                .thenReturn(List.of(transactionEntity));
        when(modelMapper.map(transactionEntity, TransactionResponse.class)).thenReturn(transactionResponse);
        assertEquals(transactionService.getAllTransactionsForUserByGivenCriteria(1L, 1L,null,
                null, null, null), List.of(transactionResponse));
    }
    @Test
    void getAllTransactionsForUserByGivenCriteria_filterByDate_throws() {
        when(userService.getUserEntityById(1L)).thenReturn(user);
        when(userService.getUserByAccountId(1L)).thenReturn(user);
        assertThrows(IncorrectDateFilteringException.class, () -> transactionService
                .getAllTransactionsForUserByGivenCriteria(1L, 1L,null,
                        dateBeforeString, "1995-01-27", dateAfterString));
    }

    @Test
    void dateParser_nullString_okay(){
        assertNull(transactionService.dateParser(null));
    }

    @Test
    void dateParser_badFormat_okay(){
        assertThrows(IllegalArgumentException.class,
                () -> transactionService.dateParser("12/12/2012"));
    }

    @Test
    void dateParser_okay(){
        assertEquals(transactionService.dateParser("1995-01-26"),dateBefore);
    }

}
