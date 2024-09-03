package com.expense_tracker.password.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.expense_tracker.email.service.EmailService;
import com.expense_tracker.password.dto.PasswordResetDTO;
import com.expense_tracker.password.dto.PasswordResetRequestDTO;
import com.expense_tracker.password.util.UrlUtils;
import com.expense_tracker.user.entity.UserInfo;
import com.expense_tracker.user.service.UserInfoService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
public class PasswordControler {

	private static final Logger log = LoggerFactory.getLogger(PasswordControler.class);

	private final UserInfoService userInfoService;

	private final EmailService emailService;

	public PasswordControler(UserInfoService userInfoService, EmailService emailService) {
		this.userInfoService = userInfoService;
		this.emailService = emailService;
	}

	// TODO : reafactor this to be cleaner
	@PostMapping("/request-password-reset")
	public ResponseEntity<String> requestPasswordReset(
			@Valid @RequestBody PasswordResetRequestDTO passwordResetRequestDTO, HttpServletRequest request) {
		String email = passwordResetRequestDTO.getEmail();
		log.info("Looking for userInfo with following emaill : {}", email);
		UserInfo userInfoOptional = userInfoService.findByEmail(email);
		// extract userInfo, so the type is now Userinfo, and notOptional anymore
		UserInfo userInfo = userInfoOptional;
		// generate token
		String passwordToken = UUID.randomUUID().toString();
		// save token in bdd
		userInfoService.createPasswordResetTokenForUser(passwordToken, userInfo);
		String passwordResetUrl = passwordResetEmailLink(userInfo, UrlUtils.applicationUrl(request), passwordToken);

		return ResponseEntity.ok(passwordResetUrl);
	}

	@PostMapping("/reset-password")
	public String resetPassword(@Valid @RequestBody PasswordResetDTO passwordResetRequest,
			@RequestParam("token") String passwordResetToken) {
		log.info("Start resetPassword with passwordResetToken :  {}", passwordResetToken);
		String tokenValidation = userInfoService.validatePasswordResetToken(passwordResetToken);
		if (!tokenValidation.equalsIgnoreCase("valid")) {
			// TODO : throw error "Invalid password reset token"
			return "Invalid password reset token";
		}
		if (!passwordResetRequest.getNewPassword().equals(passwordResetRequest.getConfirmPassword())) {
			// TODO : throw error both password does not match
			return "New password and confirm password doesn't match";
		}
		UserInfo userInfo = userInfoService.findUserByPasswordToken(passwordResetToken);
		if (userInfo != null) {
			userInfoService.resetPassword(userInfo, passwordResetRequest.getNewPassword());
			return "Password has been reset successfully";
		}
		return "Invalid password reset token";
	}

	private String passwordResetEmailLink(UserInfo userInfo, String applicationUrl, String passwordToken) {
		log.info("Start passwordResetEmailLink with url: {}, passwordToken : {}", applicationUrl, passwordToken);
		String url = applicationUrl + "/reset-password?token=" + passwordToken;
		emailService.sendPasswordResetEmail(userInfo.getEmail(), url);
		log.info("Email sent");

		return url;
	}

}
