package com.tyro.habit_tracker.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tyro.habit_tracker.model.User;
import com.tyro.habit_tracker.model.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
	
    Optional<VerificationToken> findByToken(String token);
    Optional<VerificationToken> findByUser(User user);
    boolean existsByUser(User user);

}
