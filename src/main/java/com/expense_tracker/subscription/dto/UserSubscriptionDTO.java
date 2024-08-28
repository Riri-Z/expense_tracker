package com.expense_tracker.subscription.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.expense_tracker.subscription.enums.BillingCycle;
import com.expense_tracker.subscription.enums.SubscriptionStatus;

import lombok.Data;

@Data
public class UserSubscriptionDTO {

	private Long id;

	private Long userId;

	private Long subscriptionId;

	private LocalDate startDate;

	private LocalDate endDate;

	private LocalDate renewalDate;

	private BigDecimal amount;

	private BillingCycle billingCycle;

	private SubscriptionStatus status;

}
