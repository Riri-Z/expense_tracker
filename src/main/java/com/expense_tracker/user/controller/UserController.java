package com.expense_tracker.user.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.expense_tracker.user.model.UserModel;
import com.expense_tracker.user.service.UserService;

@RestController
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/user")
	public ResponseEntity<?> getUser(@RequestParam Long id) {
		try {
			Optional<UserModel> result = userService.findUser(id);
			if (result.isPresent()) {
				return ResponseEntity.ok(result.get());
			}
			else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id " + id + " not found");
			}
		}
		catch (Exception e) {
			// Log l'exception
			// logger.error("Error fetching user with id " + id, e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("An error occurred while fetching the user: " + e.getMessage());
		}
	}

}
