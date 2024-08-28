package com.expense_tracker.subscription.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.expense_tracker.subscription.dto.SubscriptionDTO;
import com.expense_tracker.subscription.entity.Subscription;
import com.expense_tracker.subscription.repository.SubscriptionRepository;

@Service
public class SubscriptionService {

	private static final Logger log = LoggerFactory.getLogger(SubscriptionService.class);

	private final SubscriptionRepository subscriptionRepository;

	public SubscriptionService(SubscriptionRepository subscriptionRepository) {
		this.subscriptionRepository = subscriptionRepository;
	}

	public Subscription addSubscription(SubscriptionDTO userSubscriptionDTO) {
		log.info("Start adding subscription with AddUseruserSubscriptionDTO: {}", userSubscriptionDTO);
		Subscription subscription = new Subscription();
		subscription.setName(userSubscriptionDTO.getSubscriptionName());
		subscription.setDescription(userSubscriptionDTO.getSubscriptionDescription());
		return subscriptionRepository.save(subscription);
	}

}
