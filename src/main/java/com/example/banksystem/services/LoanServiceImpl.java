package com.example.banksystem.services;

import com.example.banksystem.exceptions.EntityNotFoundException;
import com.example.banksystem.exceptions.NoRecordsOfEntityInTheDatabase;
import com.example.banksystem.exceptions.ResourceNotBelongingToUser;
import com.example.banksystem.models.entities.CheckingAccountEntity;
import com.example.banksystem.models.entities.LoanEntity;
import com.example.banksystem.models.entities.UserEntity;
import com.example.banksystem.models.requsts.CalculateLoanRequest;
import com.example.banksystem.models.requsts.LoanApplyRequest;
import com.example.banksystem.models.responses.CalculateLoanResponse;
import com.example.banksystem.models.responses.CheckingAccountResponse;
import com.example.banksystem.models.responses.LoanResponse;
import com.example.banksystem.repositories.LoanRepository;
import com.example.banksystem.services.interfaces.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanServiceImpl implements LoanService {

	private final ModelMapper modelMapper;
	private final LoanRepository loanRepository;
	private final LoanCalculatorService loanCalculatorService;
	private final CheckingAccountService checkingAccountService;
	private final TransactionService transactionService;
	private final UserService userService;

	public LoanServiceImpl(ModelMapper modelMapper, LoanRepository loanRepository,
	                       LoanCalculatorService loanCalculatorService,
	                       CheckingAccountService checkingAccountService,
	                       TransactionService transactionService, UserService userService) {
		this.modelMapper = modelMapper;
		this.loanRepository = loanRepository;
		this.loanCalculatorService = loanCalculatorService;
		this.checkingAccountService = checkingAccountService;
		this.transactionService = transactionService;
		this.userService = userService;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<LoanResponse> getAllLoans() {
		List<LoanEntity> loanEntities = loanRepository.findAll();
		if (loanEntities.isEmpty()) {
			throw new NoRecordsOfEntityInTheDatabase("Loan");
		}
		return loanEntities.stream().map(type -> modelMapper.map(type, LoanResponse.class))
						.collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LoanResponse getLoanResponseById(Long loanId, Long userId) {
		LoanEntity loanEntity = loanRepository.findLoanById(loanId)
						.orElseThrow(() -> new EntityNotFoundException("Loan"));

		boolean admin = userService.getUserEntityById(userId).getRoles().stream()
						.anyMatch(r -> r.getRole().equals("ADMIN"));

		if(!admin && loanEntity.getAccount().getUser().getId() != userId){
			throw new ResourceNotBelongingToUser("Loan");
		}

		return modelMapper.map(loanEntity, LoanResponse.class);
	}

	@Override
	public LoanResponse createLoan(LoanApplyRequest applyRequest, Long userId) {
		UserEntity userEntity = userService.applyForLoan(userId);
		loanCalculatorService.checkSalary(applyRequest);

		LoanEntity loanEntity = loanCalculatorService.createLoanAppliement(applyRequest, userId);

		CheckingAccountResponse accountResponse = checkingAccountService
						.getAccountByTypeAndUserId("Checking", userId)
						.setUserFullName(userEntity.getFullName());

		transactionService.makeLoanTransfer(userId, accountResponse.getIban(), applyRequest.getLoanAmount());

		CheckingAccountEntity accountEntity = modelMapper.map(checkingAccountService
						.getAccountByIban(accountResponse.getIban()), CheckingAccountEntity.class);
		loanEntity.setAccount(accountEntity);

		LoanEntity created = loanRepository.save(loanEntity);
		userService.updateHasLoanStatus(true, userEntity);

		accountResponse.setBalance(accountEntity.getBalance());
		LoanResponse loanResponse = modelMapper.map(created, LoanResponse.class)
						.setLoanDueDate(created.getDueDate());
		loanResponse.setAccount(accountResponse);

		return loanResponse;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CalculateLoanResponse calculateLoan(CalculateLoanRequest calculateLoanRequest) {
		return loanCalculatorService.calculateLoan(calculateLoanRequest);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param dueDate is the {@link LoanEntity}s with this due date we are looking for.
	 * @return a {@link List<LoanResponse>} that match that due date.
	 */
	@Override
	public List<LoanEntity> getAllLoansByDueDate(LocalDate dueDate) {
		return loanRepository.findAllByDueDate(dueDate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteLoan(LoanEntity loan) {
		loanRepository.delete(loan);
	}

	/**
	 * {@inheritDoc}
	 * * @param dueDate is the {@link LoanEntity}s with this due date we are looking for.
	 * * @return a {@link List<LoanResponse>} that match that due date.
	 */
	@Override
	public List<LoanEntity> getAllLoansByMaturityDate(LocalDate maturityDate) {
		return loanRepository.findAllByMaturityDate(maturityDate);
	}

	@Override
	public LoanEntity saveLoan(LoanEntity loan) {
		return loanRepository.save(loan);
	}

}
