package com.example.banksystem.services;

import com.example.banksystem.exceptions.*;
import com.example.banksystem.models.entities.CheckingAccountEntity;
import com.example.banksystem.models.entities.TransactionEntity;
import com.example.banksystem.models.requsts.TransactionCreationRequest;
import com.example.banksystem.models.requsts.TransferCreationRequest;
import com.example.banksystem.models.responses.TransactionResponse;
import com.example.banksystem.models.responses.TransferResponse;
import com.example.banksystem.repositories.TransactionRepository;
import com.example.banksystem.services.interfaces.CheckingAccountService;
import com.example.banksystem.services.interfaces.TransactionService;
import com.example.banksystem.services.interfaces.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * An implementation of {@link TransactionService} interface.
 */
@Service
public class TransactionServiceImpl implements TransactionService {

	private final TransactionRepository transactionRepository;
	private final CheckingAccountService checkingAccountService;
	private final UserService userService;
	private final ModelMapper modelMapper;

	public TransactionServiceImpl(TransactionRepository transactionRepository,
								  CheckingAccountService checkingAccountService,
								  UserService userService, ModelMapper modelMapper) {
		this.transactionRepository = transactionRepository;
		this.checkingAccountService = checkingAccountService;
		this.userService = userService;
		this.modelMapper = modelMapper;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param userId the id of the user, issuing the transaction.
	 * @param body   a {@link TransactionResponse} entity, containing all the parameters for the deposition transaction.
	 * @return a {@link TransactionResponse} containing the information of the created deposition transaction.
	 * @throws EntityNotFoundException when the given id or Iban do not match with any account.
	 */
	@Override
	public TransactionResponse deposit(Long userId, TransactionCreationRequest body) {
		CheckingAccountEntity account = checkingAccountService.getAccountByUserIdAndIban(userId, body.getIban());

		this.checkingAccountService.deposit(account, body.getAmount());

		TransactionEntity transaction = makeTransactionEntity(body.getAmount(), account, "deposit");

		return modelMapper.map(transactionRepository.save(transaction), TransactionResponse.class)
						.setUserName(account.getUser().getFullName())
						.setCreatedOn(LocalDateTime.now()).setIban(body.getIban());
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param userId the id of the user, issuing the transaction.
	 * @param body   a {@link TransactionResponse} entity, containing all the parameters for the withdrawal transaction.
	 * @return a {@link TransactionResponse} containing the information of the created withdrawal transaction.
	 * @throws EntityNotFoundException    when the given id or Iban do not match with any account.
	 * @throws InsufficientFundsException when the balance in the retrieved account is less than the given
	 *                                    amount to withdraw.
	 */
	@Override
	public TransactionResponse withdraw(Long userId, TransactionCreationRequest body) {
		CheckingAccountEntity account = checkingAccountService.getAccountByUserIdAndIban(userId, body.getIban());
		this.checkingAccountService.withdraw(account, body.getAmount());

		TransactionEntity transaction = makeTransactionEntity(body.getAmount(), account, "withdraw");

		return modelMapper.map(transactionRepository.save(transaction), TransactionResponse.class)
						.setUserName(account.getUser().getFullName())
						.setCreatedOn(LocalDateTime.now()).setIban(body.getIban());
	}


	/**
	 * {@inheritDoc}
	 *
	 * @param tax     is the amount of the transaction.
	 * @param account is the account with which the money is associated.
	 * @param type    is the type of the transaction
	 */
	@Override
	public void savePaymentInTransaction(BigDecimal tax, CheckingAccountEntity account, String type) {
		transactionRepository.save(makeTransactionEntity(tax, account, type));
	}

    /**
     * {@inheritDoc}
     * @param userId the user id
     * @param accountIban the account to which the loan amount should be transferred
     * @param loanAmount the loan amount
     */
	@Override
	public void makeLoanTransfer(Long userId, String accountIban, BigDecimal loanAmount) {
		TransactionCreationRequest transactionRequest = new TransactionCreationRequest()
						.setIban(accountIban)
						.setAmount(loanAmount)
						.setReason("deposit loan amount");

		this.deposit(userId, transactionRequest);
	}

	protected TransactionEntity makeTransactionEntity(BigDecimal tax, CheckingAccountEntity account, String type) {
		return new TransactionEntity()
						.setUUid(UUID.randomUUID())
						.setAmount(tax)
						.setReason("-")
						.setType(type)
						.setAccountEntity(account);
	}

	@Override
	public TransferResponse transfer(Long userId, TransferCreationRequest body) {
		CheckingAccountEntity accountFrom =
								this.checkingAccountService.getAccountByUserIdAndIban(userId, body.getAccountFrom());
		CheckingAccountEntity accountTo = this.checkingAccountService.getAccountByIban(body.getAccountTo());

		UUID transactionUUID = UUID.randomUUID();
		LocalDateTime createdTime = LocalDateTime.now();

		TransactionEntity transactionFrom = getTransactionEntity(body, accountFrom, "send",
						transactionUUID, createdTime);

		this.checkingAccountService.withdraw(accountFrom, body.getAmount());
		this.transactionRepository.save(transactionFrom);

		TransactionEntity transactionTo = getTransactionEntity(body, accountTo, "received",
						transactionUUID, createdTime);

		this.checkingAccountService.deposit(accountTo, body.getAmount());
		TransactionEntity entity = this.transactionRepository.save(transactionTo);

		return modelMapper.map(entity, TransferResponse.class)
						.setId(entity.getId())
						.setUserName(accountFrom.getUser().getUsername())
						.setSenderIBAN(accountFrom.getIban())
						.setReceiverIBAN(accountTo.getIban())
						.setReason(body.getReason())
						.setType(accountFrom.getType().getType())
						.setCreatedOn(entity.getCreatedOn());
	}

	@Override
	public List<TransactionResponse> getAllTransactionsByGivenCriteria(String type, String dateOnString,
																	   String dateBeforeString,
																	   String dateAfterString) {
		LocalDate dateOn = dateParser(dateOnString);
		LocalDate dateBefore = dateParser(dateBeforeString);
		LocalDate dateAfter = dateParser(dateAfterString);

		if (dateOn != null && (dateBefore != null || dateAfter != null)) {
			throw new IncorrectDateFilteringException();
		}
		List<TransactionEntity> transactions;
		if (dateBefore != null && dateAfter != null && type != null) {
			transactions = transactionRepository
					.findAllByCreatedOnBeforeAndCreatedOnAfterAndType(dateBefore.atTime(LocalTime.MIN),
							dateAfter.atTime(LocalTime.MAX), type);
		} else if (dateBefore != null && dateAfter != null) {
			transactions = transactionRepository
					.findAllByCreatedOnBeforeAndCreatedOnAfter(dateBefore.atTime(LocalTime.MIN),
							dateAfter.atTime(LocalTime.MAX));
		} else if (dateBefore != null && type != null) {
			transactions = transactionRepository
					.findAllByCreatedOnBeforeAndType(dateBefore.atTime(LocalTime.MIN), type);
		} else if (dateAfter != null && type != null) {
			transactions = transactionRepository
					.findAllByCreatedOnAfterAndType(dateAfter.atTime(LocalTime.MAX), type);
		} else if (dateOn != null && type != null) {
			transactions = transactionRepository
					.findAllByCreatedOnBetweenAndType(dateOn.atTime(LocalTime.MIN), dateOn.atTime(LocalTime.MAX), type);
		} else if (dateBefore != null) {
			transactions = transactionRepository.findAllByCreatedOnBefore(dateBefore.atTime(LocalTime.MIN));
		} else if (dateAfter != null) {
			transactions = transactionRepository
					.findAllByCreatedOnAfter(dateAfter.atTime(LocalTime.MAX));
		} else if (dateOn != null) {
			transactions = transactionRepository
					.findAllByCreatedOnBetween(dateOn.atTime(LocalTime.MIN), dateOn.atTime(LocalTime.MAX));
		} else if (type != null) {
			transactions = transactionRepository
					.findAllByType(type);
		} else {
			transactions = transactionRepository.findAll();
		}
		return transactionsListMapperFromEntityToResponse(transactions);
	}

	protected LocalDate dateParser(String dateString) {
		if(dateString == null || dateString.isBlank()){
			return null;
		}
		LocalDate parsed = null;
		try{
			parsed = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		}catch (Exception e){
			throw new IllegalArgumentException("Invalid date format! Please use yyyy-MM-dd!");
		}
		return parsed;
	}

	@Override
	public List<TransactionResponse> getAllTransactionsForUserByGivenCriteria(Long userId, Long accountId,
																			  String type, String dateOnString,
																			  String dateBeforeString,
																			  String dateAfterString) {
		if(!accountBelongsToTheUser(userId, accountId)) {
			throw new AccountNotBelongToUserException();
		}

		LocalDate dateOn = dateParser(dateOnString);
		LocalDate dateBefore = dateParser(dateBeforeString);
		LocalDate dateAfter = dateParser(dateAfterString);

		if(dateOn != null && (dateBefore != null || dateAfter != null)) {
			throw new IncorrectDateFilteringException();
		}
		List<TransactionEntity> transactions;
		if(dateBefore != null && dateAfter != null && type != null){
			transactions = transactionRepository
					.findAllByCreatedOnBeforeAndCreatedOnAfterAndTypeAndAccountEntity_id
							(dateBefore.atTime(LocalTime.MIN), dateAfter.atTime(LocalTime.MAX), type, accountId);
		} else if(dateBefore != null && dateAfter != null){
			transactions = transactionRepository
					.findAllByCreatedOnBeforeAndCreatedOnAfterAndAccountEntity_id(dateBefore.atTime(LocalTime.MIN),
							dateAfter.atTime(LocalTime.MAX), accountId);
		} else if(dateBefore != null && type != null){
			transactions = transactionRepository
					.findAllByCreatedOnBeforeAndTypeAndAccountEntity_id
																(dateBefore.atTime(LocalTime.MIN), type, accountId);
		} else if(dateAfter != null && type != null){
			transactions = transactionRepository
					.findAllByCreatedOnAfterAndTypeAndAccountEntity_id(dateAfter.atTime(LocalTime.MAX),
							type, accountId);
		} else if(dateOn != null && type != null){
			transactions = transactionRepository
					.findAllByCreatedOnBetweenAndTypeAndAccountEntity_id(dateOn.atTime(LocalTime.MIN),
							dateOn.atTime(LocalTime.MAX), type, accountId);
		} else if(dateBefore != null){
			transactions = transactionRepository
					.findAllByCreatedOnBeforeAndAccountEntity_id(dateBefore.atTime(LocalTime.MIN), accountId);
		} else if(dateAfter != null){
			transactions = transactionRepository
					.findAllByCreatedOnAfterAndAccountEntity_id(dateAfter.atTime(LocalTime.MAX), accountId);
		} else if(dateOn != null){
			transactions = transactionRepository
					.findAllByCreatedOnBetweenAndAccountEntity_id(dateOn.atTime(LocalTime.MIN),
							dateOn.atTime(LocalTime.MAX), accountId);
		} else if(type != null){
			transactions = transactionRepository
					.findAllByTypeAndAccountEntity_id(type, accountId);
		} else {
			transactions = transactionRepository.findAllByAccountEntity_id(accountId);
		}
		return transactionsListMapperFromEntityToResponse(transactions);
	}

	private boolean accountBelongsToTheUser(Long userId, Long accountId) {
		return Objects.equals(userService.getUserByAccountId(accountId).getId(),
						userService.getUserEntityById(userId).getId());
	}

	private TransactionEntity getTransactionEntity(TransferCreationRequest body, CheckingAccountEntity account,
	                                               String transactionType,
	                                               UUID transactionUUID,
	                                               LocalDateTime createdTime) {
		TransactionEntity transactionFrom = new TransactionEntity()
						.setUUid(transactionUUID)
						.setAccountEntity(account)
						.setType(transactionType)
						.setReason(body.getReason())
						.setAmount(body.getAmount());
		transactionFrom.setCreatedOn(createdTime);

		return transactionFrom;
	}

	private List<TransactionResponse> transactionsListMapperFromEntityToResponse(List<TransactionEntity> list) {
		List<TransactionResponse> mappedList = list
						.stream()
						.map(transaction -> {
							CheckingAccountEntity account = transaction.getAccountEntity();
							return modelMapper.map(transaction, TransactionResponse.class)
											.setIban(account.getIban())
											.setUserName(account.getUser().getUsername());
						})
						.collect(Collectors.toList());

		if (mappedList.isEmpty()) {
			throw new NoRecordsOfEntityInTheDatabase("Transaction");
		} else {
			return mappedList;
		}
	}
}
