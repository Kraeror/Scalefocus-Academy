package com.example.banksystem.repositories;

import com.example.banksystem.models.entities.LoanTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanTypeRepository extends JpaRepository<LoanTypeEntity, Long> {
	/**
	 * Gets an optional object of {@link LoanTypeEntity} that is assigned to the given loan type.
	 * @param loanType the type of the loan it is assigned to.
	 * @return a {@link LoanTypeEntity} with the given loan type from the repository.
	 */
	Optional<LoanTypeEntity> findByName(String loanType);
}
