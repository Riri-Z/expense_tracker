package com.expense_tracker.user.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.expense_tracker.user.entity.UserInfo;

public class UserInfoDetails implements UserDetails {

	private final String name;

	private final String username;

	private final String password;

	private final List<GrantedAuthority> authorities;

	public UserInfoDetails(UserInfo userInfo) {
		this.name = userInfo.getName();
		this.username = userInfo.getUsername();
		this.password = userInfo.getPassword();
		this.authorities = List.of(userInfo.getRoles().split(","))
			.stream()
			// equivalent to role -> newSimpleGrantedAuthority(role)
			.map(SimpleGrantedAuthority::new)
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

	public String getName() {
		return name;
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
