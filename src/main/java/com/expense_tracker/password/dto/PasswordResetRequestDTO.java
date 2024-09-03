package com.expense_tracker.password.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordResetRequestDTO {

	@NotBlank(message = "Email is mandatory")
	@Email(message = "Invalide email format")
	private String email;

}
