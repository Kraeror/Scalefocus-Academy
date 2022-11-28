package com.example.banksystem.models.responses;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class CalculateLoanResponse {
	private LoanTypeResponse loanType;
	private BigDecimal beginningLoanAmount;
	private int periodInMonths;
	private BigDecimal monthlyPayment;
	private BigDecimal totalAmountSum;
	DecimalFormat df = new DecimalFormat("#,###.00");

	public String getBeginningLoanAmount() {
		return df.format(beginningLoanAmount);
	}

	public CalculateLoanResponse setBeginningLoanAmount(BigDecimal beginningLoanAmount) {
		this.beginningLoanAmount = beginningLoanAmount;
		return this;
	}

	public int getPeriodInMonths() {
		return periodInMonths;
	}

	public CalculateLoanResponse setPeriodInMonths(int periodInMonths) {
		this.periodInMonths = periodInMonths;
		return this;
	}

	public String getMonthlyPayment() {
		return df.format(monthlyPayment);
	}

	public CalculateLoanResponse setMonthlyPayment(BigDecimal monthlyPayment) {
		this.monthlyPayment = monthlyPayment;
		return this;
	}

	public String getTotalAmountSum() {
		return df.format(totalAmountSum);
	}

	public CalculateLoanResponse setTotalAmountSum(BigDecimal totalAmountSum) {
		this.totalAmountSum = totalAmountSum;
		return this;
	}

	public LoanTypeResponse getLoanType() {
		return loanType;
	}

	public void setLoanType(LoanTypeResponse loanType) {
		this.loanType = loanType;
	}
}
