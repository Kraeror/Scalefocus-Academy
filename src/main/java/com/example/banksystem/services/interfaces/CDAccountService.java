package com.example.banksystem.services.interfaces;

import com.example.banksystem.exceptions.AccountNotBelongToUserException;
import com.example.banksystem.exceptions.EntityNotFoundException;
import com.example.banksystem.exceptions.NoRecordsOfEntityInTheDatabase;
import com.example.banksystem.models.entities.CDAccountEntity;
import com.example.banksystem.models.entities.CheckingAccountEntity;
import com.example.banksystem.models.requsts.CDAccountCreationRequest;
import com.example.banksystem.models.requsts.TransactionCreationRequest;
import com.example.banksystem.models.responses.CDAccountResponse;
import com.example.banksystem.models.responses.TransactionResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface CDAccountService {

    /**
     * Retrieves a {@link CDAccountEntity} with a given id from the database.
     * @param acId the ID of the sought Certification of Deposit account.
     * @param userId the ID of the sought of the user trying to see the recourse.
     * @return {@link CDAccountEntity} with the corresponding ID.
     * @throws AccountNotBelongToUserException if the resource doesn't belong to the logged-in user or he is not admin.
     * @throws EntityNotFoundException if there is no account with this ID.
     */
    CDAccountResponse getCDAccountEntityByID(Long acId,Long userId);

    /**
     * Returns a {@link List} of all {@link CDAccountEntity} in the database.
     * @return {@link List} of {@link CDAccountEntity}.
     * @throws NoRecordsOfEntityInTheDatabase when no entities are retrieved from the database.
     */
    List<CDAccountResponse> getAllCDAccountEntities();

    /**
     * Retrieves a {@link CDAccountEntity} with a given Iban from the database.
     * @param iban the Iban of the sought Certification of Deposit account.
     * @return {@link CDAccountEntity} with the corresponding Iban.
     * @throws EntityNotFoundException if there is no account with this Iban.
     */
    CDAccountResponse getCDAccountByIban(String iban);

    /**
     * Creates an {@link CDAccountEntity} with data from a given entity and assigns it to the
     * given user. Multiple service calls are used to map from names to IDs.
     *
     * @param request an entity containing the data for the new account.
     * @param id the username for the user to whom the account will be assigned.
     * @return a {@link CDAccountResponse} with the data of the saved account.
     */
    CDAccountResponse createCdAccount(CDAccountCreationRequest request, Long id);

    /**
     * Retrieves all {@link CDAccountEntity}s from the database whose expiration date matches the given one.
     * @param date the expiration date by which accounts are queried in the database.
     * @return a {@link List} of {@link CDAccountEntity}s.
     * @throws NoRecordsOfEntityInTheDatabase if no entries match the query criteria.
     */
    List<CDAccountEntity> getAllCDAccountsByDate(LocalDate date);

    /**
     * Takes a {@link CDAccountEntity}, maps and saves it as {@link CheckingAccountEntity}.
     * <p> A Certification of deposit account is mapped to a Checking account, with its accountType and balance changed.
     * The {@link CDAccountEntity} is deleted from the database and the new {@link CheckingAccountEntity} saved.
     * @param cdAccount the Certification of deposit account to be transformed.
     * @param balance the balance that will be set upon transformation of the account.
     * @return a {@link CheckingAccountEntity} made from the given Certification of deposit account.
     */
    CheckingAccountEntity transformCDAccountToCheckingAccount(CDAccountEntity cdAccount, BigDecimal balance);

    /**
     * Transforms a {@link CDAccountEntity} to {@link CheckingAccountEntity} and executes a withdrawal transaction on it.
     * <p>See {@link TransactionService} for more information.
     * @param userId the id of the issuing user.
     * @param body a {@link TransactionCreationRequest} holding the parameters for the withdrawal.
     * @return a {@link TransactionResponse} with the information of the created transaction.
     */
    TransactionResponse withdraw(Long userId, TransactionCreationRequest body);

    List<CDAccountResponse> getAllCDAccountResponsesByUserId(Long userId, Long id);

}
