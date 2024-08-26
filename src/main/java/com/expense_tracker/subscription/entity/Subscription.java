package com.expense_tracker.subscription.entity;

import java.util.List;

import com.expense_tracker.common.entity.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Subscription extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@NotNull
	@NotBlank
	String name;

	String description;

	@OneToMany(mappedBy = "subscription")
	private List<UserSubscription> userSubscriptions;

}
