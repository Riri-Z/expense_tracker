package com.expense_tracker.subscription.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.expense_tracker.exception.user_subscription.UserSubscriptionConflictException;
import com.expense_tracker.exception.user_subscription.UserSubscriptionException;
import com.expense_tracker.subscription.dto.AddUserSubscriptionDTO;
import com.expense_tracker.subscription.dto.SubscriptionDTO;
import com.expense_tracker.subscription.dto.UserSubscriptionDTO;
import com.expense_tracker.subscription.dto.UserSubscriptionResponseDTO;
import com.expense_tracker.subscription.entity.Subscription;
import com.expense_tracker.subscription.entity.UserSubscription;
import com.expense_tracker.subscription.mapper.UserSubscriptionMapper;
import com.expense_tracker.subscription.repository.SubscriptionRepository;
import com.expense_tracker.subscription.repository.UserSubscriptionRepository;
import com.expense_tracker.subscription.validator.UserSubscriptionValidator;
import com.expense_tracker.user.entity.UserInfo;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;

@Service
public class UserSubscriptionService {

	private final Logger log = LoggerFactory.getLogger(UserSubscriptionService.class);

	private final UserSubscriptionRepository userSubscriptionRepository;

	private final UserSubscriptionValidator userSubscriptionValidator;

	private final SubscriptionRepository subscriptionRepository;


	public UserSubscriptionService(UserSubscriptionRepository userSubscriptionRepository,
			UserSubscriptionValidator userSubscriptionValidator, SubscriptionRepository subscriptionRepository
			) {
		this.userSubscriptionRepository = userSubscriptionRepository;
		this.userSubscriptionValidator = userSubscriptionValidator;
		this.subscriptionRepository = subscriptionRepository;

	}

	@Transactional
	public UserSubscriptionResponseDTO createUserSubscriptionWithNewSubscription(
			AddUserSubscriptionDTO addUserSubscriptionDTO) {
		try {
			log.info("starting adding user subscription with userSubscriptionDTO: {}", addUserSubscriptionDTO);

			UserInfo userInfo = addUserSubscriptionDTO.getUserInfo();

			SubscriptionDTO subscriptionDTO = UserSubscriptionMapper.toSubscriptionDTO(addUserSubscriptionDTO);
			Subscription subscription = getOrCreateSubscription(subscriptionDTO);
			log.info("saved new subscription id: {}", subscription.getId());

			addUserSubscriptionDTO.setUserInfo(userInfo);
			addUserSubscriptionDTO.setSubscription(subscription);

			UserSubscriptionDTO userSubscriptionDTO = UserSubscriptionMapper
					.toUserSubscriptionTDO(addUserSubscriptionDTO);

			UserSubscription userSubscription = addUserSubscription(userSubscriptionDTO);
			log.info("saved new userSubscription: {}", userSubscription);
			return new UserSubscriptionResponseDTO(userInfo.getId(), subscription.getId(), userSubscription.getId());
		} catch (UserSubscriptionConflictException ex) {
			throw new UserSubscriptionConflictException(ex.getMessage());
		} catch (ConstraintViolationException ex) {
			log.error("Failed createUserSubscriptionWithNewSubscription with addUserSubscriptionDTO: {}",
					addUserSubscriptionDTO);
			throw new UserSubscriptionException(ex.getMessage());
		} catch (Exception ex) {
			log.error("Failed adding user subscription with addUserSubscriptionDTO: {}", addUserSubscriptionDTO);
			throw new UserSubscriptionException(ex.getMessage());
		}
	}

	public Subscription getOrCreateSubscription(SubscriptionDTO userSubscriptionDTO) {

		Optional<Subscription> subscription = subscriptionRepository.findByName(userSubscriptionDTO.getName());

		if (subscription.isPresent()) {
			log.info("Start adding subscription with AddUseruserSubscriptionDTO: {}", userSubscriptionDTO);
			Subscription sub = subscription.get();
			return subscriptionRepository.save(sub);

		} else {
			log.info("Start adding subscription with AddUseruserSubscriptionDTO: {}", userSubscriptionDTO);
			Subscription newSubscription = new Subscription();
			newSubscription.setName(userSubscriptionDTO.getName());
			return subscriptionRepository.save(newSubscription);
		}
	}

	@Transactional
	public UserSubscription addUserSubscription(UserSubscriptionDTO userSubscriptionDTO) {
		try {
			log.info("Started addUserSubscription with the following  userSubscriptionDTO: {} ", userSubscriptionDTO);
			UserSubscription userSubscription = UserSubscriptionMapper.userSubscriptionDTOtoEntity(userSubscriptionDTO);

			UserInfo userInfo = userSubscriptionDTO.getUserInfo();
			Long userInfoId = userInfo.getId();
			Subscription subscription = userSubscriptionDTO.getSubscription();
			Long subscriptionId = subscription.getId();

			userSubscriptionValidator.validateUniqueUserSubscription(userInfoId, subscriptionId);
			return userSubscriptionRepository.save(userSubscription);
		} catch (UserSubscriptionConflictException ex) {
			throw new UserSubscriptionConflictException(ex.getMessage());
		} catch (ConstraintViolationException ex) {
			throw new UserSubscriptionException(ex.getMessage());
		}
	}

}