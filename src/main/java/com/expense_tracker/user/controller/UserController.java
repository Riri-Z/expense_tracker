package com.expense_tracker.user.controller;

import java.nio.file.AccessDeniedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expense_tracker.jwt.JwtService;
import com.expense_tracker.security.AuthRequest;
import com.expense_tracker.user.dto.UpdateUserDTO;
import com.expense_tracker.user.dto.UserInfoDTO;
import com.expense_tracker.user.entity.UserInfo;
import com.expense_tracker.user.repository.UserInfoRepository;
import com.expense_tracker.user.service.UserInfoDetails;
import com.expense_tracker.user.service.UserInfoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	private final UserInfoService service;

	private final JwtService jwtService;

	private final AuthenticationManager authenticationManager;

	private final UserInfoRepository userInfoRepository;

	public UserController(UserInfoService service, JwtService jwtService, AuthenticationManager authenticationManager,
			UserInfoRepository userInfoRepository) {

		this.service = service;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
		this.userInfoRepository = userInfoRepository;
	}

	@GetMapping("/welcome")
	public String welcome() {
		return "Welcome this endpoint is not secure";
	}

	@PutMapping("user/{id}")
	// @PreAuthorize("hasAuthority('ROLE_USER','ROLE_ADMIN')")
	public ResponseEntity<UserInfoDTO> updateUser(@PathVariable Long id, @RequestBody UpdateUserDTO updateUserDTO)
			throws AccessDeniedException {
		logger.info("Entering updateUser method for id: {}", id);

		// check if id from jwt == id in path
		// get auth fronm spring context
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// Verify if user is authentified
		if (authentication == null || !authentication.isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		// get userDetails
		UserInfoDetails userDetails = (UserInfoDetails) authentication.getPrincipal();
		Long userIdFromJwt = userDetails.getId();

		// Check if id from jwt is equals id from path
		if (!userIdFromJwt.equals(id)) {
			logger.warn("Access denied for user {} trying to update user {}", userIdFromJwt, id);
			throw new AccessDeniedException("You are not authorized to update this user");
		}

		UserInfoDTO updatedUser = service.updateUser(id, updateUserDTO);
		return ResponseEntity.ok(updatedUser);

	}

	@PostMapping("/addNewUser")
	public ResponseEntity<UserInfoDTO> addNewUser(@Valid @RequestBody UserInfo userInfo) {
		UserInfoDTO addedUser = service.addUser(userInfo);
		return ResponseEntity.ok(addedUser);
	}

	@GetMapping("/user/userProfile")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public ResponseEntity<String> userProfile() {
		try {

			return ResponseEntity.ok("Welcome to User Profile");
		}
		catch (Exception e) {
			System.out.println(e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/admin/adminProfile")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String adminProfile() {
		return "Welcome to Admin Profile";
	}

	@PostMapping("/generateToken")
	public ResponseEntity<String> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
			if (authentication.isAuthenticated()) {
				// get authentified user
				UserDetails userDetails = (UserDetails) authentication.getPrincipal();

				// get user from bdd
				UserInfo userInfo = userInfoRepository.findByUsername(userDetails.getUsername())
					.orElseThrow(() -> new UsernameNotFoundException("User not found: " + userDetails.getUsername()));
				String idUser = String.valueOf((userInfo.getId()));
				return ResponseEntity.ok(jwtService.generateToken(idUser));
			}
			else {
				throw new UsernameNotFoundException("Invalid user request!");
			}
		}
		catch (AuthenticationException e) {
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

}
