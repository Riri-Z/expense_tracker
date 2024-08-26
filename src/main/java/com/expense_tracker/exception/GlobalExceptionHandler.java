package com.expense_tracker.exception;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.expense_tracker.exception.user.DuplicateUserException;
import com.expense_tracker.exception.user.UserAccessDenied;
import com.expense_tracker.exception.user.UserException;
import com.expense_tracker.exception.user.UserNotFoundException;

@ControllerAdvice
/*
 * @Order(Ordered.HIGHEST_PRECEDENCE) // To be sure that this class is called before
 * classes // provided by spring
 */
public class GlobalExceptionHandler {

	private static final Logger logg = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(UserAccessDenied.class)
	public ResponseEntity<ErrorResponse> handleAccessDeniedException(UserAccessDenied ex) {
		logg.warn("Access denied: {}", ex.getMessage());

		ErrorResponse error = new ErrorResponse(HttpStatus.FORBIDDEN.value(), "AccessDenied", ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
		logg.warn("Access denied: {}", ex.getMessage());

		ErrorResponse error = new ErrorResponse(HttpStatus.FORBIDDEN.value(), "AccessDenied", ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
	}

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

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
		logg.error("Unexpected error occurred: ", ex);
		ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error",
				"An unexpected error occurred");
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(UserException.class)
	public ResponseEntity<ErrorResponse> handleUserException(UserException ex) {
		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "User operation failed",
				ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

}