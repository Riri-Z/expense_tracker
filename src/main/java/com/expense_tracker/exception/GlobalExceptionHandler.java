package com.expense_tracker.exception;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DuplicateKeyException.class)
	public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(DuplicateKeyException ex) {
		ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT.value(), "User registration failed",
				ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}

}