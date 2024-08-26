package com.expense_tracker.user.utils;

import com.expense_tracker.exception.user.UserException;

public class UserFieldValidator {

	// Prevent to instanciate this class, because this is a static one
	private UserFieldValidator() {
		throw new IllegalStateException("Utility class");
	}

	public static void validateUserFields(String value, String fieldName) {
		if (value == null || value.trim().isEmpty()) {
			throw new UserException(fieldName + " cannot be empty");
		}
	}

}
