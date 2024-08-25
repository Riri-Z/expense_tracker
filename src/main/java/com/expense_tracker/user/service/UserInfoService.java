package com.expense_tracker.user.service;

import java.util.Optional;

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
		// TODO : check why UserNotFoundException return a status 200 with empty body.
		// and why id 6 is not found
		/*
		 * ui1_0.username=?
		 * 2024-08-25T05:34:58.216+02:00 ERROR 20776 --- [fitness_api] [nio-8080-exec-1]
		 * com.expense_tracker.jwt.JwtAuthFilter : Error processing JWT token
		 *
		 * org.springframework.security.core.userdetails.UsernameNotFoundException: User
		 * not found: zaza
		 * at
		 * com.expense_tracker.user.service.UserInfoService.lambda$loadUserByUsername$0(
		 * UserInfoService.java:44) ~[classes/:na]
		 * at java.base/java.util.Optional.orElseThrow(Optional.java:403) ~[na:na]
		 * at com.expense_tracker.user.service.UserInfoService.loadUserByUsername(
		 * UserInfoService.java:44) ~[classes/:na]
		 * at
		 * com.expense_tracker.jwt.JwtAuthFilter.doFilterInternal(JwtAuthFilter.java:52)
		 * ~[classes/:na]
		 * at org.springframework.web.filter.OncePerRequestFilter.doFilter(
		 * OncePerRequestFilter.java:116) ~[spring-web-6.1.12.jar:6.1.12]
		 * at org.springframework.security.web.
		 * ObservationFilterChainDecorator$ObservationFilter.wrapFilter(
		 * ObservationFilterChainDecorator.java:240)
		 * ~[spring-security-web-6.3.3.jar:6.3.3]
		 * at org.springframework.security.web.
		 * ObservationFilterChainDecorator$ObservationFilter.doFilter(
		 * ObservationFilterChainDecorator.java:227)
		 * ~[spring-security-web-6.3.3.jar:6.3.3]
		 */
		UserInfo user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id + " not found"));
		// Mise à jour des champs de l'utilisateur
		if (updateUserDTO.getName() != null) {
			user.setName(updateUserDTO.getName());
		}
		if (updateUserDTO.getEmail() != null) {
			user.setEmail(updateUserDTO.getEmail());
		}

		if (updateUserDTO.getEmail() != null) {
			user.setUsername(updateUserDTO.getUsername());
		}

		UserInfo updaUserInfo = repository.save(user);
		return userInfoMapper.userInfoToDTO(updaUserInfo);
	}

}
