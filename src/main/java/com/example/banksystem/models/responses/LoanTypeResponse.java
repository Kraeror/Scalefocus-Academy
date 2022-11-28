package com.example.banksystem.models.responses;

import java.math.BigDecimal;

public class LoanTypeResponse {
    private Long id;

    private BigDecimal considerationFee;

    private BigDecimal interestRate;

    private BigDecimal monthlyFee;

    private String name;

    public Long getId() {
        return id;
    }

    public LoanTypeResponse setId(Long id) {
        this.id = id;
        return this;
    }

    public BigDecimal getConsiderationFee() {
        return considerationFee;
    }

    public LoanTypeResponse setConsiderationFee(BigDecimal considerationFee) {
        this.considerationFee = considerationFee;
        return this;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public LoanTypeResponse setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
        return this;
    }

    public BigDecimal getMonthlyFee() {
        return monthlyFee;
    }

    public LoanTypeResponse setMonthlyFee(BigDecimal monthlyFee) {
        this.monthlyFee = monthlyFee;
        return this;
    }

    public String getName() {
        return name;
    }

    public LoanTypeResponse setName(String name) {
        this.name = name;
        return this;
    }
}
