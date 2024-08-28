package com.expense_tracker.subscription.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.expense_tracker.subscription.enums.BillingCycle;
import com.expense_tracker.subscription.enums.SubscriptionStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class AddUserSubscriptionDTO {

	private Long userId;

	private String subscriptionName;

	private String subscriptionDescription;

	private BigDecimal amount;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
	private LocalDate startDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
	private LocalDate endDate;

	private LocalDate renewalDate;

	private BillingCycle billingCycle;

	private SubscriptionStatus status;

}
