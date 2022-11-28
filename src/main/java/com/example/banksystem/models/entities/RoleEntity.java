package com.example.banksystem.models.entities;

import javax.persistence.*;
/** Represents a user authorization role.
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "roles")
public class RoleEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role;

    public RoleEntity() {
    }

    public RoleEntity(String role) {
        this.role = role;
    }

    /** Gets the user authorization role's name.
     * @return A String representing the user authorization role.
     */
    public String getRole() {
        return role;
    }

    /** Returns {@link RoleEntity} with role's name set.
     * @param role A String containing the user authorization role's name .
     */
    public RoleEntity setRole(String role) {
        this.role = role;
        return this;
    }


}
