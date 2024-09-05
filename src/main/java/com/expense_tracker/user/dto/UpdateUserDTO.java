package com.expense_tracker.user.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/* @Data
 * Generate getters and setters (publi1c == non final) , @ToString(methode
 * toString()), @EqualsAndHashCode(methods equals() && hashCode()), @
 * RequiredArgsConstructor(constructor with final fields or annonated @NonNul)
 */
@Data
@NoArgsConstructor // Generate empty constructor, needed in springboot hibernate and JPA
// @AllArgsConstructor Generate constructor with each parameters
public class UpdateUserDTO {

	@NotNull(message = "Name is required.")
	@Size(min = 3, max = 20)
	private String name;

	@NotNull(message = "Email is required.")
	@Email
	private String email;

	@NotNull(message = "Username is required.")
	@Column(unique = true)
	@Size(min = 3, max = 20)
	private String username;

}
