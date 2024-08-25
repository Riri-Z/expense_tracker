package com.expense_tracker.user.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.expense_tracker.exception.user.DuplicateUserException;
import com.expense_tracker.exception.user.UserException;
import com.expense_tracker.exception.user.UserNotFoundException;
import com.expense_tracker.user.dto.UpdateUserDTO;
import com.expense_tracker.user.dto.UserInfoDTO;
import com.expense_tracker.user.entity.UserInfo;
import com.expense_tracker.user.mapper.UserInfoMapper;
import com.expense_tracker.user.repository.UserInfoRepository;

import jakarta.validation.Valid;

@Service
public class UserInfoService implements UserDetailsService {

	private static final Logger log = LoggerFactory.getLogger(UserInfoService.class);

	private final UserInfoRepository repository;

	private final PasswordEncoder encoder;

	private final UserInfoMapper userInfoMapper;

	public UserInfoService(UserInfoRepository repository, PasswordEncoder encoder, UserInfoMapper userInfoMapper) {
		this.repository = repository;
		this.encoder = encoder;
		this.userInfoMapper = userInfoMapper;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserInfo> userDetail = repository.findByUsername(username);

		// Converting UserInfo to UserDetails
		return userDetail.map(UserInfoDetails::new)
			.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
	}

	public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        UserInfo user = repository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

        return new UserInfoDetails(user);
    }

	@Validated
	public UserInfoDTO addUser(@Valid UserInfo userInfo) {
		// verify if username or email already exist
		if (userInfo.getEmail() == null || userInfo.getEmail().trim().isEmpty()) {
			throw new UserException("Email cannot be empty");
		}
		if (userInfo.getUsername() == null || userInfo.getUsername().trim().isEmpty()) {
			throw new UserException("Username cannot be empty");
		}
		if (userInfo.getPassword() == null || userInfo.getPassword().trim().isEmpty()) {
			throw new UserException("Password cannot be empty");
		}
		if (userInfo.getRoles() == null || userInfo.getRoles().trim().isEmpty()) {
			throw new UserException("Roles cannot be empty");
		}

		// Vérifications d'unicité
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

	public UserInfoDTO updateUser(Long id, UpdateUserDTO updateUserDTO) {
		log.info("Updating user with id: {}", id);

		UserInfo user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id + " not found"));

		// Mise à jour des champs de l'utilisateur
		if (updateUserDTO.getName() != null) {
			user.setName(updateUserDTO.getName());
			log.debug("Updated name for user {}", id);
		}
		if (updateUserDTO.getEmail() != null) {
			validateUniqueEmail(updateUserDTO.getEmail(), id);
			user.setEmail(updateUserDTO.getEmail());
			log.debug("Updated email for user {}", id);
		}

		if (updateUserDTO.getUsername() != null) {
			user.setUsername(updateUserDTO.getUsername());
			log.debug("Updated username for user {}", id);

		}

		UserInfo updaUserInfo = repository.save(user);
		log.info("Successfully updated user with id: {}", id);

		return userInfoMapper.userInfoToDTO(updaUserInfo);
	}

	private void validateUniqueEmail(String email, Long excludeUserId) {
		if (Boolean.TRUE.equals(repository.existsByEmailAndIdNot(email, excludeUserId))) {
			throw new DuplicateUserException("Email already in use: " + email);
		}
	}

}
