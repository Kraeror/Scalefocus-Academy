package com.example.banksystem.services;

import com.example.banksystem.exceptions.EntityNotFoundException;
import com.example.banksystem.models.entities.AccountTypeEntity;
import com.example.banksystem.models.entities.CheckingAccountEntity;
import com.example.banksystem.models.entities.LoanEntity;
import com.example.banksystem.models.entities.LoanTypeEntity;
import com.example.banksystem.models.requsts.CalculateLoanRequest;
import com.example.banksystem.models.requsts.LoanApplyRequest;
import com.example.banksystem.models.responses.CalculateLoanResponse;
import com.example.banksystem.repositories.LoanTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoanCalculatorServiceImplTest {
	CalculateLoanRequest request;
	LoanApplyRequest applyRequest;
	CalculateLoanResponse response;
	LoanTypeEntity loanType;
	LoanEntity loanEntity;
	AccountTypeEntity accountType;
	CheckingAccountEntity checkingAccount;
	BigDecimal monthlyPayment;
	BigDecimal totalAmountSum;
	int scale;
	@Mock
	LoanTypeRepository loanTypeRepository;
	@Mock
	ModelMapper modelMapper;
	@InjectMocks
	LoanCalculatorServiceImpl loanCalculatorService;

	@BeforeEach
	void setUp() {
		loanType = new LoanTypeEntity()
						.setId(1L)
						.setConsiderationFee(BigDecimal.valueOf(250))
						.setInterestRate(BigDecimal.valueOf(5.25))
						.setMonthlyFee(BigDecimal.valueOf(5.50))
						.setName("consumer");

		request = new CalculateLoanRequest()
						.setLoanType(loanType.getName())
						.setLoanAmount(BigDecimal.valueOf(20000))
						.setPeriod(120);

		response = new CalculateLoanResponse()
						.setPeriodInMonths(request.getPeriod());

		monthlyPayment = loanCalculatorService.calculateMonthlyPayment
											(request.getLoanAmount(), loanType.getInterestRate(), request.getPeriod());
		totalAmountSum = loanCalculatorService
						.calculateTotalAmountSum(request.getLoanAmount(), monthlyPayment, request.getPeriod(),
										loanType.getMonthlyFee(), loanType.getConsiderationFee());
		accountType = new AccountTypeEntity().setType("checking")
						.setId(1L);

		checkingAccount = new CheckingAccountEntity();
		checkingAccount.setId(1L);
		checkingAccount.setBalance(BigDecimal.valueOf(1000));
		checkingAccount.setIban("BG79BNPA94409332615387");
		checkingAccount.setType(accountType);

		loanEntity = new LoanEntity();
		loanEntity.setId(1L);
		loanEntity//.setAccount(checkingAccount)
						.setLoanType(loanType)
						.setBeginningLoanAmount(BigDecimal.valueOf(20000))
						.setRemainingLoanAmount(BigDecimal.valueOf(20000))
						.setMaturityDate(LocalDate.now().plusDays(30))
						.setMonthlyPayment(monthlyPayment)
						.setTotalAmountSum(totalAmountSum)
						.setApproved(true)
						.setDueDate(LocalDate.now().plusMonths(request.getPeriod()))
						.setPeriodInMonths(request.getPeriod())
						.setCreatedOn(LocalDateTime.now());

		applyRequest = new LoanApplyRequest()
						.setLoanType("consumer")
						.setLoanAmount(BigDecimal.valueOf(20000))
						.setPeriod(120)
						.setSalary(BigDecimal.valueOf(2000));

		scale = 36;
	}

	@Test
	void calculateLoan_okay() {
		when(loanTypeRepository.findByName(anyString())).thenReturn(Optional.of(loanType));

		when(modelMapper.map(any(Object.class), any(Class.class))).thenReturn(response);

		assertEquals(response, loanCalculatorService.calculateLoan(request));
	}

	@Test
	void calculateLoan_throwEntityNotFound() {
		request.setLoanType("");
		when(loanTypeRepository.findByName(request.getLoanType())).thenReturn(Optional.empty());

		assertThrows(EntityNotFoundException.class,
						() -> loanCalculatorService.calculateLoan(request));
	}

	@Test
	void calculateTotalAmountSum_okay() {
		BigDecimal totalMonthlyPayment = loanCalculatorService
						.calculateTotalMonthlyPayment(monthlyPayment, request.getPeriod());
		BigDecimal totalMonthlyFeeCosts = loanType.getMonthlyFee().multiply(BigDecimal.valueOf(request.getPeriod()));

		BigDecimal actual = loanCalculatorService
						.calculateTotalAmountSum(request.getLoanAmount(), monthlyPayment, request.getPeriod(),
										loanType.getMonthlyFee(), loanType.getConsiderationFee());

		BigDecimal expected = totalMonthlyPayment.add(totalMonthlyFeeCosts).add(loanType.getConsiderationFee());

		assertEquals(expected, actual);
	}

	@Test
	void calculateTotalInterestPayment_okay() {
		BigDecimal totalMonthlyPayment = loanCalculatorService
						.calculateTotalMonthlyPayment(monthlyPayment, request.getPeriod());
		BigDecimal actual = loanCalculatorService
						.calculateTotalInterestPayment(request.getLoanAmount(), monthlyPayment, request.getPeriod());

		BigDecimal expected = totalMonthlyPayment.subtract(request.getLoanAmount());

		assertEquals(expected, actual);
	}

	@Test
	void calculateMonthlyPayment_okay() {
		int monthsInOneYear = 12;
		BigDecimal interestRate =
							loanType.getInterestRate().divide(new BigDecimal(100), scale, RoundingMode.HALF_UP);
		BigDecimal monthlyInterestRate =
							interestRate.divide(BigDecimal.valueOf(monthsInOneYear), scale, RoundingMode.HALF_UP);

		BigDecimal actual = loanCalculatorService
							.calculateMonthlyPayment(request.getLoanAmount(), interestRate, request.getPeriod());

		BigDecimal expected = loanCalculatorService
							.amortizedLoanFormula(request.getLoanAmount(), monthlyInterestRate, request.getPeriod());

		assertEquals(expected, actual);
	}

	@Test
	void calculateLoanAppliement_okay() {
		when(loanTypeRepository.findByName(anyString())).thenReturn(Optional.of(loanType));

		assertEquals(loanEntity.getLoanType(),
									loanCalculatorService.createLoanAppliement(applyRequest, 1L).getLoanType());
		assertEquals(loanEntity.getPeriodInMonths(),
								loanCalculatorService.createLoanAppliement(applyRequest, 1L).getPeriodInMonths());
	}

	@Test
	void checkSalary(){
		when(loanTypeRepository.findByName(anyString())).thenReturn(Optional.of(loanType));
		assertTrue(loanCalculatorService.checkSalary(applyRequest));
	}
	@Test
	void checkSalary_throw_IllegalArgumentException(){
		when(loanTypeRepository.findByName(anyString())).thenReturn(Optional.of(loanType));
		applyRequest.setSalary(BigDecimal.valueOf(100));
		assertThrows(IllegalArgumentException.class, () -> loanCalculatorService.checkSalary(applyRequest));
	}
}
