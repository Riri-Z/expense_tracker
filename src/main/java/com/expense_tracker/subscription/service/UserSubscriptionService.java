package com.expense_tracker.subscription.service;

import java.util.Optional;

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
import com.expense_tracker.user.entity.UserInfo;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;

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
	public UserSubscriptionResponseDTO createUserSubscriptionWithNewSubscription(UserInfo userInfo,
			AddUserSubscriptionDTO addUserSubscriptionDTO) {
		try {
			log.info("starting adding user subscription with userSubscriptionDTO: {}", addUserSubscriptionDTO);

			SubscriptionDTO subscriptionDTO = UserSubscriptionMapper.toSubscriptionDTO(addUserSubscriptionDTO);
			Subscription subscription = subscriptionService.getOrCreateSubscription(subscriptionDTO);
			log.info("saved new subscription id: {}", subscription.getId());

			addUserSubscriptionDTO.setUserInfo(userInfo);
			addUserSubscriptionDTO.setSubscription(subscription);

			UserSubscriptionDTO userSubscriptionDTO = UserSubscriptionMapper
				.toUserSubscriptionTDO(addUserSubscriptionDTO);

			UserSubscription userSubscription = addUserSubscription(userSubscriptionDTO);
			log.info("saved new userSubscription: {}", userSubscription);
			return new UserSubscriptionResponseDTO(userInfo.getId(), subscription.getId(), userSubscription.getId());
		}
		catch (ConstraintViolationException ex) {
			log.error("Failed createUserSubscriptionWithNewSubscription with addUserSubscriptionDTO: {}",
					addUserSubscriptionDTO);
			throw new UserSubscriptionException(ex.getMessage());
		}
		catch (Exception ex) {
			log.error("Failed adding user subscription with addUserSubscriptionDTO: {}", addUserSubscriptionDTO);
			throw new UserSubscriptionException(ex.getMessage());
		}
	}

	@Transactional
	public UserSubscription addUserSubscription(UserSubscriptionDTO userSubscriptionDTO) {
		try {

			UserSubscription userSubscription = new UserSubscription();
			userSubscription.setStartDate(userSubscriptionDTO.getStartDate());
			userSubscription.setEndDate(userSubscriptionDTO.getEndDate());
			userSubscription.setRenewalDate(userSubscriptionDTO.getRenewalDate());
			userSubscription.setAmount(userSubscriptionDTO.getAmount());
			userSubscription.setBillingCycle(userSubscriptionDTO.getBillingCycle());
			userSubscription.setStatus(userSubscriptionDTO.getStatus());
			userSubscription.setUserInfo(userSubscriptionDTO.getUserInfo());
			userSubscription.setSubscription(userSubscriptionDTO.getSubscription());
			// verify if userSubscription isn't already in the database with the
			// subscription id and userInfo id

			UserInfo userInfo = userSubscriptionDTO.getUserInfo();
			Long userInfoId = userInfo.getId();
			Subscription subscription = userSubscriptionDTO.getSubscription();
			Long subscriptionId = subscription.getId();
			Optional<UserSubscription> userSubscriptionOptional = userSubscriptionRepository
				.findByUserInfoIdAndSubscriptionId(userInfoId, subscriptionId);
			if (userSubscriptionOptional.isPresent()) {
				log.error("UserSubscription already exists with userInfoId: {} and subscriptionId: {}", userInfoId,
						subscriptionId);
				throw new UserSubscriptionException("UserSubscription already exists with userInfoId: " + userInfoId
						+ " and subscriptionId: " + subscriptionId);
			}
			return userSubscriptionRepository.save(userSubscription);
		}
		catch (ConstraintViolationException ex) {
			log.error("Failed adding user subscription with userSubscriptionDTO: {}, with ex :{}", userSubscriptionDTO,
					ex.getMessage());
			throw new UserSubscriptionException(ex.getMessage());
		}
		catch (Exception ex) {
			log.error("Failed adding user subscription with userSubscriptionDTO: {}", userSubscriptionDTO);
			throw new UserSubscriptionException(ex.getMessage());
		}
	}

}
