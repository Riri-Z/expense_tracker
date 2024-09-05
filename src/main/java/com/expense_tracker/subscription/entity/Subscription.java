package com.expense_tracker.subscription.entity;

import java.util.List;

import com.expense_tracker.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = "userSubscription")
public class Subscription extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@NotNull(message = "Name is required.")
	@NotBlank
	String name;

	// @JsonBackReference is used on the one that will be omitted from serialization
	@JsonBackReference
	@OneToMany(mappedBy = "subscription")
	private List<UserSubscription> userSubscription;

}
