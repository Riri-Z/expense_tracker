package com.expense_tracker.security;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

	@NotBlank(message = "Username is required")
	private String username;

	@NotBlank(message = "password is required")
	private String password;

}
