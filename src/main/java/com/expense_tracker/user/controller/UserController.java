package com.expense_tracker.user.controller;

import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expense_tracker.email.service.EmailService;
import com.expense_tracker.exception.user.UserAccessDenied;
import com.expense_tracker.exception.user.UserNotFoundException;
import com.expense_tracker.jwt.JwtService;
import com.expense_tracker.password.util.PasswordResetRequestUtil;
import com.expense_tracker.security.AuthRequest;
import com.expense_tracker.subscription.dto.AddUserSubscriptionDTO;
import com.expense_tracker.subscription.dto.UserSubscriptionResponseDTO;
import com.expense_tracker.user.dto.UpdatePasswordDTO;
import com.expense_tracker.user.dto.UpdateUserDTO;
import com.expense_tracker.user.dto.UserInfoDTO;
import com.expense_tracker.user.entity.UserInfo;
import com.expense_tracker.user.repository.UserInfoRepository;
import com.expense_tracker.user.service.UserInfoDetails;
import com.expense_tracker.user.service.UserInfoService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class UserController {

	/*
	 * TODO: Create IUserService to link controller to service, and not access UserService
	 * directly. It's like we create a contract between controller and service , and limit
	 * their dependency Also, it will ease the creation of mock test,
	 *
	 */
	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	private final UserInfoService userInfoService;

	private final JwtService jwtService;

	private final EmailService emailService;

	private final AuthenticationManager authenticationManager;

	private final UserInfoRepository userInfoRepository;

	public UserController(UserInfoService userInfoService, JwtService jwtService,
			AuthenticationManager authenticationManager, UserInfoRepository userInfoRepository,
			EmailService emailService) {

		this.userInfoService = userInfoService;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
		this.userInfoRepository = userInfoRepository;
		this.emailService = emailService;
	}

	@GetMapping("/welcome")
	public String welcome() {
		return "Welcome this endpoint is not secure";
	}

	@PutMapping("user/{id}")
	// @PreAuthorize("hasAuthority('ROLE_USER','ROLE_ADMIN')")
	public ResponseEntity<UserInfoDTO> updateUser(@PathVariable Long id, @RequestBody UpdateUserDTO updateUserDTO)
			throws AccessDeniedException {
		log.info("Entering updateUser method for id: {}", id);

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
			log.warn("Access denied for user {} trying to update user {}", userIdFromJwt, id);
			throw new AccessDeniedException("You are not authorized to update this user");
		}

		UserInfoDTO updatedUser = userInfoService.updateUser(id, updateUserDTO);
		return ResponseEntity.ok(updatedUser);

	}

	/**
	 * PUT /user/password/{id} : Update the password of the user with the given id, and
	 * new passord and old password in the request body
	 * @param id
	 * @param entity
	 * @return
	 */
	@PostMapping("user/update-password")
	public ResponseEntity<String> updatePassword(@AuthenticationPrincipal UserInfoDetails userInfoDetails,
			@Valid @RequestBody UpdatePasswordDTO request) {

		userInfoService.updatePassword(userInfoDetails.getId(), request.getOldPassword(), request.getNewPassword());
		return ResponseEntity.ok("Password updated successfully");

	}

	@PostMapping("user/request-password-reset")
	public ResponseEntity<String> requestPasswordReset(@RequestBody PasswordResetRequestUtil passwordResetRequestUtil,
			HttpServletRequest request) {
		UserInfo userInfoOptional = userInfoService.findByEmail(passwordResetRequestUtil.getEmail());

		// TODO : reafactor this to be cleaner

		// extract userInfo, so the type is now Userinfo, and notOptional anymore
		UserInfo userInfo = userInfoOptional;
		// generate token
		String passwordToken = UUID.randomUUID().toString();
		// save token in bdd
		userInfoService.createPasswordResetTokenForUser(passwordToken, userInfo);
		String passwordResetUrl = passwordResetEmailLink(userInfo, applicationUrl(request), passwordToken);

		return ResponseEntity.ok(passwordResetUrl);
	}

	@PostMapping("/addNewUser")
	public ResponseEntity<UserInfoDTO> addNewUser(@Valid @RequestBody UserInfo userInfo) {
		UserInfoDTO addedUser = userInfoService.addUser(userInfo);
		return ResponseEntity.ok(addedUser);
	}

	@PostMapping("user/add-subscription")
	public ResponseEntity<UserSubscriptionResponseDTO> addNewSubscription(
			@AuthenticationPrincipal @RequestBody @Valid AddUserSubscriptionDTO payload) {
		// get auth fronm spring context
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserInfoDetails userDetails = (UserInfoDetails) authentication.getPrincipal();

		Long id = userDetails.getId();
		UserSubscriptionResponseDTO result = userInfoService.addUserSubscription(id, payload);
		return ResponseEntity.ok(result);
	}

	@DeleteMapping("user/delete")
	public ResponseEntity<String> deleteUser(@AuthenticationPrincipal UserInfoDetails userInfoDetails) {
		Long id = userInfoDetails.getId();
		userInfoService.deleteUser(id);
		return ResponseEntity.ok("User with id : " + id + " was deleted ");
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
	public ResponseEntity<Map<String, String>> authenticateAndGetToken(@RequestBody @Valid AuthRequest authRequest) {
		try {

			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

			if (authentication.isAuthenticated()) {
				// get authentified user
				UserDetails userDetails = (UserDetails) authentication.getPrincipal();

				// get user from bdd
				UserInfo userInfo = userInfoRepository.findByUsername(userDetails.getUsername())
					.orElseThrow(() -> new UserNotFoundException("User not found: " + userDetails.getUsername()));
				String idUser = String.valueOf((userInfo.getId()));
				return ResponseEntity.ok(jwtService.generateToken(idUser, userInfo.getUsername()));
			}
			else {
				throw new UserAccessDenied("Cannot generate token, authentication failed");
			}
		}
		catch (InternalAuthenticationServiceException e) {
			log.error("Authentication failed for user: {}", authRequest.getUsername(), e);

			throw new UserAccessDenied("Cannot generate token");
		}
	}

	public String applicationUrl(HttpServletRequest request) {
		// TODO: handle prod url
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}

	private String passwordResetEmailLink(UserInfo userInfo, String applicationUrl, String passwordToken) {
		log.info("Start passwordResetEmailLink with url: {}, passwordToken : {}", applicationUrl, passwordToken);
		String url = applicationUrl + "/reset-password?token=" + passwordToken;
		emailService.sendPasswordResetEmail(userInfo.getEmail(), url);
		log.info("Email sent");

		return url;
	}

}
