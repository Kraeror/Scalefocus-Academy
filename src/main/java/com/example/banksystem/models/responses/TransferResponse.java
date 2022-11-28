package com.example.banksystem.models.responses;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

public class TransferResponse {
    private Long id;
    private String senderIBAN;
    private String receiverIBAN;
    private LocalDateTime createdOn;
    private BigDecimal amount;
    private String reason;
    private String type;
    private String userName;
    DecimalFormat df = new DecimalFormat("#,###.00");

    public TransferResponse() {
    }

    public TransferResponse(String senderIBAN, String receiverIBAN, LocalDateTime createdOn, BigDecimal amount,
                            String reason, String type, String userName) {
        this.senderIBAN = senderIBAN;
        this.receiverIBAN = receiverIBAN;
        this.createdOn = createdOn;
        this.amount = amount;
        this.reason = reason;
        this.type = type;
        this.userName = userName;
    }

    public long getId() {
        return id;
    }

    public TransferResponse setId(Long id) {
        this.id = id;
        return this;
    }

    public String getSenderIBAN() {
        return senderIBAN;
    }

    public TransferResponse setSenderIBAN(String senderIBAN) {
        this.senderIBAN = senderIBAN;
        return this;
    }

    public String getReceiverIBAN() {
        return receiverIBAN;
    }

    public TransferResponse setReceiverIBAN(String receiverIBAN) {
        this.receiverIBAN = receiverIBAN;
        return this;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public TransferResponse setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public String getAmount() {
        return df.format(amount);
    }

    public TransferResponse setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public String getReason() {
        return reason;
    }

    public TransferResponse setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public String getType() {
        return type;
    }

    public TransferResponse setType(String type) {
        this.type = type;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public TransferResponse setUserName(String userName) {
        this.userName = userName;
        return this;
    }
}
