package com.example.banksystem.models.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents a loan type.
 *
 * @version 1.2.0
 * @since 1.1.0
 */
@Entity
@Table(name = "loans")
public class LoanEntity extends BaseEntity {
	/**
	 * Represents the account to which the loan will be added
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "account_id", nullable = false)
	private CheckingAccountEntity account;

	/**
	 * Represents the type of the loan.
	 */
	@OneToOne
	@JoinColumn(name = "type_id", nullable = false)
	private LoanTypeEntity loanType;

	/**
	 * Represents the starting loan amount
	 */
	@Column(name = "beginning_loan_amount", nullable = false)
	private BigDecimal beginningLoanAmount;

	/**
	 * Represents the difference between the beginningLoanAmount and the principal portion.
	 */
	@Column(name = "remaining_loan_amount", nullable = false)
	private BigDecimal remainingLoanAmount;

	/**
	 * Represents the date on which the loan repayment installment must be paid.
	 */
	@Column(name = "maturity_date", nullable = false)
	private LocalDate maturityDate;

	/**
	 * Represents the sum of the monthly interest rate and the remaining that goes to reduce the principal.
	 */
	@Column(name = "monthly_payment", nullable = false)
	private BigDecimal monthlyPayment;

	/**
	 * Represents the total amount due at the end of the loan including fees and interests.
	 */
	@Column(name = "total_amount_sum", nullable = false)
	private BigDecimal totalAmountSum;

	/**
	 * Indicates whether the loan is approved against the user's income
	 */
	@Column(name = "approved", nullable = false)
	private boolean approved;

	/**
	 * Represents the end date of a loan.
	 */
	@Column(name = "due_date", nullable = false)
	private LocalDate dueDate;

	/**
	 * Represents the period of the loan in months
	 */
	@Column(name = "period_in_months", nullable = false)
	private int periodInMonths;

	public LoanEntity() {
		this.setMaturityDate(super.getCreatedOn().toLocalDate().plusMonths(1));
		this.setDueDate(super.getCreatedOn().toLocalDate().plusMonths(this.getPeriodInMonths()));
	}

	public CheckingAccountEntity getAccount() {
		return account;
	}

	public LoanEntity setAccount(CheckingAccountEntity account) {
		this.account = account;
		return this;
	}

	public LoanTypeEntity getLoanType() {
		return loanType;
	}

	public LoanEntity setLoanType(LoanTypeEntity loanType) {
		this.loanType = loanType;
		return this;
	}

	public BigDecimal getBeginningLoanAmount() {
		return beginningLoanAmount;
	}

	public LoanEntity setBeginningLoanAmount(BigDecimal beginningLoanAmount) {
		this.beginningLoanAmount = beginningLoanAmount;
		return this;
	}

	public BigDecimal getRemainingLoanAmount() {
		return remainingLoanAmount;
	}

	public LoanEntity setRemainingLoanAmount(BigDecimal remainingLoanAmount) {
		this.remainingLoanAmount = remainingLoanAmount;
		return this;
	}

	public LocalDate getMaturityDate() {
		return maturityDate;
	}

	public LoanEntity setMaturityDate(LocalDate maturityDate) {
		this.maturityDate = maturityDate;
		return this;
	}

	public LoanEntity setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
		return this;
	}

	public BigDecimal getMonthlyPayment() {
		return monthlyPayment;
	}

	public LoanEntity setMonthlyPayment(BigDecimal monthlyPayment) {
		this.monthlyPayment = monthlyPayment;
		return this;
	}

	public BigDecimal getTotalAmountSum() {
		return totalAmountSum;
	}

	public LoanEntity setTotalAmountSum(BigDecimal totalAmountSum) {
		this.totalAmountSum = totalAmountSum;
		return this;
	}

	public boolean isApproved() {
		return approved;
	}

	public LoanEntity setApproved(boolean approved) {
		this.approved = approved;
		return this;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public int getPeriodInMonths() {
		return periodInMonths;
	}

	public LoanEntity setPeriodInMonths(int periodInMonths) {
		this.periodInMonths = periodInMonths;
		return this;
	}
}
