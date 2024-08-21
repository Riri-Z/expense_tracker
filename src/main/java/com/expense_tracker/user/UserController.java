package com.expense_tracker.user;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("An error occurred while fetching the user: " + e.getMessage());
		}
	}

}
