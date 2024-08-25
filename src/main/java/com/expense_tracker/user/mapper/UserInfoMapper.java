package com.expense_tracker.user.mapper;

import org.springframework.stereotype.Component;

import com.expense_tracker.user.dto.UserInfoDTO;
import com.expense_tracker.user.entity.UserInfo;

@Component
public class UserInfoMapper {

	public UserInfoDTO userInfoToDTO(UserInfo userInfo) {
		return new UserInfoDTO(userInfo.getId(), userInfo.getName(), userInfo.getUsername(), userInfo.getEmail(),
				userInfo.getRoles());
	}

}
