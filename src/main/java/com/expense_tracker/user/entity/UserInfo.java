package com.expense_tracker.user.entity;

import java.util.List;

import com.expense_tracker.common.entity.BaseEntity;
import com.expense_tracker.subscription.entity.UserSubscription;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class UserInfo extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Size(min = 3, max = 20)
	private String name;

	@NotNull
	@Column(unique = true)
	@Size(min = 3, max = 20)
	private String username;

	@NotNull
	@Column(unique = true)
	@Email
	private String email;

	@NotNull
	@Size(min = 6, max = 100)
	private String password;

	@NotNull
	// Enum
	private String roles;

	@OneToMany(mappedBy = "user_info")
	private List<UserSubscription> userSubscriptions;

}
