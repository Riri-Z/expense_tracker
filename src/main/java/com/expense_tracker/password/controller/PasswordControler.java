package com.expense_tracker.password.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.expense_tracker.password.util.PasswordResetRequestUtil;
import com.expense_tracker.user.controller.UserController;
import com.expense_tracker.user.entity.UserInfo;
import com.expense_tracker.user.service.UserInfoService;

@RestController
public class PasswordControler {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	private final UserInfoService userInfoService;

	public PasswordControler(UserInfoService userInfoService) {
		this.userInfoService = userInfoService;
	}

	@PostMapping("/reset-password")
	public String resetPassword(@RequestBody PasswordResetRequestUtil passwordResetRequestUtil,
			@RequestParam("token") String passwordResetToken) {
		log.info("Start resetPassword with passwordResetToken :  {}", passwordResetToken);
		String tokenValidation = userInfoService.validatePasswordResetToken(passwordResetToken);
		if (!tokenValidation.equalsIgnoreCase("valid")) {
			// TODO : throw error "Invalid password reset token"
			return "Invalid password reset token";
		}
		if (!passwordResetRequestUtil.getNewPassword().equals(passwordResetRequestUtil.getConfirmPassword())) {
			// TODO : throw error both password does not match
			return "New password and confirm password doesn't match";
		}
		UserInfo userInfo = userInfoService.findUserByPasswordToken(passwordResetToken);
		if (userInfo != null) {
			userInfoService.resetPassword(userInfo, passwordResetRequestUtil.getNewPassword());
			return "Password has been reset successfully";
		}
		return "Invalid password reset token";
	}

}
