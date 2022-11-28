package com.example.banksystem.services.interfaces;

import com.example.banksystem.models.entities.LoanEntity;
import com.example.banksystem.models.entities.LoanTypeEntity;
import com.example.banksystem.models.requsts.CalculateLoanRequest;
import com.example.banksystem.models.requsts.LoanApplyRequest;
import com.example.banksystem.models.responses.CalculateLoanResponse;

import java.math.BigDecimal;

/**
 * A service interface that is required to calculate the monthly installments,
 * the final amount and the loan interest that the customer must pay to the bank.
 */
public interface LoanCalculatorService {
	/**
	 * Calculate the loan with given information in parameter
	 * @param request {@link CalculateLoanRequest} necessary information about loan
	 * @return {@link CalculateLoanResponse} information about monthly installment, total refund amount
	 */
	CalculateLoanResponse calculateLoan(CalculateLoanRequest request);

	/**
	 * Create new loan if the user is eligible
	 *
	 * @param applyRequest {@link LoanApplyRequest} the required data for the loan
	 * @param userId       the id of the currently logged in user.
	 * @return {@link LoanEntity}  information for the loan
	 */
	LoanEntity createLoanAppliement(LoanApplyRequest applyRequest, Long userId);

	/**
	 * Calculates the total amount due of the loan.
	 * <p>
	 * First it calculates the total monthly payment - includes the interest and principal.
	 * After that calculates the total sum of monthly fees costs and considerations fee.
	 * </p>
	 *
	 * @param loanAmount       the amount of the loan the user is applying for
	 * @param monthlyPayment   the monthly loan payment including interest and principal
	 * @param periodInMonths   the term in months chosen by the customer to pay off the loan
	 * @param loanMonthlyFee   a monthly fee that is deducted for account maintenance and servicing
	 * @param considerationFee Fee for consideration and approval of credit (One time)
	 * @return BigDecimal the total amount due that the customer must return to the bank at the end of the loan period
	 * including interests, consideration tax and fees.
	 */
	BigDecimal calculateTotalAmountSum(BigDecimal loanAmount, BigDecimal monthlyPayment, int periodInMonths,
	                                   BigDecimal loanMonthlyFee, BigDecimal considerationFee);

	/**
	 * Calculates the total interest of the loan the customer must pay at the end of the period.
	 *
	 * @param loanAmount     the amount of the loan the user is applying for
	 * @param monthlyPayment the monthly loan payment including interest and principal
	 * @param periodInMonths the term in months chosen by the customer to pay off the loan
	 * @return BigDecimal the calculated interest in the end of the loan period.
	 */
	BigDecimal calculateTotalInterestPayment(BigDecimal loanAmount, BigDecimal monthlyPayment, int periodInMonths);

	/**
	 * Calculates the total amount due.
	 * <p>
	 * Throughout the term of the loan agreement, the monthly payment does not change.
	 * Interest and principal are included in the monthly payment.
	 * </p>
	 *
	 * @param monthlyPayment the monthly loan payment including interest and principal
	 * @param periodInMonths the term in months chosen by the customer to pay off the loan
	 * @return BigDecimal The total amount sum of monthly payments. It includes only the interest and principal.
	 */
	BigDecimal calculateTotalMonthlyPayment(BigDecimal monthlyPayment, int periodInMonths);

	/**
	 * Calculates the monthly payment that the customer must pay.
	 * <p>
	 * The monthly payment is calculated with the amortization loan formula.
	 * </p>
	 *
	 * @param loanAmount       the amount of the loan the user is applying for
	 * @param loanInterestRate the loan interest rate determined by the bank depending on the type of loan
	 * @param periodInMonths   the term in months chosen by the customer to pay off the loan
	 * @return BigDecimal monthly payment for the loan
	 */
	BigDecimal calculateMonthlyPayment(BigDecimal loanAmount, BigDecimal loanInterestRate, int periodInMonths);

	/**
	 * Calculate amortization of a loan
	 * <p>
	 * Amortization refers to the process of paying off debt over time in regular installments of interest and
	 * principal sufficient to pay off the loan in full by its maturity date. A higher percentage of the fixed
	 * monthly payment goes toward interest at the beginning of the loan, but with each subsequent payment,
	 * a larger percentage of it goes toward the loan principal.
	 * </p>
	 * The formula for amortization:
	 * (loanAmount*interestRate/12*Math.pow((1+interestRate/12),(12*periodInMonths))/(Math.pow((1+interestRate/12),(12*periodInMonths))-1));
	 *
	 * @param loanAmount          the amount of the loan the user is applying for
	 * @param monthlyInterestRate the monthly interest rate
	 * @param periodInMonths      the term of the loan
	 * @return BigDecimal the monthly payment.
	 */
	BigDecimal amortizedLoanFormula(BigDecimal loanAmount, BigDecimal monthlyInterestRate, int periodInMonths);

	/**
	 * Checks the customer salary
	 * <p>
	 * if 40% of the client's salary manages to cover the monthly loan installment.
	 * </p>
	 *
	 * @param request {@link LoanApplyRequest}
	 */
	boolean checkSalary(LoanApplyRequest request);

	/**
	 * Write the loan information needed by the user
	 * @param loanAmount the amount of the loan the user is applying for
	 * @param periodInMonths the term of the loan
	 * @param loanInterestRate the loan interest rate determined by the bank depending on the type of loan
	 * @param loanType the type of the loan
	 * @return {@link LoanEntity} the information about the loan after calculations
	 */
	LoanEntity getLoanInformation(BigDecimal loanAmount, int periodInMonths,
	                              BigDecimal loanInterestRate, LoanTypeEntity loanType);
}
