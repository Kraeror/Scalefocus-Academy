package com.example.banksystem.services;

import com.example.banksystem.exceptions.EntityNotFoundException;
import com.example.banksystem.exceptions.NoRecordsOfEntityInTheDatabase;
import com.example.banksystem.models.entities.*;
import com.example.banksystem.models.requsts.CalculateLoanRequest;
import com.example.banksystem.models.requsts.LoanApplyRequest;
import com.example.banksystem.models.responses.CalculateLoanResponse;
import com.example.banksystem.models.responses.CheckingAccountResponse;
import com.example.banksystem.models.responses.LoanResponse;
import com.example.banksystem.repositories.LoanRepository;
import com.example.banksystem.services.interfaces.CheckingAccountService;
import com.example.banksystem.services.interfaces.LoanCalculatorService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoanServiceImplTest {

	LoanEntity loanEntity;
	LoanResponse loanResponse;
	LoanTypeEntity loanTypeEntity;
	List<LoanEntity> loanEntityList;
	List<LoanResponse> loanResponseList;
	CheckingAccountEntity checkingAccountEntity;
	CheckingAccountResponse checkingAccountResponse;
	CalculateLoanResponse calculateLoanResponse;
	CalculateLoanRequest calculateLoanRequest;
	UserEntity userEntity;
	List<CheckingAccountEntity> checkingAccounts;
	AccountTypeEntity accountType;
	LoanApplyRequest applyRequest;
	@Mock
	LoanCalculatorService loanCalculatorService;
	@Mock
	LoanRepository loanRepository;
	@Mock
	UserService userService;

	@Mock
	CheckingAccountService checkingAccountService;

	@Mock
	TransactionService transactionService;
	@Mock
	ModelMapper modelMapper;

	@InjectMocks
	LoanServiceImpl loanService;

	@BeforeEach
	void setUp() {
		userEntity = new UserEntity();
		userEntity.setId(1L);
		userEntity.setUsername("testuser")
						.setEmail("testuser@gmail.com")
						.setHasLoan(false)
						.setFullName("Test User");

		checkingAccountEntity = new CheckingAccountEntity();
		checkingAccountEntity.setId(1L);
		checkingAccountEntity.setType(accountType);
		checkingAccountEntity.setIban("BG79BNPA94409332615387");
		checkingAccountEntity.setBalance(BigDecimal.valueOf(10000));
		checkingAccountEntity.setType(new AccountTypeEntity()
						.setType("checking").setId(1L).setMonthlyFee(BigDecimal.ONE).setTransactionFee(BigDecimal.ONE));
		checkingAccountEntity.setUser(userEntity);

		loanTypeEntity = new LoanTypeEntity()
						.setMonthlyFee(BigDecimal.ONE)
						.setConsiderationFee(BigDecimal.ONE)
						.setMonthlyFee(BigDecimal.ONE)
						.setInterestRate(BigDecimal.ONE)
						.setName("consumer")
						.setId(1L);

		loanEntity = new LoanEntity();
		loanEntity.setId(1L);
		loanEntity.setAccount(checkingAccountEntity);
		loanEntity.setCreatedOn(LocalDateTime.now());
		loanEntity.setLoanType(loanTypeEntity);
		loanEntity.setBeginningLoanAmount(BigDecimal.ONE);
		loanEntity.setRemainingLoanAmount(BigDecimal.ONE);
		loanEntity.setMaturityDate(LocalDate.now());
		loanEntity.setMonthlyPayment(BigDecimal.ONE);
		loanEntity.setTotalAmountSum(BigDecimal.ONE);
		loanEntity.setApproved(true);

		loanResponse = new LoanResponse();
		loanResponse.setId(1L);
		loanResponse.setAccount(checkingAccountResponse);
		loanResponse.setBeginningLoanAmount(BigDecimal.ONE);
		loanResponse.setRemainingLoanAmount(BigDecimal.ONE);
		loanResponse.setTotalAmountSum(BigDecimal.ONE);
		loanResponse.setLoanDueDate(LocalDate.now());

		loanEntityList = new ArrayList<>();
		loanEntityList.add(loanEntity);

		loanResponseList = new ArrayList<>();
		loanResponseList.add(loanResponse);

		calculateLoanResponse = new CalculateLoanResponse();
		calculateLoanRequest = new CalculateLoanRequest();

		accountType = new AccountTypeEntity();
		accountType.setId(1L);
		accountType.setType("checking");

		checkingAccounts = new ArrayList<>();
		checkingAccounts.add(checkingAccountEntity);

		userEntity.setCheckingAccounts(checkingAccounts);
		userEntity.addRole(new RoleEntity().setRole("USER"));
		checkingAccountResponse = new CheckingAccountResponse();
		checkingAccountEntity.setId(1L);
		checkingAccountResponse.setBalance(checkingAccountEntity.getBalance());
		checkingAccountResponse.setType("checking");
		checkingAccountResponse.setIban(checkingAccountEntity.getIban());
		checkingAccountResponse.setUserFullName(userEntity.getFullName());

		applyRequest = new LoanApplyRequest().setLoanAmount(BigDecimal.valueOf(20000))
						.setLoanType("consumer")
						.setPeriod(120)
						.setSalary(BigDecimal.valueOf(1500));
	}

	@Test
	void getLoanResponseById_whenUserIsAdmin_ok() {
		when(loanRepository.findLoanById(anyLong()))
						.thenReturn(Optional.of(loanEntity));
		when(userService.getUserEntityById(anyLong())).thenReturn(userEntity);
		when(modelMapper.map(loanEntity, LoanResponse.class)).thenReturn(loanResponse);

		assertEquals(loanResponse, loanService.getLoanResponseById(1L, 1L));
	}

	@Test
	void getLoanResponseById_whenUserIsUser_ok() {
		when(loanRepository.findLoanById(anyLong()))
				.thenReturn(Optional.of(loanEntity));
		when(userService.getUserEntityById(anyLong())).thenReturn(userEntity);
		when(modelMapper.map(loanEntity, LoanResponse.class)).thenReturn(loanResponse);

		assertEquals(loanResponse, loanService.getLoanResponseById(1L, 1L));
	}

	@Test
	void getLoanResponseById_whenUserTryToAccessOtherLoanId_throw() {
		when(loanRepository.findLoanById(1L))
				.thenReturn(Optional.empty());

		assertThrows(EntityNotFoundException.class,
						() -> loanService.getLoanResponseById(1L, 2L));
	}

	@Test
	void getAllLoan_throw() {
		when(loanRepository.findAll()).thenReturn(new ArrayList<>());
		assertThrows(NoRecordsOfEntityInTheDatabase.class,
						() -> loanService.getAllLoans());
	}

	@Test
	void getAllLoan_ok() {
		when(loanRepository.findAll()).thenReturn(loanEntityList);
		when(modelMapper.map(loanEntity, LoanResponse.class)).thenReturn(loanResponse);

		assertEquals(loanResponseList, loanService.getAllLoans());
	}

	@Test
	void calculateLoan_okay() {
		when(loanCalculatorService.calculateLoan(calculateLoanRequest)).thenReturn(calculateLoanResponse);
		assertEquals(loanService.calculateLoan(calculateLoanRequest), calculateLoanResponse);
	}

	@Test
	void getAllLoansByDueDate_okay() {
		when(loanRepository.findAllByDueDate(LocalDate.now())).thenReturn(List.of(loanEntity));
		assertEquals(loanService.getAllLoansByDueDate(LocalDate.now()), List.of(loanEntity));
	}

	@Test
	void getAllLoansByMaturityDate_okay() {
		when(loanRepository.findAllByMaturityDate(LocalDate.now())).thenReturn(List.of(loanEntity));
		assertEquals(loanService.getAllLoansByMaturityDate(LocalDate.now()), List.of(loanEntity));
	}

	@Test
	void createLoan_okay() {
		when(userService.applyForLoan(1L)).thenReturn(userEntity);
		when(loanCalculatorService.checkSalary(applyRequest)).thenReturn(true);
		when(loanCalculatorService.createLoanAppliement(applyRequest, 1L)).thenReturn(loanEntity);
		when(checkingAccountService.getAccountByTypeAndUserId("Checking", 1L))
				.thenReturn(checkingAccountResponse);
		doNothing().when(transactionService).makeLoanTransfer(
											1L, checkingAccountResponse.getIban(), applyRequest.getLoanAmount());

		when(modelMapper.map(checkingAccountService.getAccountByIban(checkingAccountEntity.getIban()),
						CheckingAccountEntity.class)).thenReturn(checkingAccountEntity);

		loanEntity.setAccount(checkingAccountEntity);

		when(loanRepository.save(loanEntity)).thenReturn(loanEntity);
		doNothing().when(userService).updateHasLoanStatus(true, userEntity);
		checkingAccountResponse.setBalance(checkingAccountEntity.getBalance());

		loanResponse.setLoanDueDate(loanEntity.getDueDate());
		loanResponse.setAccount(checkingAccountResponse);
		when(modelMapper.map(loanEntity, LoanResponse.class)).thenReturn(loanResponse);

		LoanResponse actual = loanService.createLoan(applyRequest, 1L);

		assertEquals(loanResponse, actual);
	}

	@Test
	void saveLoan() {
		when(loanRepository.save(loanEntity)).thenReturn(loanEntity);
		assertEquals(loanEntity, loanService.saveLoan(loanEntity));
	}

	@Test
	void deleteLoan() {
		doNothing().when(loanRepository).delete(loanEntity);
		loanService.deleteLoan(loanEntity);
	}
}
