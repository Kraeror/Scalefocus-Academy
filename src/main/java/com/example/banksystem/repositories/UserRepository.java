package com.example.banksystem.repositories;

import com.example.banksystem.models.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    /**
     * Gets an optional object of {@link UserEntity} that is assigned to the given user email.
     * @param email the given E-mail that the user is assigned to.
     * @return a {@link UserEntity} with the given E-mail from the repository.
     */
    Optional<UserEntity> findByEmail(String email);
    /**
     * Gets an optional object of {@link UserEntity} that is assigned to the given username.
     * @param username the given username that the user is assigned to.
     * @return a {@link UserEntity} with the given username from the repository.
     */
    Optional<UserEntity> findByUsername(String username);
    /**
     * Gets an optional object of {@link UserEntity} that is assigned to the given user account id.
     * @param accountId the given account id that the user is assigned to.
     * @return a {@link UserEntity} with the given account id from the repository.
     */
    Optional<UserEntity> findByCheckingAccounts_id(Long accountId);
}
