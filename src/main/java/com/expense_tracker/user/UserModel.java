package com.expense_tracker.user;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserModel {

	   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

	public UserModel() {
	}

	public UserModel(Long id, String username, String password, String firstName, String lastName, LocalDate dateOfBirth, OffsetDateTime createdAt) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.createdAt = createdAt;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDate getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public OffsetDateTime getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(OffsetDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public UserModel id(Long id) {
		setId(id);
		return this;
	}

	public UserModel username(String username) {
		setUsername(username);
		return this;
	}

	public UserModel password(String password) {
		setPassword(password);
		return this;
	}

	public UserModel firstName(String firstName) {
		setFirstName(firstName);
		return this;
	}

	public UserModel lastName(String lastName) {
		setLastName(lastName);
		return this;
	}

	public UserModel dateOfBirth(LocalDate dateOfBirth) {
		setDateOfBirth(dateOfBirth);
		return this;
	}

	public UserModel createdAt(OffsetDateTime createdAt) {
		setCreatedAt(createdAt);
		return this;
	}

	@Override
	public String toString() {
		return "{" +
			" id='" + getId() + "'" +
			", username='" + getUsername() + "'" +
			", password='" + getPassword() + "'" +
			", firstName='" + getFirstName() + "'" +
			", lastName='" + getLastName() + "'" +
			", dateOfBirth='" + getDateOfBirth() + "'" +
			", createdAt='" + getCreatedAt() + "'" +
			"}";
	}

}
