package com.expense_tracker.subscription.dto;

import lombok.Data;

@Data
public class UserSubscriptionResponseDTO {

	private Long userId;

	private Long subscriptionId;

	private Long userSubscriptionId;

	public UserSubscriptionResponseDTO(Long userId, Long subscriptionId, Long userSubscriptionId) {
		this.userId = userId;
		this.subscriptionId = subscriptionId;
		this.userSubscriptionId = userSubscriptionId;
	}

}
