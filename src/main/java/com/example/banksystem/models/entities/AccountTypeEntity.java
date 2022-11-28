package com.example.banksystem.models.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
/** Represents an account type.
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "account_types")
public class AccountTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal transactionFee;

    private BigDecimal monthlyFee;

    private String type;

    @OneToMany(mappedBy="type")
    private List<CheckingAccountEntity> checkingAccounts;

    @OneToMany(mappedBy = "type")
    private List<CDAccountEntity> cdAccounts;

    public AccountTypeEntity() {
    }

    /** Gets the account type's id.
     * @return A Long representing the account type's id.
     */
    public Long getId() {
        return id;
    }

    /** Returns {@link AccountTypeEntity} with id set.
     * @param id A Long containing account type's id.
     */
    public AccountTypeEntity setId(Long id) {
        this.id = id;
        return this;
    }

    /** Gets the account type's name.
     * @return A String representing the account type's name.
     */
    public String getType() {
        return type;
    }

    /** Returns {@link AccountTypeEntity} with type's name set.
     * @param type A String containing account type's name.
     */
    public AccountTypeEntity setType(String type) {
        this.type = type;
        return this;
    }

    public BigDecimal getTransactionFee() {
        return transactionFee;
    }

    public AccountTypeEntity setTransactionFee(BigDecimal transactionFee) {
        this.transactionFee = transactionFee;
        return this;
    }

    public BigDecimal getMonthlyFee() {
        return monthlyFee;
    }

    public AccountTypeEntity setMonthlyFee(BigDecimal monthlyFee) {
        this.monthlyFee = monthlyFee;
        return this;
    }
}
