package com.expense_tracker.subscription.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.expense_tracker.common.entity.BaseEntity;
import com.expense_tracker.subscription.enums.BillingCycle;
import com.expense_tracker.subscription.enums.SubscriptionStatus;
import com.expense_tracker.user.entity.UserInfo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class UserSubscription extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "subscription_start_date", columnDefinition = "TIMESTAMP")
	private LocalDate startDate;

	@NotNull
	@Future
	@Column(name = "subscription_end_date", columnDefinition = "TIMESTAMP")
	private LocalDate endDate;

	@NotNull
	@Column(name = "renewal_date")
	private LocalDate renewalDate;

	@NotNull
	@Column(name = "amount")
	private BigDecimal amount;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "billing_cycle")
	private BillingCycle billingCycle; // 'monthly', 'yearly' etc.

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private SubscriptionStatus status; // 'active', 'cancelled', 'paused'

	@ManyToOne
	@JoinColumn(name = "user_info_id")
	private UserInfo userInfo;

	@ManyToOne
	@JoinColumn(name = "subscription_id")
	private Subscription subscription;

}
