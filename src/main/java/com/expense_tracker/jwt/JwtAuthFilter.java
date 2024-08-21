package com.expense_tracker.jwt;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.expense_tracker.user.UserInfoService;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtService jwtService;

	private final UserInfoService userDetailsService;

	public JwtAuthFilter(JwtService jwtService, UserInfoService userDetailsService) {
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain)
			throws ServletException, IOException, java.io.IOException {
		// Retrieve the Authorization header
		String authHeader = request.getHeader("Authorization");
		String token = null;
		String username = null;

		// Check if the header starts with "Bearer "
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7); // Extract token
			username = jwtService.extractUsername(token); // Extract username from token
		}
		// If the token is valid and no authentication is set in the context
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);

			// Validate token and set authentication
			if (Boolean.TRUE.equals(jwtService.validateToken(token, userDetails))) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}

		// Continue the filter chain
		filterChain.doFilter(request, response);
	}

}
