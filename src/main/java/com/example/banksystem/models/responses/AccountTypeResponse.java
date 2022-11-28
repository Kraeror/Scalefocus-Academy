package com.example.banksystem.models.responses;

import java.math.BigDecimal;

public class AccountTypeResponse {

    private Long id;
    private BigDecimal transactionFee;
    private BigDecimal monthlyFee;
    private String type;

    public AccountTypeResponse() {
    }

    public Long getId() {
        return id;
    }

    public AccountTypeResponse setId(Long id) {
        this.id = id;
        return this;
    }

    public BigDecimal getTransactionFee() {
        return transactionFee;
    }

    public AccountTypeResponse setTransactionFee(BigDecimal transactionFee) {
        this.transactionFee = transactionFee;
        return this;
    }

    public BigDecimal getMonthlyFee() {
        return monthlyFee;
    }

    public AccountTypeResponse setMonthlyFee(BigDecimal monthlyFee) {
        this.monthlyFee = monthlyFee;
        return this;
    }

    public String getType() {
        return type;
    }

    public AccountTypeResponse setType(String type) {
        this.type = type;
        return this;
    }
}
