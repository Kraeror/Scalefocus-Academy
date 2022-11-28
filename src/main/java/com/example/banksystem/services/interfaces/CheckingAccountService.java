package com.example.banksystem.services.interfaces;

import com.example.banksystem.exceptions.EntityNotFoundException;
import com.example.banksystem.models.entities.CheckingAccountEntity;
import com.example.banksystem.models.responses.CheckingAccountResponse;
import com.example.banksystem.repositories.CheckingAccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * A Service interface for {@link CheckingAccountEntity}. Has methods for creating an account to
 * a user, saving it and retrieving it from the database repository and manipulating
 * the {@link CheckingAccountEntity}'s fields.
 */
@Service
public interface CheckingAccountService {
	/**
	 * Creates an {@link CheckingAccountEntity} with data from a given entity and assigns it to the
	 * given user. Multiple repository calls are used to map from names to IDs.
	 *
	 * @param id                  the username for the user to whom the account will be assigned.
	 * @return an {@link CheckingAccountResponse} with the data of the saved account.
	 */
	CheckingAccountResponse createAccount(Long id);

	/**
	 * Gets an {@link CheckingAccountEntity} from {@link CheckingAccountRepository} with a given id
	 *
     * @param accountId the ID of the account.
     * @return an {@link CheckingAccountEntity}.
	 * @throws  EntityNotFoundException when an account with the given id is not found.
	 */
	CheckingAccountResponse getAccountById(Long accountId);

	/**
	 * Gets an {@link CheckingAccountEntity} from {@link CheckingAccountRepository} with a given Iban.
	 * @param iban the iban of the {@link CheckingAccountEntity}.
	 * @return an {@link CheckingAccountEntity}.
	 * @throws EntityNotFoundException when an account with the given Iban is not found.
	 */
	CheckingAccountEntity getAccountByIban(String iban);

	/**
	 * Gets an {@link CheckingAccountEntity} from {@link CheckingAccountRepository} with a given Iban
	 * and userId.
	 * @param userId the ID of the user to whom the account belongs.
	 * @param iban the Iban of the account.
	 * @return an {@link CheckingAccountEntity}.
	 * @throws  EntityNotFoundException when an account with the given userID
	 * and Iban is not found.
	 */
	CheckingAccountEntity getAccountByUserIdAndIban(Long userId, String iban);

	/**
	 * Get account of the user by its account type
	 * @param accountType
	 * @param userId
	 * @return the {@link CheckingAccountEntity} or newly created one if not exist which belongs to the user
	 */
	CheckingAccountResponse getAccountByTypeAndUserId(String accountType, Long userId);
	/**
	 * Executes a withdrawal transaction to the given account.
	 * <p>For more information on withdrawal, check {@link TransactionService}.
	 * @param account the account on which the transaction will be executed.
	 * @param amount the amount to be withdrawn from the account.
	 * @throws com.example.banksystem.exceptions.InsufficientFundsException when the
	 * given amount to withdraw is greater than the balance in the account.
	 */
	void withdraw(CheckingAccountEntity account, BigDecimal amount);

	/**
	 * Executes a deposit transaction to the given account.
	 * <p>For more information on deposit, check {@link TransactionService}.
	 * @param account the account on which the transaction will be executed.
	 * @param amount the amount to be deposited in the account.
	 */
	void deposit(CheckingAccountEntity account, BigDecimal amount);

	/**
	 * Gets all {@link CheckingAccountEntity}s from the database.
	 *  @return a {@link List} of {@link CheckingAccountEntity}.
	 * @throws IllegalArgumentException when there are no accounts.
	 */
    List<CheckingAccountEntity> getAllAccounts();

	/**
	 * Saves {@link CheckingAccountEntity} to the database.
	 * @param account the account that needs to be saved to the database.
	 * @return an {@link CheckingAccountEntity}.
	 */
	CheckingAccountEntity saveAccount(CheckingAccountEntity account);

	/** Gets a list of {@link CheckingAccountEntity}s with the given userId and maps them to {@link CheckingAccountResponse}.
	 * @param userId the ID of the user to whom the accounts are assigned.
	 * @return a {@link List} of {@link CheckingAccountResponse}s.
	 */
	List<CheckingAccountResponse> getAllAccountResponsesByUserId(Long userId, Long loggedInUserId);

	/**
	 * Gets all accounts and returns them as {@link CheckingAccountResponse}.
	 * @return a {@link List} of {@link CheckingAccountResponse}s
	 */
	List<CheckingAccountResponse> getAllAccountResponses();

	/**
	 * Gets a {@link CheckingAccountEntity} with the given Iban and maps it to {@link CheckingAccountResponse}.
	 * @param iban the Iban of the sought account.
	 * @return an {@link CheckingAccountResponse}
	 * @throws com.example.banksystem.exceptions.EntityNotFoundException when an account with the given Iban is not found.
	 */
	CheckingAccountResponse getAccountResponseByIban(String iban);

	CheckingAccountResponse getCheckingAccountById(Long acId, Long usDd);

}
