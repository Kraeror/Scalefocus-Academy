package com.example.banksystem.models.responses;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;

public class LoanResponse {
	private Long id;
	private CheckingAccountResponse account;
	private boolean isApproved;
	private BigDecimal beginningLoanAmount;
	private BigDecimal remainingLoanAmount;
	private BigDecimal monthlyPayment;
	private BigDecimal totalAmountSum;
	private LocalDate loanDueDate;
	DecimalFormat df = new DecimalFormat("#,###.00");

	public Long getId() {
		return id;
	}

	public LoanResponse setId(Long id) {
		this.id = id;
		return this;
	}

	public CheckingAccountResponse getAccount() {
		return account;
	}

	public void setAccount(CheckingAccountResponse accountResponse) {
		this.account = accountResponse;
	}

	public String getBeginningLoanAmount() {
		return df.format(beginningLoanAmount);
	}

	public LoanResponse setBeginningLoanAmount(BigDecimal beginningLoanAmount) {
		this.beginningLoanAmount = beginningLoanAmount;
		return this;
	}

	public String getRemainingLoanAmount() {
		return df.format(remainingLoanAmount);
	}

	public LoanResponse setRemainingLoanAmount(BigDecimal remainingLoanAmount) {
		this.remainingLoanAmount = remainingLoanAmount;
		return this;
	}

	public String getMonthlyPayment() {
		return df.format(monthlyPayment);
	}

	public LoanResponse setMonthlyPayment(BigDecimal monthlyPayment) {
		this.monthlyPayment = monthlyPayment;
		return this;
	}

	public String getTotalAmountSum() {
		return df.format(totalAmountSum);
	}

	public LoanResponse setTotalAmountSum(BigDecimal totalAmountSum) {
		this.totalAmountSum = totalAmountSum;
		return this;
	}

	public LocalDate getLoanDueDate() {
		return loanDueDate;
	}

	public LoanResponse setLoanDueDate(LocalDate loanDueDate) {
		this.loanDueDate = loanDueDate;
		return this;
	}

	public boolean isApproved() {
		return isApproved;
	}

	public LoanResponse setApproved(boolean approved) {
		isApproved = approved;
		return this;
	}
}
