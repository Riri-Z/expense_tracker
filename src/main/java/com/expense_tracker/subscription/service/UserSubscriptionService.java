package com.expense_tracker.subscription.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.expense_tracker.exception.userSubscription.UserSubscriptionException;
import com.expense_tracker.subscription.dto.AddUserSubscriptionDTO;
import com.expense_tracker.subscription.dto.SubscriptionDTO;
import com.expense_tracker.subscription.dto.UserSubscriptionDTO;
import com.expense_tracker.subscription.dto.UserSubscriptionResponseDTO;
import com.expense_tracker.subscription.entity.Subscription;
import com.expense_tracker.subscription.entity.UserSubscription;
import com.expense_tracker.subscription.mapper.UserSubscriptionMapper;
import com.expense_tracker.subscription.repository.UserSubscriptionRepository;

import jakarta.transaction.Transactional;

@Service
public class UserSubscriptionService {

	private final Logger log = LoggerFactory.getLogger(UserSubscriptionService.class);

	private final UserSubscriptionRepository userSubscriptionRepository;

	private final SubscriptionService subscriptionService;

	public UserSubscriptionService(UserSubscriptionRepository userSubscriptionRepository,
			SubscriptionService subscriptionService) {
		this.userSubscriptionRepository = userSubscriptionRepository;
		this.subscriptionService = subscriptionService;
	}

	@Transactional
	public UserSubscriptionResponseDTO createUserSubscriptionWithNewSubscription(Long userId,
			AddUserSubscriptionDTO addUserSubscriptionDTO) {
		try {
			log.info("starting adding user subscription with userSubscriptionDTO: {}", addUserSubscriptionDTO);

			SubscriptionDTO subscriptionDTO = UserSubscriptionMapper.toSubscriptionDTO(addUserSubscriptionDTO);
			Subscription subscription = subscriptionService.addSubscription(subscriptionDTO);
			log.info("saved new subscription: {}", subscription.getId());

			UserSubscriptionDTO userSubscriptionDTO = UserSubscriptionMapper
				.toUserSubscriptionTDO(addUserSubscriptionDTO);

			UserSubscription userSubscription = addUserSubscription(userSubscriptionDTO);
			log.info("saved new userSubscription: {}", userSubscription);
			return new UserSubscriptionResponseDTO(userId, subscription.getId(), userSubscription.getId());

		}
		catch (Exception ex) {
			log.error("Failed adding user subscription with addUserSubscriptionDTO: {}", addUserSubscriptionDTO);
			throw new UserSubscriptionException("Failed operation with the following exception: " + ex.getMessage());
		}
	}

	public UserSubscription addUserSubscription(UserSubscriptionDTO userSubscriptionDTO) {
		try {

			UserSubscription userSubscription = new UserSubscription();
			userSubscription.setAmount(userSubscriptionDTO.getAmount());
			userSubscription.setBillingCycle(userSubscriptionDTO.getBillingCycle());
			userSubscription.setEndDate(userSubscriptionDTO.getEndDate());
			userSubscription.setRenewalDate(userSubscriptionDTO.getRenewalDate());
			userSubscription.setStartDate(userSubscriptionDTO.getStartDate());
			userSubscription.setStatus(userSubscriptionDTO.getStatus());
			return userSubscriptionRepository.save(userSubscription);
		}
		catch (Exception ex) {
			log.error("Failed adding user subscription with userSubscriptionDTO: {}", userSubscriptionDTO);
			throw new UserSubscriptionException("Failed operation with the following exception: " + ex.getMessage());
		}
	}

}
