package com.expense_tracker.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.expense_tracker.user.service.UserInfoService;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	private final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

	private final JwtService jwtService;

	private final UserInfoService userInfoService;

	public JwtAuthFilter(JwtService jwtService, UserInfoService userInfoService) {
		this.jwtService = jwtService;
		this.userInfoService = userInfoService;
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {
		try {

			// Retrieve the Authorization header
			String authHeader = request.getHeader("Authorization");
			String token = null;
			String id = null;

			// Check if the header starts with "Bearer "
			if (authHeader != null && authHeader.startsWith("Bearer ")) {

				token = authHeader.substring(7); // Extract token
				id = jwtService.extractId(token); // Extract id from
													// token
			}
			// If the token is valid and no authentication is set in the context
			if (id != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				UserDetails userDetails = userInfoService.loadUserById(Long.valueOf(id));

				// Validate token and set authentication
				if (Boolean.TRUE.equals((jwtService.validateToken(token, userDetails)))) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
							null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
				else {
					log.warn("Invalid JWT token for user: {}", id);
				}
			}

			// Continue the filter chain
			filterChain.doFilter(request, response);
		}
		catch (ServletException | java.io.IOException | UsernameNotFoundException e) {
			log.error("Error processing JWT token", e);

		}
	}

}
