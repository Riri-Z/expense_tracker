package com.expense_tracker.common.apiResponse;

import org.springframework.http.ResponseEntity;

import lombok.Data;

@Data
public class ApiResponse<T> {

	private String status;

	private T result;

	public ApiResponse(String status, T result) {
		this.status = status;
		this.result = result;
	}

	public static <T> ResponseEntity<ApiResponse<T>> success(T result) {

		return ResponseEntity.ok(new ApiResponse<>("success", result));
	}

}
