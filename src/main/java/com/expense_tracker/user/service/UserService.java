package com.expense_tracker.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.expense_tracker.user.model.UserModel;
import com.expense_tracker.user.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public Iterable<UserModel> findAllUser() {
		return this.userRepository.findAll();
	}

	public Optional<UserModel> findUser(Long id) {
		return this.userRepository.findById(id);
	}

}
