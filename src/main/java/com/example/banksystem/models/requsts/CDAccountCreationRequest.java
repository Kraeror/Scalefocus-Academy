package com.example.banksystem.models.requsts;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CDAccountCreationRequest {
    @Min(value = 500, message = "Initial amount cannot be less than 500!")
    @NotNull
    private BigDecimal amount;

    private Integer periodInYears;

    public BigDecimal getAmount() {
        return amount;
    }

    public CDAccountCreationRequest setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public Integer getPeriodInYears() {
        return periodInYears;
    }

    public CDAccountCreationRequest setPeriod(Integer period) {
        this.periodInYears = period;
        return this;
    }
}
