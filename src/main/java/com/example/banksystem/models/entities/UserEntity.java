package com.example.banksystem.models.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a user.
 *
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false, name = "full_name")
	private String fullName;

	@Column(name = "phone_number")
	private String phoneNumber;

	/**
	 * Represents the user's authorization roles .
	 */
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "users_roles",
					joinColumns = @JoinColumn(name = "user_id"),
					inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<RoleEntity> roles;

	/**
	 * Represents the user's checking accounts .
	 */
	@OneToMany(mappedBy = "user")
	private List<CheckingAccountEntity> checkingAccounts;

	/**
	 * Represents the user's certification of deposit accounts .
	 */
	@OneToMany(mappedBy = "user")
	private List<CDAccountEntity> cdAccounts;

	@Column(name = "has_loan", nullable = false, columnDefinition = "boolean default false")
	private boolean hasLoan;

	public List<CheckingAccountEntity> getCheckingAccounts() {
		return checkingAccounts;
	}

	public UserEntity setCheckingAccounts(List<CheckingAccountEntity> checkingAccounts) {
		this.checkingAccounts = checkingAccounts;
		return this;
	}

	public List<CDAccountEntity> getCdAccounts() {
		return cdAccounts;
	}

	public UserEntity setCdAccounts(List<CDAccountEntity> cdAccounts) {
		this.cdAccounts = cdAccounts;
		return this;
	}

	public UserEntity() {
		this.roles = new HashSet<>();
	}

	/**
	 * Gets the user's username.
	 *
	 * @return A String representing the user's username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Returns {@link UserEntity} with username set.
	 *
	 * @param username A String containing user's username.
	 */
	public UserEntity setUsername(String username) {
		this.username = username;
		return this;
	}

	/**
	 * Gets the user's password.
	 *
	 * @return A String representing the user's password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Returns {@link UserEntity} with password set.
	 *
	 * @param password A String containing user's password.
	 */
	public UserEntity setPassword(String password) {
		this.password = password;
		return this;
	}

	/**
	 * Gets the user's email.
	 *
	 * @return A String representing the user's email.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Returns {@link UserEntity} with email set.
	 *
	 * @param email A String containing user's email.
	 */
	public UserEntity setEmail(String email) {
		this.email = email;
		return this;
	}

	/**
	 * Gets the user's full name.
	 *
	 * @return A String representing the user's full name.
	 */
	public String getFullName() {
		return fullName;
	}


	/**
	 * Returns {@link UserEntity} with full name set.
	 *
	 * @param fullName A String containing user's full name.
	 */
	public UserEntity setFullName(String fullName) {
		this.fullName = fullName;
		return this;
	}

	/**
	 * Gets the user's phone number.
	 *
	 * @return A String representing the user's phone number.
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}


	/**
	 * Returns {@link UserEntity} with phone number set.
	 *
	 * @param phoneNumber A String containing user's phone number.
	 */
	public UserEntity setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		return this;
	}

	/**
	 * Gets the user's authorization roles.
	 *
	 * @return A Set representing the user's authorization roles.
	 */
	public Set<RoleEntity> getRoles() {
		return roles;
	}

	public boolean hasLoan() {
		return hasLoan;
	}

	public UserEntity setHasLoan(boolean hasLoan) {
		this.hasLoan = hasLoan;
		return this;
	}

	/**
	 * Returns {@link UserEntity} with authorization roles set.
	 *
	 * @param roles A Set containing user's authorization roles.
	 */
	public UserEntity setRoles(Set<RoleEntity> roles) {
		this.roles = roles;
		return this;
	}

	/**
	 * Adds an {@link RoleEntity} to the Set of the user's authorization roles.
	 *
	 * @param role an {@link RoleEntity} containing an authorization role.
	 */
	public void addRole(RoleEntity role) {
		this.roles.add(role);
	}
}
