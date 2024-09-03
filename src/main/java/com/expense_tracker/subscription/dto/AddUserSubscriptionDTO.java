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
public class AddUserSubscriptionDTO {

	private UserInfo userInfo;

	private Subscription subscription;

	private BigDecimal amount;

	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
	private ZonedDateTime startDate;

	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
	private ZonedDateTime endDate;

	@NotNull
	private ZonedDateTime renewalDate;

	@NotNull
	private BillingCycle billingCycle;

	@NotNull
	private SubscriptionStatus status;

}
