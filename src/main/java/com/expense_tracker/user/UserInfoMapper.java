package com.expense_tracker.user;

import org.springframework.stereotype.Component;

@Component
public class UserInfoMapper {

	public UserInfoDTO userInfoToDTO(UserInfo userInfo) {
		return new UserInfoDTO(userInfo.getId(), userInfo.getName(), userInfo.getUsername(), userInfo.getEmail(),
				userInfo.getRoles());
	}

}
