package com.expense_tracker.password.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordResetDTO {

	@NotBlank(message = "Email is mandatory")
	@Email(message = "Invalide email format")
	private String email;

	@NotBlank
	@Size(min = 3, max = 20)
	private String newPassword;

	@NotBlank
	@Size(min = 3, max = 20)
	private String confirmPassword;

}
