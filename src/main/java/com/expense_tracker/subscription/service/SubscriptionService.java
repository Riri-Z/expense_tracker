package com.expense_tracker.subscription.service;

import java.util.Optional;

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

	public Subscription getOrCreateSubscription(SubscriptionDTO userSubscriptionDTO) {

		Optional<Subscription> subscription = subscriptionRepository.findByName(userSubscriptionDTO.getName());

		if (subscription.isPresent()) {
			log.info("Start adding subscription with AddUseruserSubscriptionDTO: {}", userSubscriptionDTO);
			Subscription sub = subscription.get();
			return subscriptionRepository.save(sub);

		}
		else {
			log.info("Start adding subscription with AddUseruserSubscriptionDTO: {}", userSubscriptionDTO);
			Subscription newSubscription = new Subscription();
			newSubscription.setName(userSubscriptionDTO.getName());
			return subscriptionRepository.save(newSubscription);
		}
	}

}
