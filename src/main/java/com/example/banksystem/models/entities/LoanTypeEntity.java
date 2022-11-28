package com.example.banksystem.models.entities;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Represents a loan type.
 */
@Entity
@Table(name = "loan_types")
public class LoanTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "consideration_Fee",nullable = false)
    private BigDecimal considerationFee;

    @Column(name = "interest_rate",nullable = false)
    private BigDecimal interestRate;

    @Column(name = "monthly_fee",nullable = false)
    private BigDecimal monthlyFee;

    private String name;

    public LoanTypeEntity() {
    }

    /** Gets the loan type's id.
     * @return A Long representing the loan type's id.
     */
    public Long getId() {
        return id;
    }

    /** Returns {@link LoanTypeEntity} with id set.
     * @param id A Long containing loan type's id.
     */
    public LoanTypeEntity setId(Long id) {
        this.id = id;
        return this;
    }

    /** Gets the loan type's consideration fee.
     * @return A BigDecimal representing consideration fee.
     */
    public BigDecimal getConsiderationFee() {
        return considerationFee;
    }


    /** Returns {@link LoanTypeEntity} with consideration fee set.
     * @param considerationFee A BigDecimal containing consideration fee.
     */
    public LoanTypeEntity setConsiderationFee(BigDecimal considerationFee) {
        this.considerationFee = considerationFee;
        return this;
    }

    /** Gets the loan type's interest rate.
     * @return A BigDecimal representing interest rate.
     */
    public BigDecimal getInterestRate() {
        return interestRate;
    }

    /** Returns {@link LoanTypeEntity} with interest rate set.
     * @param interestRate A BigDecimal containing interest rate.
     */
    public LoanTypeEntity setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
        return this;
    }

    /** Gets the loan type's monthly fee.
     * @return A BigDecimal representing monthly fee.
     */
    public BigDecimal getMonthlyFee() {
        return monthlyFee;
    }

    /** Returns {@link LoanTypeEntity} with monthly fee set.
     * @param monthlyFee A BigDecimal containing monthly fee.
     */
    public LoanTypeEntity setMonthlyFee(BigDecimal monthlyFee) {
        this.monthlyFee = monthlyFee;
        return this;
    }

    /** Gets the loan type's name.
     * @return A String representing the account type's name.
     */
    public String getName() {
        return name;
    }

    /** Returns {@link LoanTypeEntity} with type's name set.
     * @param type A String containing loan type's name.
     */
    public LoanTypeEntity setName(String type) {
        this.name = type;
        return this;
    }
}
