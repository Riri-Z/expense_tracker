package com.expense_tracker.user;

public class UserInfoDTO {

	private long id;

	private String name;

	private String username;

	private String email;

	private String roles;

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
