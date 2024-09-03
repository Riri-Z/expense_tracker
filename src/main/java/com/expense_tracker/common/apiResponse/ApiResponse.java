package com.expense_tracker.common.apiResponse;

import org.springframework.http.ResponseEntity;

import lombok.Data;

@Data
public class ApiResponse<T> {

	private String status;

	private T details;

	public ApiResponse(String status, T details) {
		this.status = status;
		this.details = details;
	}

	public static <T> ResponseEntity<ApiResponse<T>> success(T details) {

		return ResponseEntity.ok(new ApiResponse<>("success", details));
	}

}
