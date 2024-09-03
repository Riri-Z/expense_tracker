package com.expense_tracker.subscription.mapper;

import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.expense_tracker.subscription.dto.AddUserSubscriptionDTO;
import com.expense_tracker.subscription.dto.SubscriptionDTO;
import com.expense_tracker.subscription.dto.UserSubscriptionDTO;
import com.expense_tracker.subscription.entity.Subscription;
import com.expense_tracker.subscription.entity.UserSubscription;

@Component
public class UserSubscriptionMapper {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(UserSubscriptionMapper.class);

	private UserSubscriptionMapper() {
		// constructeur privé pour empecher l'instanciation
	}

	public static SubscriptionDTO toSubscriptionDTO(AddUserSubscriptionDTO addUserSubscriptionDTO) {
		SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
		Subscription subscription = addUserSubscriptionDTO.getSubscription();
		String subscriptionName = subscription.getName();

		subscriptionDTO.setName(subscriptionName);
		return subscriptionDTO;
	}

	public static UserSubscriptionDTO toUserSubscriptionTDO(AddUserSubscriptionDTO addUserSubscriptionDTO) {
		log.info("Starting to map AddUserSubscriptionDTO to UserSubscriptionDTO : {}", addUserSubscriptionDTO);
		UserSubscriptionDTO userSubscriptionDTO = new UserSubscriptionDTO();
		// Manual mapping with BeanUtil
		BeanUtils.copyProperties(addUserSubscriptionDTO, userSubscriptionDTO);

		log.info("Mapped AddUserSubscriptionDTO to UserSubscriptionDTO : {}", userSubscriptionDTO);
		return userSubscriptionDTO;
	}

	public static UserSubscription userSubscriptionDTOtoEntity(UserSubscriptionDTO userSubscriptionDTO) {
		log.info("Starting userSubscriptionDTOtoEntity: {}", userSubscriptionDTO);

		UserSubscription userSubscription = new UserSubscription();
		userSubscription.setStartDate(userSubscriptionDTO.getStartDate());
		userSubscription.setEndDate(userSubscriptionDTO.getEndDate());
		userSubscription.setRenewalDate(userSubscriptionDTO.getRenewalDate());
		userSubscription.setAmount(userSubscriptionDTO.getAmount());
		userSubscription.setBillingCycle(userSubscriptionDTO.getBillingCycle());
		userSubscription.setStatus(userSubscriptionDTO.getStatus());
		userSubscription.setUserInfo(userSubscriptionDTO.getUserInfo());
		userSubscription.setSubscription(userSubscriptionDTO.getSubscription());

		return userSubscription;
	}

}
