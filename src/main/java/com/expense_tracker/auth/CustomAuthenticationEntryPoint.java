package com.expense_tracker.auth;

import java.io.OutputStream;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.expense_tracker.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * Custom implementation for AuthenticationEntryPoint to handle failures.
 * This component is responsible for sending a custom error response when authentication fails
 * NB for myself : HttpServletResponse has an method outputStream
 * outputStream aims to write data in for RESPONSE
 * VS
 * inputStream aims to read data  from HttpServletRequest (payload from clients)
 * ObjectMapper provides functionality for reading and writing JSON, either to and from basic POJOs (Plain Old Java Objects)
 * flush allows to be sure that all data are sent to the client
 */
@Component("customAuthenticationEntryPoint")
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException, java.io.IOException {

		// Set the response to 401 unauthorized
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		// Set the response type to JSON
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		// Init custom errorResposne
		ErrorResponse errorResponse = new ErrorResponse(HttpServletResponse.SC_FORBIDDEN, "Authentication failed",
				authException.getMessage());
		// Get the output stream to write response
		OutputStream responseStream = response.getOutputStream();
		// convert errorResposne to json
		ObjectMapper mapper = new ObjectMapper();
		// write the errorResponse to JSON
		mapper.writeValue(responseStream, errorResponse);
		// ensure all content is written in json, and sent to the client
		responseStream.flush();
	}

}
