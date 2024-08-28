package com.expense_tracker.subscription.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.expense_tracker.subscription.enums.BillingCycle;
import com.expense_tracker.subscription.enums.SubscriptionStatus;

import lombok.Data;

@Data
public class AddUserSubscriptionDTO {

	private Long userId;

	private String subscriptionName;

	private String subscriptionDescription;

	private BigDecimal amount;

	private LocalDate startDate;

	private LocalDate endDate;

	private LocalDate renewalDate;

	private BillingCycle billingCycle;

	private SubscriptionStatus status;

}
