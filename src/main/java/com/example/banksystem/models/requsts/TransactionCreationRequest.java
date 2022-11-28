package com.example.banksystem.models.requsts;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class TransactionCreationRequest {

	@NotBlank(message = "IBAN field cannot be empty")
	private String iban;
	@Min(value = 10, message = "Amount cannot be less than 10")
	@NotNull
	private BigDecimal amount;
	@NotBlank(message = "Reason cannot be empty")
	private String reason;

	public String getIban() {
		return iban;
	}

	public TransactionCreationRequest setIban(String iban) {
		this.iban = iban;
		return this;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public TransactionCreationRequest setAmount(BigDecimal amount) {
		this.amount = amount;
		return this;
	}

	public String getReason() {
		return reason;
	}

	public TransactionCreationRequest setReason(String reason) {
		this.reason = reason;
		return this;
	}
}