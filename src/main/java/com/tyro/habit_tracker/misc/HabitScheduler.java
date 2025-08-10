package com.tyro.habit_tracker.misc;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tyro.habit_tracker.service.HabitLogService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HabitScheduler {
	
	private final HabitLogService habitLogService;
	
    @Scheduled(cron = "0 1 0 * * *")
	public void checkMissedHabits() {
		habitLogService.createMissedLog();
	}
	
}
