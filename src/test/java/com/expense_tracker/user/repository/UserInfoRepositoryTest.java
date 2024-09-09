package com.expense_tracker.user.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import com.expense_tracker.config.PostgresqlTestContainerBase;
import com.expense_tracker.subscription.entity.Subscription;
import com.expense_tracker.subscription.entity.UserSubscription;
import com.expense_tracker.subscription.enums.BillingCycle;
import com.expense_tracker.subscription.enums.SubscriptionStatus;
import com.expense_tracker.subscription.repository.SubscriptionRepository;
import com.expense_tracker.subscription.repository.UserSubscriptionRepository;
import com.expense_tracker.user.entity.UserInfo;

@ActiveProfiles("test")
class UserInfoRepositoryTest extends PostgresqlTestContainerBase {

	@Autowired
	private UserInfoRepository userInfoRepository;

	@Autowired
	private UserSubscriptionRepository userSubscriptionRepository;

	@Autowired
	private SubscriptionRepository subscriptionRepository;

	@Test
	void UserInfoRepository_getUserInfo_returnUsername() {
		// arrange
		UserInfo userInfo = UserInfo.builder()
			.name("test")
			.email("test@gmail.com")
			.username("username")
			.password("myPassword")
			.roles("ROLE_USER")
			.build();

		userInfoRepository.save(userInfo);
		// act
		Optional<UserInfo> user = userInfoRepository.findByUsername("username");
		System.out.println("===LOG====" + user);
		// assert
		Assertions.assertThat(user).isNotNull();
		Assertions.assertThat(user.get().getId()).isPositive();
	}

	@Test
	void UserInfoRepository_getUserInfoByEmail() {
		String email = "testGetUserByEmail@gmail.com";

		UserInfo userInfo = UserInfo.builder()
			.name("testGetUserByEmail")
			.email(email)
			.username("testGetUserByEmail")
			.password("myPassword")
			.roles("ROLE_USER")
			.build();

		userInfoRepository.save(userInfo);

		Optional<UserInfo> userByEmail = userInfoRepository.findByEmail(email);

		Assertions.assertThat(userByEmail).isNotNull();
		Assertions.assertThat(userByEmail.get().getEmail()).isEqualTo(email);
	}

	@Test
	void UserInfoRepository_existsByEmailAndIdNot_returnBoolean() {

		UserInfo userInfo = UserInfo.builder()
			.name("myName")
			.email("existsByEmailAndIdNot@gmail.com")
			.username("myUsername")
			.password("myPassword")
			.roles("ROLE_USER")
			.build();

		userInfoRepository.save(userInfo);

		Boolean userExistsByEmailAndIdNot = userInfoRepository.existsByEmailAndIdNot(userInfo.getEmail(),
				userInfo.getId());

		Assertions.assertThat(userExistsByEmailAndIdNot).isFalse();

	}

	@Test
	void UserInfoRepository_getUserInfoAndHisSubscriptions_returnUserWithSubscription() {

		// Create userInfo
		UserInfo userInfo = UserInfo.builder()
			.name("userWithSub")
			.email("userWithSub@gmail.com")
			.username("userWithSub")
			.password("myPassword")
			.roles("ROLE_USER")
			.build();

		UserInfo savedUser = userInfoRepository.save(userInfo);
		System.out.println("===LOG====" + savedUser);
		System.out.println("===LOG getId====" + savedUser.getId());

		// Create subscription
		Subscription subscription = new Subscription();
		subscription.setName("test");
		Subscription savedSubscriptions = subscriptionRepository.save(subscription);
		System.out.println("===savedSubscriptions====" + savedSubscriptions);

		// Create userSubscription
		LocalDateTime now = LocalDateTime.now();
		ZonedDateTime startDate = now.atZone(ZoneId.systemDefault());
		ZonedDateTime endSubscription = startDate.plusYears(2);
		ZonedDateTime renewalDateTime = startDate.plusMonths(1);
		BigDecimal amount = BigDecimal.valueOf(200);
		UserSubscription userSubscription = new UserSubscription();
		userSubscription.setStartDate(startDate);
		userSubscription.setEndDate(endSubscription);
		userSubscription.setRenewalDate(renewalDateTime);
		userSubscription.setAmount(amount);
		userSubscription.setBillingCycle(BillingCycle.MONTHLY);
		userSubscription.setStatus(SubscriptionStatus.ACTIVE);
		userSubscription.setUserInfo(savedUser);
		userSubscription.setSubscription(savedSubscriptions);

		// save userSubscription to this user
		UserSubscription savedUserSubscription = userSubscriptionRepository.save(userSubscription);
		System.out.println("===LOG savedUserSubscription====" + savedUserSubscription);

		Assertions.assertThat(savedUserSubscription.getStartDate()).isEqualTo(startDate);
		Assertions.assertThat(savedUserSubscription.getEndDate()).isEqualTo(endSubscription);
		Assertions.assertThat(savedUserSubscription.getAmount()).isEqualTo(amount);
		Assertions.assertThat(savedUserSubscription.getBillingCycle()).isEqualTo(BillingCycle.MONTHLY);
		Assertions.assertThat(savedUserSubscription.getStatus()).isEqualTo(SubscriptionStatus.ACTIVE);

		Optional<UserInfo> userInfoAndHisSubscriptions = userInfoRepository
			.findUserInfoAndHisSubscriptions(savedUser.getId());
		System.out.println("===LOG userInfoAndHisSubscriptions====" + userInfoAndHisSubscriptions);

		Assertions.assertThat(userInfoAndHisSubscriptions.get().getId()).isEqualTo(savedUser.getId());

	}

}
