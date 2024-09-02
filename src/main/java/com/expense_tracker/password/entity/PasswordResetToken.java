package com.expense_tracker.password.entity;

import java.util.Calendar;
import java.util.Date;

import com.expense_tracker.user.entity.UserInfo;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class PasswordResetToken {

	private static final int EXPIRATION_TIME = 100;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String token;

	private Date expirationTime;

	@OneToOne(targetEntity = UserInfo.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_info_id")
	private UserInfo userInfo;

	public PasswordResetToken(String token, UserInfo userInfo) {
		this.token = token;
		this.userInfo = userInfo;
		this.expirationTime = this.getTokenExpirationTime();
	}

	public PasswordResetToken(String token) {
		this.token = token;
		this.expirationTime = this.getTokenExpirationTime();
	}

	public final Date getTokenExpirationTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(new Date().getTime());
		calendar.add(Calendar.MINUTE, EXPIRATION_TIME);
		return new Date(calendar.getTime().getTime());
	}

}
