package com.expense_tracker.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.expense_tracker.jwt.JwtAuthFilter;
import com.expense_tracker.user.service.UserInfoService;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity(debug = true)
public class SecurityConfig {

	private final UserInfoService userInfoService;

	@Qualifier("customAuthenticationEntryPoint")
	private final AuthenticationEntryPoint authenticationEntryPoint;

	public SecurityConfig(@Lazy UserInfoService userInfoService, AuthenticationEntryPoint authenticationEntryPoint) {
		this.userInfoService = userInfoService;
		this.authenticationEntryPoint = authenticationEntryPoint;
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return userInfoService;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter authFilter,
			AuthenticationProvider authenticationProvider) throws Exception {

		return http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.csrf(csrf -> csrf.disable()) // disable for stateless apis
			.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
				.requestMatchers("/auth/welcome", "/auth/addNewUser", "/auth/generateToken", "/request-password-reset",
						"/reset-password*", "/swagger-ui/**", "/v3/api-docs/**")
				.permitAll()
				.requestMatchers("/auth/user/**")
				.hasAnyRole("USER", "ADMIN")
				.requestMatchers("/auth/admin/**")
				.hasAuthority("ROLE_ADMIN")
				.anyRequest()
				.authenticated() // Protect all other endpoints

			)
			// No sessions
			.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			// Custom authentication provider
			.authenticationProvider(authenticationProvider)
			// Add JWT filter
			.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(authenticationEntryPoint))
			.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		// TODO ; UPDATE URL
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); // Password encoding
	}

	@Bean
	public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);
		return authenticationProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

}
