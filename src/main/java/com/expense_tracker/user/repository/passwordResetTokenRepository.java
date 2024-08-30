package com.expense_tracker.user.repository;

import com.expense_tracker.user.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class passwordResetTokenRepository extends JpaRepository<PasswordResetToken, Long>{

}
