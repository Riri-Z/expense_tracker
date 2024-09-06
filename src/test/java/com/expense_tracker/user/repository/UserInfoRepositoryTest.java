package com.expense_tracker.user.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import com.expense_tracker.config.PostgresqlTestContainerBase;
import com.expense_tracker.user.entity.UserInfo;

/* @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers */
@ActiveProfiles("test")
class UserInfoRepositoryTest extends PostgresqlTestContainerBase {

	@Autowired
	private UserInfoRepository userInfoRepository;

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

}
