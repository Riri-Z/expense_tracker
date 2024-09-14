package com.expense_tracker.subscription.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expense_tracker.common.apiResponse.ApiResponse;
import com.expense_tracker.subscription.dto.UserSubscriptionDTO;
import com.expense_tracker.subscription.dto.UserSubscriptionResponseDTO;
import com.expense_tracker.subscription.service.UserSubscriptionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

	private final UserSubscriptionService userSubscriptionService;

	public SubscriptionController(UserSubscriptionService userSubscriptionService) {

		this.userSubscriptionService = userSubscriptionService;

	}

	@PostMapping("/add-subscription")
	public ApiResponse<UserSubscriptionResponseDTO> addNewSubscription(
			@AuthenticationPrincipal @RequestBody @Valid UserSubscriptionDTO payload) {
		// get auth from spring context

		UserSubscriptionResponseDTO result = userSubscriptionService.createUserSubscriptionWithNewSubscription(payload);
		return new ApiResponse<>("ok", result);
	}

	@PutMapping("/update-subscription/{id}")
	public ApiResponse<UserSubscriptionResponseDTO> updateSubscription(
			@AuthenticationPrincipal @Valid @RequestBody UserSubscriptionDTO payload, @PathVariable Long id) {
		UserSubscriptionResponseDTO result = userSubscriptionService.updateUserSubscription(payload, id);
		return new ApiResponse<>("ok", result);
	}

	@DeleteMapping("/delete-user-subscription/{id}")
	public ApiResponse<String> deleteUserSubscription(@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Long id) {
		String result = userSubscriptionService.delete(id, userDetails.getUsername());
		return new ApiResponse<>("ok", result);
	}

}