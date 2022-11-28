package com.example.banksystem.services.interfaces;

import com.example.banksystem.exceptions.EntityNotFoundException;
import com.example.banksystem.exceptions.InsufficientFundsException;
import com.example.banksystem.models.entities.AccountEntity;
import com.example.banksystem.models.entities.CheckingAccountEntity;
import com.example.banksystem.models.entities.TransactionEntity;
import com.example.banksystem.models.requsts.TransactionCreationRequest;
import com.example.banksystem.models.requsts.TransferCreationRequest;
import com.example.banksystem.models.responses.TransactionResponse;
import com.example.banksystem.models.responses.TransferResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * A Services class for {@link TransactionEntity}. Handles transaction creation with received input from
 * TransactionController, saving them in the database and retrieving
 * them from it. Has functions for withdraw, deposit and transfer transactions.
 */
public interface TransactionService {
	/**
	 * Creates a deposit transaction and add the given funds to the balance of an account.
	 * <p>An {@link AccountEntity} with the given Iban is created. Then its balance is increased with the
	 * imputed amount and updated. A transaction of type deposit is created and saved with a random {@link UUID}.
	 * <p>A {@link TransactionResponse} is created with the new transaction's information and returned.
	 *
	 * @param userId the id of the user, issuing the transaction.
	 * @param body   a {@link TransactionResponse} entity, containing all the parameters for the deposition transaction.
	 * @return a {@link TransactionResponse} containing the information of the created deposition transaction.
	 * @throws EntityNotFoundException when the given id or Iban do not match with any account.
	 */
	TransactionResponse deposit(Long userId, TransactionCreationRequest body);

	/**
	 * Creates a withdrawal transaction and deduces the given funds from the balance of an account.
	 * <p>An {@link AccountEntity} with the given Iban is created. Then its balance is decreased by the
	 * imputed amount and updated in the database. A transaction of type withdraw is created
	 * and saved with a random {@link UUID}.
	 * <p>A {@link TransactionResponse} is created with the new transaction's information and returned.
	 *
	 * @param userId the id of the user, issuing the transaction.
	 * @param body   a {@link TransactionResponse} entity, containing all the parameters for the withdrawal transaction.
	 * @return a {@link TransactionResponse} containing the information of the created withdrawal transaction.
	 * @throws EntityNotFoundException    when the given id or Iban do not match with any account.
	 * @throws InsufficientFundsException when the balance in the retrieved account is less than the given
	 *                                    amount to withdraw.
	 */
	TransactionResponse withdraw(Long userId, TransactionCreationRequest body);

	TransferResponse transfer(Long userId, TransferCreationRequest body);

	List<TransactionResponse> getAllTransactionsByGivenCriteria(String type, String dateOn, String dateBefore,
																String dateAfter);
	List<TransactionResponse> getAllTransactionsForUserByGivenCriteria(Long userId, Long accountId,
																	   String type, String dateOn,
																	   String dateBefore, String dateAfter);

	/**
	 * Saves a payment in the form of {@link TransactionEntity} by given parameters to the TransactionRepository.
	 * <p>A {@link TransactionEntity} is created with the new transaction's information by the
	 *  makeTransactionEntity method and then saved to repository.
	 *
	 * @param tax is the amount of the transaction.
	 * @param account is the account with which the money is associated.
	 * @param type is the type of the transaction
	 */
    void savePaymentInTransaction(BigDecimal tax, CheckingAccountEntity account, String type);

	/**
	 * Make a transfer of loan amount to checking account
	 *
	 * @param userId the user id
	 * @param accountIban the account to which the loan amount should be transferred
	 * @param loanAmount the loan amount
	 */
	void makeLoanTransfer(Long userId, String accountIban, BigDecimal loanAmount);

}
