package com.tyro.habit_tracker.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tyro.habit_tracker.dto.HabitLogRequestDTO;
import com.tyro.habit_tracker.misc.LogStatus;
import com.tyro.habit_tracker.model.Habit;
import com.tyro.habit_tracker.model.HabitLog;
import com.tyro.habit_tracker.repository.HabitLogRepository;
import com.tyro.habit_tracker.repository.HabitRepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HabitLogService {
	
	private final HabitLogRepository habitLogRepository;
	private final HabitRepository habitRepository;
	
	public HabitLog addNewHabitLog(HabitLogRequestDTO habitLogRequestDTO) {
		
		if(!habitRepository.existsById(habitLogRequestDTO.getHabitId())) {
			throw new IllegalArgumentException("No habit exists withh id "+habitLogRequestDTO.getHabitId());
		}
		
		Habit habit = habitRepository.findById(habitLogRequestDTO.getHabitId()).orElseThrow();
		
		HabitLog habitLog = HabitLog.builder()
				.status(habitLogRequestDTO.getStatus())
				.note(habitLogRequestDTO.getNote())
				.habit(habit)
				.build();
		
		return habitLogRepository.save(habitLog);
		
	}
	
	public HabitLog retrieveLogById(Long id) {
		return habitLogRepository.findById(id).orElseThrow();
	}
	
	public List<HabitLog> retriveAllHabitLogsByHabitId(Long habitId){
		
		if(!habitLogRepository.existsByHabitId(habitId)) {
			throw new IllegalArgumentException("No habit exists withh id "+habitId);
		}
		return habitLogRepository.findAllByHabitId(habitId);
	}
	
	
	public void createMissedLog() {
		
		LocalDate yesterday = LocalDate.now().minusDays(1);
		
		List<Habit> allHabits = habitRepository.findAll();
		
		for (Habit habit: allHabits) {
			
			boolean alreadyLogged = habitLogRepository.existsByHabitAndCreatedAt(habit, yesterday);
			if(!alreadyLogged) {
				HabitLog missedLog = new HabitLog();
				
				missedLog.setHabit(habit);
				missedLog.setNote("You skipped today");
				missedLog.setStatus(LogStatus.SKIPPED);
				habitLogRepository.save(missedLog);
			}
			habit.setCompleted(false);
		}
	}

}