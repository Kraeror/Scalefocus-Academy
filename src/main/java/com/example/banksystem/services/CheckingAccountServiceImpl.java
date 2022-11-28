package com.example.banksystem.services;

import com.example.banksystem.exceptions.*;
import com.example.banksystem.models.entities.AccountEntity;
import com.example.banksystem.models.entities.AccountTypeEntity;
import com.example.banksystem.models.entities.CheckingAccountEntity;
import com.example.banksystem.models.entities.UserEntity;
import com.example.banksystem.models.responses.CheckingAccountResponse;
import com.example.banksystem.repositories.CheckingAccountRepository;
import com.example.banksystem.services.interfaces.AccountTypeService;
import com.example.banksystem.services.interfaces.CheckingAccountService;
import com.example.banksystem.services.interfaces.UserService;
import org.iban4j.Iban;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An implementation class of {@link CheckingAccountService} interface.
 */
@Service
public class CheckingAccountServiceImpl implements CheckingAccountService {
	private final CheckingAccountRepository checkingAccountRepository;
	private final UserService userService;
	private final AccountTypeService accountTypeService;
	private final ModelMapper modelMapper;

	public CheckingAccountServiceImpl(CheckingAccountRepository checkingAccountRepository,
	                                  UserService userService, AccountTypeService accountTypeService,
	                                  ModelMapper modelMapper) {
		this.checkingAccountRepository = checkingAccountRepository;
		this.userService = userService;
		this.accountTypeService = accountTypeService;
		this.modelMapper = modelMapper;
	}

	/**
	 * Creates an {@link CheckingAccountEntity} with data from a given entity and assigns it to the
	 * given user. Multiple service calls are used to map from names to IDs.
	 *
	 * @param id                             the username for the user to whom the account will be assigned.
	 * @return an {@link CheckingAccountResponse} with the data of the saved account.
	 */
	@Override
	public CheckingAccountResponse createAccount(Long id) {
		AccountTypeEntity accountType = accountTypeService.findAccountTypeByType("Checking");
		UserEntity user = userService.getUserEntityById(id);

		String iban = Iban.random().toString();

		CheckingAccountEntity accountEntity = new CheckingAccountEntity();
		accountEntity.setBalance(BigDecimal.valueOf(0));
		accountEntity.setType(accountType);
		accountEntity.setUser(user);
		accountEntity.setIban(iban);

		CheckingAccountEntity saved = checkingAccountRepository.save(accountEntity);

		return mapAccountEntityToAccountResponse(saved);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param iban the iban of the {@link CheckingAccountEntity}.
	 * @return an {@link CheckingAccountEntity}.
	 * @throws EntityNotFoundException when an account with the given Iban is not found.
	 */
	@Override
	public CheckingAccountEntity getAccountByIban(String iban) {
		return checkingAccountRepository.findByIban(iban)
						.orElseThrow(() -> new EntityNotFoundException("CheckingAccount"));
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param userId the Id of the user to whom the account belongs.
	 * @param iban   the Iban of the account.
	 * @return an {@link AccountEntity}.
	 * @throws EntityNotFoundException when an account with the given userID
	 *                                 and Iban is not found.
	 */
	@Override
	public CheckingAccountEntity getAccountByUserIdAndIban(Long userId, String iban) {
		return this.checkingAccountRepository.findByUserIdAndIban(userId, iban)
						.orElseThrow(AccountNotBelongToUserException::new);
	}

	@Override
	public CheckingAccountResponse getAccountByTypeAndUserId(String accountType, Long userId) {
		AccountTypeEntity accountTypeEntity = accountTypeService.findAccountTypeByType(accountType);
		List<CheckingAccountEntity> accounts = this.checkingAccountRepository
				.findByTypeAndUserId(accountTypeEntity, userId);

		if (accounts.isEmpty()) {
			return this.createAccount(userId);
		}
		return modelMapper.map(accounts.get(0), CheckingAccountResponse.class);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param account the account on which the transaction will be executed.
	 * @param amount  the amount to be withdrawn from the account.
	 * @throws com.example.banksystem.exceptions.InsufficientFundsException when the
	 * given amount to withdraw is greater than the balance in the account.
	 */
	@Override
	public void withdraw(CheckingAccountEntity account, BigDecimal amount) {
		BigDecimal totalWithdraw =
				amount.add(accountTypeService.findAccountTypeByType(account.getType().getType()).getTransactionFee());

		if (totalWithdraw.compareTo(account.getBalance()) > 0)
			throw new InsufficientFundsException("The selected account has Insufficient funds for this transaction!");

		account.setBalance(account.getBalance().subtract(totalWithdraw));
		this.checkingAccountRepository.save(account);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param account the account on which the transaction will be executed.
	 * @param amount  the amount to be deposited in the account.
	 */
	@Override
	public void deposit(CheckingAccountEntity account, BigDecimal amount) {
		account.setBalance(account.getBalance().add(amount));
		this.checkingAccountRepository.save(account);
	}

	@Override
	public List<CheckingAccountEntity> getAllAccounts() {
		List<CheckingAccountEntity> accounts = checkingAccountRepository.findAll();
		if (accounts.isEmpty()) {
			throw new NoRecordsOfEntityInTheDatabase("CheckingAccount");
		}
		return accounts;
	}

	/**
	 * {@inheritDoc}
	 * @param accountId the ID of the account.
	 * @return an {@link CheckingAccountEntity}.
	 * @throws EntityNotFoundException when an account with the given id is not found.
	 */
	@Override
	public CheckingAccountResponse getAccountById(Long accountId) {
		CheckingAccountEntity account = checkingAccountRepository.findById(accountId)
						.orElseThrow(() -> new EntityNotFoundException("CheckingAccount"));
		return modelMapper.map(account, CheckingAccountResponse.class);
	}

	@Override
	public CheckingAccountEntity saveAccount(CheckingAccountEntity account) {
		return checkingAccountRepository.save(account);
	}

	/**
	 * Retrieves a list of {@link CheckingAccountEntity} from {@link CheckingAccountRepository} with the given userId
	 * and maps it to {@link CheckingAccountResponse}.
	 *
	 * @param userId the ID of the user to whom the accounts are assigned.
	 * @return a {@link List<CheckingAccountEntity>} with all matching {@link CheckingAccountEntity}s from
	 * the repository.
	 * @throws EntityNotFoundException when there are no matching accounts or a user with that ID.
	 */
	@Override
	public List<CheckingAccountResponse> getAllAccountResponsesByUserId(Long userId, Long loggedInUserId) {
		if(!isAdmin(loggedInUserId) && !userId.equals(loggedInUserId)) {
			throw new ResourceNotBelongingToUser("Checking account");
		}
		List<CheckingAccountEntity> allAccounts = checkingAccountRepository.findByUserId(userId);
			if (allAccounts.isEmpty()) {
				throw new NoRecordsOfEntityInTheDatabase("CheckingAccount");
			}
		return allAccounts.stream().map(this::mapAccountEntityToAccountResponse).collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return a {@link List} of {@link CheckingAccountResponse}s
	 */
	@Override
	public List<CheckingAccountResponse> getAllAccountResponses() {
		List<CheckingAccountEntity> allAccounts = getAllAccounts();
		return allAccounts
						.stream().map(this::mapAccountEntityToAccountResponse).collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param iban the Iban of the sought account.
	 * @return an {@link CheckingAccountResponse}
	 * @throws EntityNotFoundException when an account with the given Iban is not found.
	 */
	@Override
	public CheckingAccountResponse getAccountResponseByIban(String iban) {
		return mapAccountEntityToAccountResponse(getAccountByIban(iban));
	}

	@Override
	public CheckingAccountResponse getCheckingAccountById(Long acId, Long usId) {
		CheckingAccountEntity checkingAccount = this.checkingAccountRepository.findById(acId)
				.orElseThrow(() -> new EntityNotFoundException("CheckingAccount"));
		UserEntity user = userService.getUserEntityById(usId);
		if(user.getRoles().stream().noneMatch(role -> role.getRole().equals("ADMIN")) &&
				checkingAccount.getUser().getId() != usId){
			throw new ResourceNotBelongingToUser("Checking Account");
		}
		return modelMapper.map(checkingAccount,CheckingAccountResponse.class).setUserFullName(user.getFullName());
	}

	/**
	 * Maps {@link CheckingAccountEntity} to {@link CheckingAccountResponse}.
	 *
	 * @param entity an {@link CheckingAccountEntity}
	 * @return an {@link CheckingAccountResponse}
	 */
	public CheckingAccountResponse mapAccountEntityToAccountResponse(CheckingAccountEntity entity) {
		CheckingAccountResponse viewModel = modelMapper.map(entity, CheckingAccountResponse.class);
		viewModel.setType(entity.getType().getType());
		viewModel.setUserFullName(entity.getUser().getFullName());
		return viewModel;
	}

	private boolean isAdmin(Long usId) {
		return this.userService.getUserEntityById(usId)
				.getRoles().stream().anyMatch(role -> role.getRole().equals("ADMIN"));
	}

}
