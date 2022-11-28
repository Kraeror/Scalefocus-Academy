package com.example.banksystem.models.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;
/** Represents a transaction.
 * @version 1.0
 * @since 1.0
 */

@Entity
@Table(name = "transactions")
public class TransactionEntity extends BaseEntity {

    @Column(name = "UUid",columnDefinition = "VARCHAR", nullable = false)
    private UUID UUid;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Column(name = "type")
    private String type;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private CheckingAccountEntity accountEntity;

    public TransactionEntity() {
    }

    public TransactionEntity(UUID UUid, BigDecimal amount, String reason, String type, CheckingAccountEntity accountEntity) {
        this.UUid = UUid;
        this.amount = amount;
        this.reason = reason;
        this.type = type;
        this.accountEntity = accountEntity;
    }

    /** Gets the transaction's unique UUID.
     * @return A UUID representing the transaction's unique UUID.
     */
    public UUID getUUid() {
        return UUid;
    }


    /** Returns {@link TransactionEntity} with UUID set.
     * @param UUid A UUid containing transaction's unique UUID.
     */
    public TransactionEntity setUUid(UUID UUid) {
        this.UUid = UUid;
        return this;
    }

    /** Gets the transaction's amount.
     * @return A BigDecimal representing the transaction's amount.
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /** Returns {@link TransactionEntity} with amount set.
     * @param amount A BigDecimal containing transaction's amount.
     */
    public TransactionEntity setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    /** Gets the transaction's reason.
     * @return A String representing the transaction's reason.
     */
    public String getReason() {
        return reason;
    }

    /** Returns {@link TransactionEntity} with reason set.
     * @param reason A String containing transaction's reason.
     */
    public TransactionEntity setReason(String reason) {
        this.reason = reason;
        return this;
    }

    /** Gets the transaction's type.
     * @return A String representing the transaction's type.
     */
    public String getType() {
        return type;
    }

    /** Returns {@link TransactionEntity} with type set.
     * @param type A String containing transaction's type.
     */
    public TransactionEntity setType(String type) {
        this.type = type;
        return this;
    }

    /** Gets the transaction's account that the money is transferred from.
     * @return A {@link AccountEntity} representing the account that the money is transferred from.
     */
    public CheckingAccountEntity getAccountEntity() {
        return accountEntity;
    }

    /** Returns {@link TransactionEntity} with the account that the money is transferred from set.
     * @param accountEntity A {@link AccountEntity} containing the account that the money is transferred from..
     */
    public TransactionEntity setAccountEntity(CheckingAccountEntity accountEntity) {
        this.accountEntity = accountEntity;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionEntity that = (TransactionEntity) o;
        return Objects.equals(amount, that.amount) && Objects.equals(reason, that.reason) && Objects.equals(type, that.type) && Objects.equals(accountEntity, that.accountEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, reason, type, accountEntity);
    }
}
