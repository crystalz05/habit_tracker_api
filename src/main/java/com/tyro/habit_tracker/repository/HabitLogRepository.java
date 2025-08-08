package com.tyro.habit_tracker.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tyro.habit_tracker.model.Habit;
import com.tyro.habit_tracker.model.HabitLog;

public interface HabitLogRepository extends JpaRepository<HabitLog, Long> {
	
	List<HabitLog> findAllByHabitId(Long habitId);
	boolean existsByHabitId(Long id);
	boolean existsByHabitAndCreatedAt(Habit habit, LocalDate date);


}
