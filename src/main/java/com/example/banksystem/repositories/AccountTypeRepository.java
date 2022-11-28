package com.example.banksystem.repositories;

import com.example.banksystem.models.entities.AccountTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountTypeRepository extends JpaRepository<AccountTypeEntity, Long>
{

    /** Gets an optional object of {@link AccountTypeEntity} that is assigned to the given account type.
     * @param type the type of the user account.
     * @return an {@link AccountTypeEntity} with the given type from the repository.
     */
    Optional<AccountTypeEntity> findByType(String type);
}
