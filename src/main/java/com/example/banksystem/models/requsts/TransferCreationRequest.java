package com.example.banksystem.models.requsts;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class TransferCreationRequest {
	@NotBlank(message = "IBAN from can not be blank!")
	private String accountFrom;
	@NotBlank(message = "IBAN to can not be blank!")
	private String accountTo;
	@NotBlank(message = "Reason can not be blank!")
	private String reason;
	@Min(value = 10, message = "Minimal transfer amount is 10!")
	private BigDecimal amount;

	public String getAccountFrom() {
		return accountFrom;
	}

	public TransferCreationRequest setAccountFrom(String accountFrom) {
		this.accountFrom = accountFrom;
		return this;
	}

	public String getAccountTo() {
		return accountTo;
	}

	public TransferCreationRequest setAccountTo(String accountTo) {
		this.accountTo = accountTo;
		return this;
	}

	public String getReason() {
		return reason;
	}

	public TransferCreationRequest setReason(String reason) {
		this.reason = reason;
		return this;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public TransferCreationRequest setAmount(BigDecimal amount) {
		this.amount = amount;
		return this;
	}
}
