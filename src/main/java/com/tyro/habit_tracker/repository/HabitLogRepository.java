package com.tyro.habit_tracker.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tyro.habit_tracker.model.Habit;
import com.tyro.habit_tracker.model.HabitLog;

public interface HabitLogRepository extends JpaRepository<HabitLog, Long> {
	
	List<HabitLog> findAllByHabitId(Long habitId);
	boolean existsByHabitId(Long id);
	boolean existsByHabitAndCreatedAt(Habit habit, LocalDate date);
	
	
	@Query(value = "SELECT * FROM habits_log WHERE habit_id = :habitId ORDER BY created_at DESC LIMIT 1", nativeQuery = true)
	Optional<HabitLog> findLatestHabitLogByHabitId(Long habitId);
}
