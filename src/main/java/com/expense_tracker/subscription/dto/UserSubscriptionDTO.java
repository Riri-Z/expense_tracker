package com.expense_tracker.subscription.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import com.expense_tracker.subscription.entity.Subscription;
import com.expense_tracker.subscription.enums.BillingCycle;
import com.expense_tracker.subscription.enums.SubscriptionStatus;
import com.expense_tracker.user.entity.UserInfo;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserSubscriptionDTO {

	private Long id;

	private UserInfo userInfo;

	private Subscription subscription;

	@NotNull(message = "Start date is required.")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
	private ZonedDateTime startDate;

	@NotNull(message = "End date is required.")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
	private ZonedDateTime endDate;

	@NotNull(message = "Renewal date is required.")
	private ZonedDateTime renewalDate;

	@NotNull(message = "Amount is required.")
	private BigDecimal amount;

	@NotNull(message = "Billing cycle is required.")
	private BillingCycle billingCycle;

	@NotNull(message = "Status is required.")
	private SubscriptionStatus status;

}
