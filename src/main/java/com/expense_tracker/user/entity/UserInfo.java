package com.expense_tracker.user.entity;

import java.util.List;

import com.expense_tracker.common.entity.BaseEntity;
import com.expense_tracker.subscription.entity.UserSubscription;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = "userSubscriptions")
public class UserInfo extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Name is required.")
	@Size(min = 3, max = 20)
	private String name;

	@NotNull(message = "Username is required.")
	@Column(unique = true)
	@Size(min = 3, max = 20)
	private String username;

	@NotNull(message = "email is required.")
	@Column(unique = true)
	@Email
	private String email;

	@NotNull(message = "password is required.")
	@JsonIgnore
	@Size(min = 6, max = 100)
	private String password;

	@NotNull(message = "roles is required.")
	// Enum
	private String roles;

	// here we could have manyToMany to subscription entity directly, but then we
	// would //have only fk_user_id and fk_subscription_id in the junction table so
	// we handle ourself the junction table with userSubscription entity
	@OneToMany(mappedBy = "userInfo")
	// prevent jackson to have an Infinite recursion
	// @JsonManagedReference is used on the one that got serialized normally
	@JsonManagedReference
	private List<UserSubscription> userSubscriptions;

}
