package com.expense_tracker.subscription.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import com.expense_tracker.subscription.entity.Subscription;
import com.expense_tracker.subscription.enums.BillingCycle;
import com.expense_tracker.subscription.enums.SubscriptionStatus;
import com.expense_tracker.user.entity.UserInfo;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class UserSubscriptionDTO {

	private Long id;

	private UserInfo userInfo;

	private Subscription subscription;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
	private ZonedDateTime startDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
	private ZonedDateTime endDate;

	private ZonedDateTime renewalDate;

	private BigDecimal amount;

	private BillingCycle billingCycle;

	private SubscriptionStatus status;

}
