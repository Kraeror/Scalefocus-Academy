package com.example.banksystem.models.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

/** Represents an account.
 * @version 1.0
 * @since 1.0
 */
@MappedSuperclass
//@Entity
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AccountEntity extends BaseEntity{
    @Column(nullable = false,unique = true)
    private String iban;

    private BigDecimal balance;

    /** Represents the account’s owner .
     */
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private UserEntity user;

    /** Represents the type of the account.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    private AccountTypeEntity type;

//    public AccountEntity() {
//    }

    /** Gets the account's iban.
     * @return A String representing the account's iban.
     */
    public String getIban() {
        return iban;
    }

    /**
     * Returns {@link AccountEntity} with iban set.
     *
     * @param iban A String containing account's iban.
     */
    public void setIban(String iban) {
        this.iban = iban;
    }

    /** Gets the account's balance.
     * @return A {@link BigDecimal} representing the account's balance.
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /** Returns {@link AccountEntity} with balance set.
     * @param balance A {@link BigDecimal} containing the account's balance.
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /** Gets the account's owner.
     * @return A {@link UserEntity} representing the account's balance.
     */
    public UserEntity getUser() {
        return user;
    }

    /** Returns {@link AccountEntity} with account's owner set.
     * @param user A {@link UserEntity}  containing the account's owner.
     */
    public void setUser(UserEntity user) {
        this.user = user;
    }

    /** Gets the account's type.
     * @return An {@link AccountTypeEntity} representing account's type.
     */
    public AccountTypeEntity getType() {
        return type;
    }

    /** Returns {@link AccountEntity} with the account's type set.
     * @param type An {@link AccountTypeEntity} containing account's type.
     */
    public void setType(AccountTypeEntity type) {
        this.type = type;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountEntity that = (AccountEntity) o;
        return Objects.equals(iban, that.iban) && Objects.equals(balance, that.balance) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iban, balance, user, type);
    }
}