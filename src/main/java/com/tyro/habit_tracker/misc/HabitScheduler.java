package com.tyro.habit_tracker.misc;


import org.springframework.stereotype.Service;

import com.tyro.habit_tracker.service.HabitLogService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HabitScheduler {
	
	private final HabitLogService habitLogService;
	
	public void checkMissedHabits() {
		habitLogService.createMissedLog();
	}
	
	
}
