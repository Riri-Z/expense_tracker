package com.expense_tracker.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.expense_tracker.user.entity.UserInfo;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

	Optional<UserInfo> findByUsername(String username);

	Optional<UserInfo> findByEmail(String email);

	Boolean existsByUsername(String username);

	Boolean existsByEmailAndIdNot(String email, Long id);

	Boolean existsByEmail(String email);

	/*
	 * Fetch user with the given ID, and his subscriptions
	 */
	@Query("select ui from UserInfo ui left join fetch ui.userSubscriptions us left join fetch us.subscription left join  fetch us.subscription where ui.id = :id")
	Optional<UserInfo> findUserInfoAndHisSubscriptions(@Param("id") Long id);

}
