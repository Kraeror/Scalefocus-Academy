package com.example.banksystem.models.entities;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
/** Represents a base from which we'll get the id for every entity generated after it extends it.
 * @version 1.0
 * @since 1.0
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;


    public BaseEntity() {
        this.createdOn = LocalDateTime.now();
    }

    /** Gets the base entity's id.
     * @return A Long representing the account type's id.
     */
    public Long getId() {
        return id;
    }

    /** Returns {@link BaseEntity} with id set.
     * @param id A Long containing base entity's id .
     */
    public BaseEntity setId(Long id) {
        this.id = id;
        return this;
    }

    /** Gets the base entity's date that it is created on.
     * @return A LocalDateTime representing the base entity's date that it is created on.
     */
    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    /** Returns {@link BaseEntity} with date that it is created on set.
     * @param createdOn A LocalDateTime containing base entity's date that it is created on .
     */
    public BaseEntity setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }
}
