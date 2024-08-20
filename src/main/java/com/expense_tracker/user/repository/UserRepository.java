package com.expense_tracker.user.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.expense_tracker.user.model.UserModel;

@Repository
public interface UserRepository extends CrudRepository<UserModel, Long> {

}
