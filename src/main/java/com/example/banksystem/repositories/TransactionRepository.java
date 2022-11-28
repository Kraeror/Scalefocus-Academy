package com.example.banksystem.repositories;

import com.example.banksystem.models.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    /**
     * Gets a list of {@link TransactionEntity}s that are assigned to the given account id.
     * @param accountId the account id that the transaction is assigned to.
     * @return a {@link List <TransactionEntity>} with all matching {@link TransactionEntity}s from
     * the repository.
     */
    List<TransactionEntity> findAllByAccountEntity_id(Long accountId);
    /**
     * Gets a list of {@link TransactionEntity}s that are assigned to the transaction type.
     * @param type the transaction type that the transaction is assigned to.
     * @return a {@link List <TransactionEntity>} with all matching {@link TransactionEntity}s from
     * the repository.
     */
    List<TransactionEntity> findAllByType(String type);
    /**
     * Gets a list of {@link TransactionEntity}s that are assigned to the transaction type and account id.
     * @param type the transaction type that the transaction is assigned to.
     * @param accountId the account id that the transaction is assigned to.
     * @return a {@link List <TransactionEntity>} with all matching {@link TransactionEntity}s from
     * the repository.
     */
    List<TransactionEntity> findAllByTypeAndAccountEntity_id(String type, Long accountId);
    /**
     * Gets a list of {@link TransactionEntity}s that are created between<br/>
     * two dates and time and have the given transaction type.
     * @param startTime the starting date and time of the transaction creation.
     * @param endTime the end date and time of the transaction creation.
     * @param type the transaction type that the transaction is assigned to.
     * @return a {@link List <TransactionEntity>} with all matching {@link TransactionEntity}s from
     * the repository.
     */
    List<TransactionEntity> findAllByCreatedOnBetweenAndType(LocalDateTime startTime,
                                                                             LocalDateTime endTime, String type);
    /**
     * Gets a list of {@link TransactionEntity}s that are created between<br/>
     * two dates and time, have the given transaction type and account id.
     * @param startTime the starting date and time of the transaction creation.
     * @param endTime the end date and time of the transaction creation.
     * @param type the transaction type that the transaction is assigned to.
     * @param accountId the id for the account that the transaction belongs to.
     * @return a {@link List <TransactionEntity>} with all matching {@link TransactionEntity}s from
     * the repository.
     */
    List<TransactionEntity> findAllByCreatedOnBetweenAndTypeAndAccountEntity_id(LocalDateTime startTime,
                                                             LocalDateTime endTime, String type, Long accountId);
    /**
     * Gets a list of {@link TransactionEntity}s that are created before<br/>
     * the given date and time and have the given transaction type.
     * @param startTime the starting date and time of the transaction creation.
     * @param type the transaction type that the transaction is assigned to.
     * @return a {@link List <TransactionEntity>} with all matching {@link TransactionEntity}s from
     * the repository.
     */
    List<TransactionEntity> findAllByCreatedOnBeforeAndType(LocalDateTime startTime, String type);
    /**
     * Gets a list of {@link TransactionEntity}s that are created after<br/>
     * the given date and time and have the given transaction type and account id.
     * @param startTime the starting date and time of the transaction creation.
     * @param type the transaction type that the transaction is assigned to.
     * @param accountId the id for the account that the transaction belongs to.
     * @return a {@link List <TransactionEntity>} with all matching {@link TransactionEntity}s from
     * the repository.
     */
    List<TransactionEntity> findAllByCreatedOnBeforeAndTypeAndAccountEntity_id(LocalDateTime startTime,
                                                                               String type, Long accountId);
    /**
     * Gets a list of {@link TransactionEntity}s that are created after<br/>
     * the given date and time and have the given transaction type.
     * @param endTime the end date and time of the transaction creation.
     * @param type the transaction type that the transaction is assigned to.
     * @return a {@link List <TransactionEntity>} with all matching {@link TransactionEntity}s from
     * the repository.
     */
    List<TransactionEntity> findAllByCreatedOnAfterAndType(LocalDateTime endTime, String type);
    /**
     * Gets a list of {@link TransactionEntity}s that are created after<br/>
     * the given date and time and have the given transaction type and account id.
     * @param endTime the end date and time of the transaction creation.
     * @param type the transaction type that the transaction is assigned to.
     * @param accountId the id for the account that the transaction belongs to.
     * @return a {@link List <TransactionEntity>} with all matching {@link TransactionEntity}s from
     * the repository.
     */
    List<TransactionEntity> findAllByCreatedOnAfterAndTypeAndAccountEntity_id(LocalDateTime endTime,
                                                                              String type, Long accountId);
    /**
     * Gets a list of {@link TransactionEntity}s that are created before the given date and time.
     * @param startTime the starting date and time of the transaction creation.
     * @return a {@link List <TransactionEntity>} with all matching {@link TransactionEntity}s from
     * the repository.
     */
    List<TransactionEntity> findAllByCreatedOnBefore(LocalDateTime startTime);
    /**
     * Gets a list of {@link TransactionEntity}s that are created before<br/>
     * the given date and time and have the given account id.
     * @param startTime the starting date and time of the transaction creation.
     * @param accountId the id for the account that the transaction belongs to.
     * @return a {@link List <TransactionEntity>} with all matching {@link TransactionEntity}s from
     * the repository.
     */
    List<TransactionEntity> findAllByCreatedOnBeforeAndAccountEntity_id(LocalDateTime startTime, Long accountId);
    /**
     * Gets a list of {@link TransactionEntity}s that are created after the given date and time.
     * @param endTime the end date and time of the transaction creation.
     * @return a {@link List <TransactionEntity>} with all matching {@link TransactionEntity}s from
     * the repository.
     */
    List<TransactionEntity> findAllByCreatedOnAfter(LocalDateTime endTime);
    /**
     * Gets a list of {@link TransactionEntity}s that are created after<br/>
     * the given date and time and have the given account id.
     * @param endTime the end date and time of the transaction creation.
     * @param accountId the id for the account that the transaction belongs to.
     * @return a {@link List <TransactionEntity>} with all matching {@link TransactionEntity}s from
     * the repository.
     */
    List<TransactionEntity> findAllByCreatedOnAfterAndAccountEntity_id(LocalDateTime endTime, Long accountId);
    /**
     * Gets a list of {@link TransactionEntity}s that are created between two dates and time.
     * @param startTime the starting date and time of the transaction creation.
     * @param endTime the end date and time of the transaction creation.
     * @return a {@link List <TransactionEntity>} with all matching {@link TransactionEntity}s from
     * the repository.
     */
    List<TransactionEntity> findAllByCreatedOnBetween(LocalDateTime startTime, LocalDateTime endTime);
    /**
     * Gets a list of {@link TransactionEntity}s that are created between<br/>
     * two dates and time and have the given account id.
     * @param startTime the starting date and time of the transaction creation.
     * @param endTime the end date and time of the transaction creation.
     * @param accountId the id for the account that the transaction belongs to.
     * @return a {@link List <TransactionEntity>} with all matching {@link TransactionEntity}s from
     * the repository.
     */
    List<TransactionEntity> findAllByCreatedOnBetweenAndAccountEntity_id(LocalDateTime startTime,
                                                                         LocalDateTime endTime, Long accountId);
    /**
     * Gets a list of {@link TransactionEntity}s that are created between two dates and time.
     * @param startTime the starting date and time of the transaction creation.
     * @param endTime the end date and time of the transaction creation.
     * @return a {@link List <TransactionEntity>} with all matching {@link TransactionEntity}s from
     * the repository.
     */
    List<TransactionEntity> findAllByCreatedOnBeforeAndCreatedOnAfter(LocalDateTime startTime,
                                                                                         LocalDateTime endTime);
    /**
     * Gets a list of {@link TransactionEntity}s that are created between<br/>
     * two dates and time and have the given account id.
     * @param startTime the starting date and time of the transaction creation.
     * @param endTime the end date and time of the transaction creation.
     * @param accountId the id for the account that the transaction belongs to.
     * @return a {@link List <TransactionEntity>} with all matching {@link TransactionEntity}s from
     * the repository.
     */
    List<TransactionEntity> findAllByCreatedOnBeforeAndCreatedOnAfterAndAccountEntity_id(LocalDateTime startTime,
                                                                      LocalDateTime endTime, Long accountId);
    /**
     * Gets a list of {@link TransactionEntity}s that are created between<br/>
     * two dates and time and have the given and transaction type.
     * @param startTime the starting date and time of the transaction creation.
     * @param endTime the end date and time of the transaction creation.
     * @param type the transaction type that the transaction is assigned to.
     * @return a {@link List <TransactionEntity>} with all matching {@link TransactionEntity}s from
     * the repository.
     */
    List<TransactionEntity> findAllByCreatedOnBeforeAndCreatedOnAfterAndType(LocalDateTime startTime,
                                                                             LocalDateTime endTime, String type);
    /**
     * Gets a list of {@link TransactionEntity}s that are created between<br/>
     * two dates and time and have the given transaction type and account id.
     * @param startTime the starting date and time of the transaction creation.
     * @param endTime the end date and time of the transaction creation.
     * @param type the transaction type that the transaction is assigned to.
     * @param accountId the id for the account that the transaction belongs to.
     * @return a {@link List <TransactionEntity>} with all matching {@link TransactionEntity}s from
     * the repository.
     */
    List<TransactionEntity> findAllByCreatedOnBeforeAndCreatedOnAfterAndTypeAndAccountEntity_id(LocalDateTime startTime,
                                                                             LocalDateTime endTime,
                                                                             String type, Long accountId);

}
