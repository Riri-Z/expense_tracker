package com.expense_tracker.user.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.expense_tracker.exception.user.DuplicateUserException;
import com.expense_tracker.exception.user.MissMatchedPasswordException;
import com.expense_tracker.exception.user.UserNotFoundException;
import com.expense_tracker.password.service.PasswordResetTokenService;
import com.expense_tracker.user.dto.UpdateUserDTO;
import com.expense_tracker.user.dto.UserInfoDTO;
import com.expense_tracker.user.entity.UserInfo;
import com.expense_tracker.user.mapper.UserInfoMapper;
import com.expense_tracker.user.repository.UserInfoRepository;
import com.expense_tracker.user.utils.UserFieldValidator;

import jakarta.validation.Valid;

@Service
public class UserInfoService implements UserDetailsService {

	private static final String NOT_FOUND = " not found";

	private static final Logger log = LoggerFactory.getLogger(UserInfoService.class);

	private final UserInfoRepository repository;

	private final PasswordEncoder encoder;

	private final UserInfoMapper userInfoMapper;


	private final PasswordResetTokenService passwordResetTokenService;

	public UserInfoService(UserInfoRepository repository, PasswordEncoder encoder, UserInfoMapper userInfoMapper,
		 PasswordResetTokenService passwordResetTokenService) {
		this.repository = repository;
		this.encoder = encoder;
		this.userInfoMapper = userInfoMapper;
		this.passwordResetTokenService = passwordResetTokenService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
		Optional<UserInfo> userDetail = repository.findByUsername(username);

		// Converting UserInfo to UserDetails
		return userDetail.map(UserInfoDetails::new)
			.orElseThrow(() -> new UserNotFoundException("User not found: " + username));
	}

	public UserInfo findByEmail(String email) {
		return repository.findByEmail(email)
			.orElseThrow(() -> new UserNotFoundException("User not found with id: " + email));
	}

	public UserInfoDTO findById(Long id) {
		UserInfo userInfo = repository.findById(id)
			.orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

		return userInfoMapper.userInfoToDTO(userInfo);
	}

	public UserDetails loadUserById(Long id) throws UserNotFoundException {
		UserInfo user = repository.findById(id)
			.orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

		return new UserInfoDetails(user);
	}

	public void createPasswordResetTokenForUser(String token, UserInfo userInfo) {
		passwordResetTokenService.createPasswordResetTokenForUser(token, userInfo);
	}

	public void resetPassword(UserInfo userInfo, String newPassword) {

		userInfo.setPassword(encoder.encode(newPassword));
		repository.save(userInfo);

	}

	@Validated
	public UserInfoDTO addUser(@Valid UserInfo userInfo) {
		// verify if username or email already exist

		UserFieldValidator.validateUserFields(userInfo.getName(), "Name");
		UserFieldValidator.validateUserFields(userInfo.getEmail(), "Email");
		UserFieldValidator.validateUserFields(userInfo.getUsername(), "Username");
		UserFieldValidator.validateUserFields(userInfo.getPassword(), "Password");
		UserFieldValidator.validateUserFields(userInfo.getRoles(), "Roles");

		// check unicity
		if (Boolean.TRUE.equals(repository.existsByEmail(userInfo.getEmail()))) {
			throw new DuplicateUserException("Email already exists :" + userInfo.getEmail());
		}
		if (Boolean.TRUE.equals(repository.existsByUsername((userInfo.getUsername())))) {
			throw new DuplicateUserException("Username already exists :" + userInfo.getUsername());
		}
		// Encode password before saving the user
		userInfo.setPassword(encoder.encode(userInfo.getPassword()));
		UserInfo savedUser = repository.save(userInfo);

		return userInfoMapper.userInfoToDTO(savedUser);
	}

	public boolean deleteUser(Long id) {
		log.info("Start Deleting user with id: {}", id);

		boolean userExist = repository.existsById(id);
		log.info("user find with id: {}", id);

		if (!userExist) {
			log.warn("user missing with id: {}", id);

			throw new UserNotFoundException("User with the following " + id + " doesn't exist");
		}

		try {
			log.warn("deleting user with id: {}", id);

			repository.deleteById(id);
			log.info("SUCCESS delete id: {}", id);

			return true;
		}
		catch (Exception ex) {
			log.error("Failed deleting the following user id  : {} ", id);
			return false;
		}
	}

	@Validated
	public UserInfoDTO updateUser(@Valid Long id, UpdateUserDTO updateUserDTO) {
		log.info("Updating user with updateUserDTO: {}", updateUserDTO);

		UserInfo user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id + NOT_FOUND));
		UserFieldValidator.validateUserFields(updateUserDTO.getName(), "Name");
		UserFieldValidator.validateUserFields(updateUserDTO.getEmail(), "Email");
		UserFieldValidator.validateUserFields(updateUserDTO.getUsername(), "Username");
		// Mise à jour des champs de l'utilisateur
		if (updateUserDTO.getName() != null && !updateUserDTO.getName().trim().isEmpty()) {
			user.setName(updateUserDTO.getName());
			log.debug("Updated name for user {}", id);
		}
		if (updateUserDTO.getEmail() != null && !updateUserDTO.getEmail().trim().isEmpty()) {
			validateUniqueEmail(updateUserDTO.getEmail(), id);
			user.setEmail(updateUserDTO.getEmail());
			log.debug("Updated email for user {}", id);
		}

		if (updateUserDTO.getUsername() != null && !updateUserDTO.getUsername().trim().isEmpty()) {
			user.setUsername(updateUserDTO.getUsername());
			log.debug("Updated username for user {}", id);

		}

		UserInfo updateUserInfo = repository.save(user);
		log.info("Successfully updated user with id: {}", id);

		return userInfoMapper.userInfoToDTO(updateUserInfo);
	}

	public boolean updatePassword(Long id, String oldPassword, String newPassword) throws MissMatchedPasswordException {

		// Get user from bdd
		UserInfo userInfo = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id + NOT_FOUND));
		// Get encoded password from userInfo
		String encodedPassword = userInfo.getPassword();
		// Verify if old password is correct
		boolean isUserProvidedOldPassworCorrect = encoder.matches(oldPassword, encodedPassword);
		// if mismatch, throw exception
		if (!isUserProvidedOldPassworCorrect) {
			throw new MissMatchedPasswordException("Old password provided is incorrect");
		}
		// Save encoded new password
		userInfo.setPassword(encoder.encode(newPassword));
		repository.save(userInfo);
		log.info("Successfully updated password for user with id: {}", id);

		return true;
	}

	private void validateUniqueEmail(String email, Long excludeUserId) {
		if (Boolean.TRUE.equals(repository.existsByEmailAndIdNot(email, excludeUserId))) {
			throw new DuplicateUserException("Email already in use: " + email);
		}
	}

	public String validatePasswordResetToken(String token) {
		return passwordResetTokenService.validatePasswordResetToken(token);
	}

	public UserInfo findUserByPasswordToken(String passwordResetToken) {
		UserInfo userInfo = passwordResetTokenService.findUserByPasswordToken(passwordResetToken);
		if (userInfo == null) {
			log.debug("USER NOT FOUND WITH TOKEN : {}", passwordResetToken);
			throw new UserNotFoundException("User not found by token: " + passwordResetToken);
		}
		log.info("user found : {}", userInfo);
		return userInfo;
	}

}
