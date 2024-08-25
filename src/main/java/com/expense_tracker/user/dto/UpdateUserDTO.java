package com.expense_tracker.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Generate getters and setters (public)
@NoArgsConstructor // Generate empty constructor, needed in springboot hibernate and JPA
@AllArgsConstructor // Generate constructor with each parameters
public class UpdateUserDTO {

	private String name;

	private String email;

	private String username;

}
