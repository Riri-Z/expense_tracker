package com.expense_tracker.subscription.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expense_tracker.common.apiResponse.ApiResponse;
import com.expense_tracker.subscription.dto.AddUserSubscriptionDTO;
import com.expense_tracker.subscription.dto.UserSubscriptionResponseDTO;
import com.expense_tracker.subscription.service.UserSubscriptionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

	private final UserSubscriptionService userSubscriptionService;

	public SubscriptionController(UserSubscriptionService userSubscriptionService) {

		this.userSubscriptionService = userSubscriptionService;

	}

	@PostMapping("/add-subscription")
	public ApiResponse<UserSubscriptionResponseDTO> addNewSubscription(
			@AuthenticationPrincipal @RequestBody @Valid AddUserSubscriptionDTO payload) {
		// get auth from spring context

		UserSubscriptionResponseDTO result = userSubscriptionService.createUserSubscriptionWithNewSubscription(payload);
		return new ApiResponse<>("ok", result);
	}

}
