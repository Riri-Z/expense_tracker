package com.expense_tracker.user.dto;

import java.util.List;

import com.expense_tracker.subscription.entity.Subscription;
import com.expense_tracker.subscription.entity.UserSubscription;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserInfoSubscriptionsDTO extends UserInfoDTO {

	private List<Subscription> subscriptions;

	private List<UserSubscription> userSubscription;

}
