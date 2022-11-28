package com.example.banksystem.models.responses;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class CheckingAccountResponse {

    private Long id;

    private String iban;

    private BigDecimal balance;

    private String userFullName;

    private String type;
    DecimalFormat df = new DecimalFormat("#,###.00");

    public CheckingAccountResponse() {
    }

    public Long getId() {
        return id;
    }

    public CheckingAccountResponse setId(Long id) {
        this.id = id;
        return this;
    }

    public String getIban() {
        return iban;
    }

    public CheckingAccountResponse setIban(String iban) {
        this.iban = iban;
        return this;
    }

    public String getBalance() {
        return df.format(balance);
    }

    public CheckingAccountResponse setBalance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public CheckingAccountResponse setUserFullName(String userFullName) {
        this.userFullName = userFullName;
        return this;
    }

    public String getType() {
        return type;
    }

    public CheckingAccountResponse setType(String type) {
        this.type = type;
        return this;
    }
}
