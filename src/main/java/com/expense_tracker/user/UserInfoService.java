package com.expense_tracker.user;

import java.util.Optional;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService implements UserDetailsService {

	private final UserInfoRepository repository;

	private final PasswordEncoder encoder;

	public UserInfoService(UserInfoRepository repository, PasswordEncoder encoder) {
		this.repository = repository;
		this.encoder = encoder;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserInfo> userDetail = repository.findByUsername(username);

		// Converting UserInfo to UserDetails
		return userDetail.map(UserInfoDetails::new)
			.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
	}

	public String addUser(UserInfo userInfo) {
		// verify if username or email already exist
		if (Boolean.TRUE.equals(repository.existsByEmail(userInfo.getEmail()))) {
			throw new DuplicateKeyException("Email already exists :" + userInfo.getEmail());
		}
		if (Boolean.TRUE.equals(repository.existsByUsername((userInfo.getUsername())))) {
			throw new DuplicateKeyException("Username already exists :" + userInfo.getUsername());
		}
		// Encode password before saving the user
		userInfo.setPassword(encoder.encode(userInfo.getPassword()));
		repository.save(userInfo);
		return "User Added Successfully";
	}

}
