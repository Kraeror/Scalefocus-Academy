package com.example.banksystem.repositories;

import com.example.banksystem.models.entities.AccountTypeEntity;
import com.example.banksystem.models.entities.CheckingAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CheckingAccountRepository extends JpaRepository<CheckingAccountEntity,Long> {
    /**
     * Gets an optional object of {@link CheckingAccountEntity} that is assigned to the given IBAN.
     * @param iban the IBAN of the checking account it is assigned to.
     * @return a {@link CheckingAccountEntity} with the given IBAN from the repository.
     */
    Optional<CheckingAccountEntity> findByIban(String iban);
    /**
     * Gets an optional object of {@link CheckingAccountEntity} that is assigned to the given IBAN.
     * @param userId the ID of the user to whom the checking account is assigned.
     * @param iban the IBAN of the checking account it is assigned to.
     * @return a {@link CheckingAccountEntity} with the given IBAN and user ID from the repository.
     */
    Optional<CheckingAccountEntity> findByUserIdAndIban(Long userId, String iban);

    /**
     * Gets a list of {@link CheckingAccountEntity}s that are assigned to the given userId.
     * @param userId the ID of the user to whom the accounts are assigned.
     * @return a {@link List<CheckingAccountEntity>} with all matching {@link CheckingAccountEntity}s from
     * the repository.
     */
    List<CheckingAccountEntity> findByUserId(Long userId);

    List<CheckingAccountEntity> findByTypeAndUserId(AccountTypeEntity type, Long userId);
}
