package com.expense_tracker.subscription.mapper;

import org.springframework.stereotype.Component;

import com.expense_tracker.subscription.dto.AddUserSubscriptionDTO;
import com.expense_tracker.subscription.dto.SubscriptionDTO;
import com.expense_tracker.subscription.dto.UserSubscriptionDTO;

@Component
public class UserSubscriptionMapper {

	private UserSubscriptionMapper() {
		// constructeur privé pour empecher l'instanciation
	}

	public static SubscriptionDTO toSubscriptionDTO(AddUserSubscriptionDTO addUserSubscriptionDTO) {
		SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
		subscriptionDTO.setSubscriptionName(addUserSubscriptionDTO.getSubscriptionName());
		subscriptionDTO.setSubscriptionDescription(addUserSubscriptionDTO.getSubscriptionDescription());
		return subscriptionDTO;
	}

	public static UserSubscriptionDTO toUserSubscriptionTDO(AddUserSubscriptionDTO addUserSubscriptionDTO) {
		UserSubscriptionDTO userSubscriptionDTO = new UserSubscriptionDTO();

		userSubscriptionDTO.setUserId(addUserSubscriptionDTO.getUserId());
		userSubscriptionDTO.setStartDate(addUserSubscriptionDTO.getStartDate());
		userSubscriptionDTO.setEndDate(addUserSubscriptionDTO.getEndDate());
		userSubscriptionDTO.setAmount(addUserSubscriptionDTO.getAmount());
		userSubscriptionDTO.setBillingCycle(addUserSubscriptionDTO.getBillingCycle());
		userSubscriptionDTO.setStatus(addUserSubscriptionDTO.getStatus());
		return userSubscriptionDTO;
	}

}
