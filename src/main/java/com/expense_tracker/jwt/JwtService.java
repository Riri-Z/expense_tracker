package com.expense_tracker.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtService {

	@Value("${jwt.secret}")
	private String secret;

	@PostConstruct
	public void init() {
		if (secret == null || secret.trim().isEmpty()) {
			throw new IllegalStateException("JWT secret is not set. Please set the jwt.secret property.");
		}
	}

	// Generate token with given user name
	public String generateToken(String id) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, id);
	}

	// Create a JWT token with specified claims and subject (user name)
	private String createToken(Map<String, Object> claims, String id) {
		return Jwts.builder()
			.setClaims(claims)
			.setSubject(id)
			.setIssuedAt(new Date())
			// Token valid for 30 minutes
			.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
			.signWith(getSignKey(), SignatureAlgorithm.HS256)
			.compact();
	}

	// Get the signigenerateTokenng key for JWT token
	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	// Extract the username from the token
	public String extractId(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	// Extract the expiration date from the token
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	// Extract a claim from the token
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	// Extract all claims from the token
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
	}

	// Check if the token is expired
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	// Validate the token against user details and expiration
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractId(token);
		boolean usernameMatches = username.equals(userDetails.getUsername());
		boolean tokenNotExpired = !isTokenExpired(token);

		return usernameMatches && tokenNotExpired;
	}

}
