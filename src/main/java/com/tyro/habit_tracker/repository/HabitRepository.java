package com.tyro.habit_tracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tyro.habit_tracker.model.Habit;

public interface HabitRepository extends JpaRepository<Habit, Long> {
	
	boolean existsByName(String habitName);
	boolean existsByUserId(Long userId);
	Optional<Habit> findByName(String habitName);
	List<Habit> findAllByUserId(Long userId);
	

}
