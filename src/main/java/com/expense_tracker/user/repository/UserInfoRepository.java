package com.expense_tracker.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.expense_tracker.user.entity.UserInfo;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

	Optional<UserInfo> findByUsername(String username);

	Optional<UserInfo> findByEmail(String email);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);

}
