package com.tyro.habit_tracker.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tyro.habit_tracker.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findById(Long id);
	Optional<User> findByEmail(String email);
	boolean existsByEmail(String email);
	boolean existsByUsername(String username);

}
