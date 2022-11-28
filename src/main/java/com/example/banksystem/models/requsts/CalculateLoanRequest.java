package com.example.banksystem.models.requsts;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CalculateLoanRequest {
	@NotBlank(message = "Loan type can not be blank. Possible options: Consumer, Mortgage")
	private String loanType;

	@NotNull(message = "Please enter amount!")
	@Min(value = 1000, message = "Minimum loan amount is 1000")
	@Max(value = 500000, message = "Maximum loan amount is 500000")
	private BigDecimal loanAmount;

	@NotNull
	@Min(value = 12, message = "Minimum period in months is 12")
	@Max(value = 360, message = "Maximum period in months is 360")
	private int period;

	public String getLoanType() {
		return loanType;
	}

	public CalculateLoanRequest setLoanType(String loanType) {
		this.loanType = loanType;
		return this;
	}

	public BigDecimal getLoanAmount() {
		return loanAmount;
	}

	public CalculateLoanRequest setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
		return this;
	}

	public int getPeriod() {
		return period;
	}

	public CalculateLoanRequest setPeriod(int period) {
		this.period = period;
		return this;
	}
}
