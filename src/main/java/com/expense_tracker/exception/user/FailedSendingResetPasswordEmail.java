package com.expense_tracker.exception.user;

public class FailedSendingResetPasswordEmail extends UserException {

	public FailedSendingResetPasswordEmail(String message) {
		super(message);
	}

}