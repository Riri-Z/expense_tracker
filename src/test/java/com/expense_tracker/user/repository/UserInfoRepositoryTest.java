package com.expense_tracker.user.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.expense_tracker.user.entity.UserInfo;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
class UserInfoRepositoryTest {

	@LocalServerPort
	private Integer port;

	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

	@BeforeAll
	static void beforeAll() {
		// Call autmaticaly before any test to start postgre
		postgres.start();
	}

	@AfterAll
	static void afterAll() {
		postgres.stop();
	}

	@Autowired
	private UserInfoRepository userInfoRepository;

	@Test
	void UserInfoRepository_getUserInfo_returnUsername() {
		// arrange
		UserInfo userInfo = UserInfo.builder()
			.name("test")
			.email("test@gmail.com")
			.username("username")
			.password("dzkoqdozqkod")
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
