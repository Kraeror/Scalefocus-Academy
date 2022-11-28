package com.example.banksystem.models.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cd_accounts")
public class CDAccountEntity extends AccountEntity{
    @Column(nullable = false)
    private Integer periodInYears;

    @Column(nullable = false)
    private BigDecimal interest;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Column(name = "outcome_amount", nullable = false)
    private BigDecimal outcomeAmount;

    /** Gets the account's lock period.
     * @return A String representing the account's lock period.
     */
    public Integer getPeriodInYears() {
        return periodInYears;
    }

    /**
     * Returns {@link CDAccountEntity} with period set.
     *
     * @param period An Integer containing account's lock period.
     */
    public CDAccountEntity setPeriod(Integer period) {
        this.periodInYears = period;
        return this;
    }

    /** Gets the account's interest.
     * @return A {@link BigDecimal} representing the account's interest.
     */
    public BigDecimal getInterest() {
        return interest;
    }

    /** Returns {@link CDAccountEntity} with interest set.
     * @param interest A {@link BigDecimal} containing the account's interest.
     */
    public CDAccountEntity setInterest(BigDecimal interest) {
        this.interest = interest;
        return this;
    }

    /** Gets the account's expiration date.
     * @return A {@link LocalDate} representing the account's expiration date.
     */
    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    /** Returns {@link CDAccountEntity} with expiration date set.
     * @param expirationDate A {@link LocalDate} containing the account's expiration date.
     */
    public CDAccountEntity setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    /** Gets the account's expected amount after end of the lock period.
     * @return A {@link BigDecimal} representing the account's expected amount after the lock period.
     */
    public BigDecimal getOutcomeAmount() {
        return outcomeAmount;
    }

    /** Returns {@link CDAccountEntity} with expected outcome amount set.
     * @param outcomeAmount A {@link BigDecimal} containing the account's expected amount outcome.
     */
    public CDAccountEntity setOutcomeAmount(BigDecimal outcomeAmount) {
        this.outcomeAmount = outcomeAmount;
        return this;
    }
}
