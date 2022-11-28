package com.example.banksystem.services.interfaces;

import com.example.banksystem.models.entities.LoanEntity;
import com.example.banksystem.models.requsts.CalculateLoanRequest;
import com.example.banksystem.models.requsts.LoanApplyRequest;
import com.example.banksystem.models.responses.CalculateLoanResponse;
import com.example.banksystem.models.responses.LoanResponse;
import com.example.banksystem.repositories.LoanRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * A service interface for {@link LoanResponse}. Has methods for get all loan, get by type and get by Id.
 */
@Service
public interface LoanService {

	/**
	 * Gets all {@link LoanEntity}s from the database.
	 *
	 * @return a {@link List} of {@link LoanResponse}.
	 */
	List<LoanResponse> getAllLoans();

	/**
	 * Gets all {@link LoanEntity}s from the database.
	 *
	 * @param loanId the id of the loan we are looking for
	 * @param userId the id of the authenticated user
	 * @return a {@link LoanResponse} that match the id submitted.
	 */
	LoanResponse getLoanResponseById(Long loanId, Long userId);

	LoanResponse createLoan(LoanApplyRequest loanApplyRequest, Long id);

	/**
	 * Gets a {@link CalculateLoanRequest} from the user, calls the LoanCalculatorService to do the calculations
	 * and then returns {@link CalculateLoanResponse} .
	 *
	 * @param calculateLoanRequest the request sent from the user.
	 * @return a {@link CalculateLoanResponse} the calculated response from the LoanCalculatorService.
	 */
	CalculateLoanResponse calculateLoan(CalculateLoanRequest calculateLoanRequest);

	/**
	 * Gets all {@link LoanEntity}s from the database with a given dueDate.
	 *
	 * @param dueDate is the {@link LoanEntity}s with this due date we are looking for.
	 * @return a {@link List<LoanResponse>} that match that dueDate.
	 */
	List<LoanEntity> getAllLoansByDueDate(LocalDate dueDate);

	/**
	 * Deletes a {@link LoanEntity} from {@link LoanRepository}.
	 */
	void deleteLoan(LoanEntity loan);

	/**
	 * Gets all {@link LoanEntity}s from the database with a given maturityDate.
	 *
	 * @param maturityDate is the {@link LoanEntity}s with this due date we are looking for.
	 * @return a {@link List<LoanResponse>} that match that maturityDate.
	 */
	List<LoanEntity> getAllLoansByMaturityDate(LocalDate maturityDate);

	LoanEntity saveLoan(LoanEntity loan);

}
