package com.expense_tracker.user.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Generate getters and setters (public)
@NoArgsConstructor // Generate empty constructor, needed in springboot hibernate and JPA
@AllArgsConstructor // Generate constructor with each parameters
public class UpdateUserDTO {

	@NotNull
	@Size(min = 3, max = 20)
	private String name;

	@NotNull
	@Email
	private String email;

	@NotNull
	@Column(unique = true)
	@Size(min = 3, max = 20)
	private String username;

}
