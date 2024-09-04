package com.expense_tracker.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserInfoDTO {

	private Long id;

	@NotBlank(message = "Name cannot be blank")
	@Size(min = 3, max = 20)
	private String name;

	@NotBlank(message = "UserName cannot be blank")
	@Size(min = 3, max = 20)
	private String username;

	@NotBlank(message = "Email cannot be blank")
	@Email
	private String email;

	@NotBlank(message = "Roles cannot be blank")
	private String roles;

	public UserInfoDTO() {
	}

	public UserInfoDTO(long id, String name, String username, String email, String roles) {
		this.id = id;
		this.name = name;
		this.username = username;
		this.email = email;
		this.roles = roles;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

}
