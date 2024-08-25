package com.expense_tracker.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.expense_tracker.exception.user.DuplicateUserException;
import com.expense_tracker.exception.user.UserException;
import com.expense_tracker.exception.user.UserNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handlerMethodValidationException(MethodArgumentNotValidException ex) {
		// Init a map to store all validation exception
		Map<String, String> errors = new HashMap<>();

		// Get errors validation details through BindingResult
		BindingResult exBindingResult = ex.getBindingResult();
		// Get all errors from BindingResult
		// it includes filds erros (FieldError) and globals errors (ObjectError)
		List<ObjectError> allErrors = exBindingResult.getAllErrors();

		allErrors.forEach((error -> {
			// Get fieldName error, we add type otherwise we cannot .getField()
			String fieldName = ((FieldError) error).getField();
			// Get Error message
			String errorMessage = error.getDefaultMessage();
			// add the error into map
			errors.put(fieldName, errorMessage);
		}));

		// Create custom error response
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Validation failed",
				errors.toString());

		// return responsEntity with errrorResponse and the status BAD_REQUEST
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(DuplicateUserException.class)
	public ResponseEntity<ErrorResponse> handleDuplicateUserException(DuplicateUserException ex) {

		// Create custom error response
		ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT.value(), "User registration failed",
				ex.getMessage());
		// return responsEntity with errrorResponse and the status CONFLICT

		return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleMissingUserException(UserNotFoundException ex) {
		ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "User not found", ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserException.class)
	public ResponseEntity<ErrorResponse> handleUserException(UserException ex) {
		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "User operation failed",
				ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

}