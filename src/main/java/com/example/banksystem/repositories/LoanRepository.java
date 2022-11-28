package com.example.banksystem.repositories;

import com.example.banksystem.models.entities.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, Long> {

	List<LoanEntity> findAllByDueDate(LocalDate dueDate);

	List<LoanEntity> findAllByMaturityDate(LocalDate maturityDate);

	Optional<LoanEntity> findLoanById(Long loanId);
}
