package com.expense_tracker.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CreateUserDTO extends UserInfoDTO {

	@NotBlank(message = "Password cannot be blank")
	@Size(min = 6, max = 100)
	private String password;

}
