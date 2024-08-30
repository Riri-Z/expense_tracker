package com.expense_tracker.subscription.validator;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.expense_tracker.exception.user_subscription.UserSubscriptionConflictException;
import com.expense_tracker.subscription.entity.UserSubscription;
import com.expense_tracker.subscription.repository.UserSubscriptionRepository;

@Component
public class UserSubscriptionValidator {

	Logger log = LoggerFactory.getLogger(UserSubscriptionValidator.class);

	private final UserSubscriptionRepository userSubscriptionRepository;

	public UserSubscriptionValidator(UserSubscriptionRepository userSubscriptionRepository) {
		this.userSubscriptionRepository = userSubscriptionRepository;
	}

	public void validateUniqueUserSubscription(Long userInfoId, Long subscriptionId) {
		log.info("Starting to validate if userInfoId: {} and subscriptionId: {} is unique", userInfoId, subscriptionId);

		Optional<UserSubscription> userSubscription = userSubscriptionRepository
			.findByUserInfoIdAndSubscriptionId(userInfoId, subscriptionId);
		if (userSubscription.isPresent()) {
			log.error("UserSubscription already exists with userInfoId: {} and subscriptionId: {}", userInfoId,
					subscriptionId);
			throw new UserSubscriptionConflictException("UserSubscription already exists with userInfoId: " + userInfoId
					+ " and subscriptionId: " + subscriptionId);
		}
		log.info("UserSubscription is unique with userInfoId: {} and subscriptionId: {}", userInfoId, subscriptionId);
	}

}
