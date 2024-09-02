package com.expense_tracker.password.util;

import lombok.Data;

@Data
public class PasswordResetRequestUtil {

	private String email;

	private String newPassword;

	private String confirmPassword;

}
