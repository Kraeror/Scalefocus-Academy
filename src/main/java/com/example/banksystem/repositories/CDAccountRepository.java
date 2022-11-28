package com.example.banksystem.repositories;

import com.example.banksystem.models.entities.CDAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CDAccountRepository extends JpaRepository<CDAccountEntity,Long> {
    /**
     * Gets an optional object of {@link CDAccountEntity} that is assigned to the given IBAN.
     * @param iban the IBAN of the CD account.
     * @return a {@link CDAccountEntity} with the given IBAN from the repository.
     */
    Optional<CDAccountEntity> findByIban(String iban);

    List<CDAccountEntity> findAllByExpirationDate (LocalDate date);

    List<CDAccountEntity> findAllByUser_Id(Long userId);
}
