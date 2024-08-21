package com.expense_tracker.user;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserInfoDetails implements UserDetails {

	private final String username; // Changed from 'name' to 'username' for clarity

	private final String password;

	private final List<GrantedAuthority> authorities;

	public UserInfoDetails(UserInfo userInfo) {
		this.username = userInfo.getName(); // Assuming 'name' is used as 'username'
		this.password = userInfo.getPassword();
		this.authorities = List.of(userInfo.getRoles().split(","))
			.stream()
			.map(SimpleGrantedAuthority::new) // equivalent to role -> new
												// SimpleGrantedAuthority(role)
			.collect(Collectors.toList());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO : implements this
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO : implements this
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO : implements this
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO : implements this
		return true;
	}

}
