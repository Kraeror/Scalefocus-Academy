package com.example.banksystem.models.responses;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionResponse {
    private Long id;
    private UUID uuid;
    private String iban;
    private LocalDateTime createdOn;
    private BigDecimal amount;
    private String type;
    private String userName;
    DecimalFormat df = new DecimalFormat("#,###.00");

    public TransactionResponse() {
    }

    public TransactionResponse(UUID uuid, String iban, LocalDateTime createdOn, BigDecimal amount, String type, String userName) {
        this.uuid = uuid;
        this.iban = iban;
        this.createdOn = createdOn;
        this.amount = amount;
        this.type = type;
        this.userName = userName;
    }

    public Long getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public TransactionResponse setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getIban() {
        return iban;
    }

    public TransactionResponse setIban(String iban) {
        this.iban = iban;
        return this;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public TransactionResponse setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public String getAmount() {
        return df.format(amount);
    }

    public TransactionResponse setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public String getType() {
        return type;
    }

    public TransactionResponse setType(String type) {
        this.type = type;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public TransactionResponse setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public TransactionResponse setId(Long id) {
        this.id = id;
        return this;
    }
}
