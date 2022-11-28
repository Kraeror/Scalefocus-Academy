package com.example.banksystem.models.responses;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CDAccountResponse {

    private Long id;

    private String iban;

    private BigDecimal balance;

    private String fullName;

    private String type;

    private LocalDate createdOn;

    private Integer periodInYears;

    private BigDecimal interest;

    private LocalDate expirationDate;

    private BigDecimal outcomeAmount;
    DecimalFormat df = new DecimalFormat("#,###.00");

    public CDAccountResponse() {
    }

    public Long getId() {
        return id;
    }

    public CDAccountResponse setId(Long id) {
        this.id = id;
        return this;
    }

    public String getIban() {
        return iban;
    }

    public CDAccountResponse setIban(String iban) {
        this.iban = iban;
        return this;
    }

    public String getBalance() {
        return df.format(balance);
    }

    public CDAccountResponse setBalance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public CDAccountResponse setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getType() {
        return type;
    }

    public CDAccountResponse setType(String type) {
        this.type = type;
        return this;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public CDAccountResponse setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn.toLocalDate();
        return this;
    }

    public Integer getPeriodInYears() {
        return periodInYears;
    }

    public CDAccountResponse setPeriodInYears(Integer periodInYears) {
        this.periodInYears = periodInYears;
        return this;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public CDAccountResponse setInterest(BigDecimal interest) {
        this.interest = interest;
        return this;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public CDAccountResponse setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public String getOutcomeAmount() {
        return df.format(outcomeAmount);
    }

    public CDAccountResponse setOutcomeAmount(BigDecimal outcomeAmount) {
        this.outcomeAmount = outcomeAmount;
        return this;
    }
}
