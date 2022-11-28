package com.example.banksystem.models.requsts;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class LoanApplyRequest {
    @NotBlank(message = "Type cannot be blank.")
    private String loanType;

    @NotNull(message = "Please enter amount!")
    @Min(value = 1000, message = "Minimum loan amount is 1000")
    @Max(value = 500000, message = "Maximum loan amount is 500000")
    private BigDecimal loanAmount;

    @NotNull(message = "Please enter months!")
    @Min(value = 12, message = "Minimum period in months is 12")
    @Max(value = 360, message = "Maximum period in months is 360")
    private int period;

    @NotNull(message = "Please enter months!")
    private BigDecimal salary;

    public String getLoanType() {
        return loanType;
    }

    public LoanApplyRequest setLoanType(String loanType) {
        this.loanType = loanType;
        return this;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public LoanApplyRequest setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
        return this;
    }

    public int getPeriod() {
        return period;
    }

    public LoanApplyRequest setPeriod(int period) {
        this.period = period;
        return this;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public LoanApplyRequest setSalary(BigDecimal salary) {
        this.salary = salary;
        return this;
    }
}
