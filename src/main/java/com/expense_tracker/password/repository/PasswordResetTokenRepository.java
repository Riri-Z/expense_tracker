package com.expense_tracker.password.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.expense_tracker.password.entity.PasswordResetToken;
import com.expense_tracker.user.entity.UserInfo;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

	PasswordResetToken findByToken(String token);

	PasswordResetToken findByUserInfo(UserInfo userInfo);

}
