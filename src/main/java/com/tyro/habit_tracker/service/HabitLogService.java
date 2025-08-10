package com.tyro.habit_tracker.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tyro.habit_tracker.dto.HabitLogRequestDTO;
import com.tyro.habit_tracker.misc.Frequency;
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
        Habit habit = habitRepository.findById(habitLogRequestDTO.getHabitId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No habit exists with id " + habitLogRequestDTO.getHabitId()));

        HabitLog habitLog = HabitLog.builder()
                .status(habitLogRequestDTO.getStatus())
                .note(habitLogRequestDTO.getNote())
                .habit(habit)
                .build();

        return habitLogRepository.save(habitLog);
    }
	
	
    public HabitLog retrieveLogById(Long id) {
        return habitLogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No log exists with id " + id));
    }
	

    public List<HabitLog> retrieveAllHabitLogsByHabitId(Long habitId) {
        if (!habitRepository.existsById(habitId)) {
            throw new IllegalArgumentException("No habit exists with id " + habitId);
        }
        return habitLogRepository.findAllByHabitId(habitId);
    }
    
    public void createMissedLog() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<Habit> allHabits = habitRepository.findAll();

        for (Habit habit : allHabits) {
            if (isDailyHabit(habit)) {
                handleDailyMissedLog(habit, yesterday);
            } else if (isWeeklyHabit(habit)) {
                handleWeeklyMissedLog(habit, yesterday);
            }
        }
    }
    
    private boolean isDailyHabit(Habit habit) {
        return habit.getFrequency() == Frequency.DAILY;
    }

    private boolean isWeeklyHabit(Habit habit) {
        return habit.getFrequency() == Frequency.WEEKLY;
    }

    private void handleDailyMissedLog(Habit habit, LocalDate date) {
        if (!habitLogRepository.existsByHabitAndCreatedAt(habit, date)) {
            saveMissedLog(habit, "You skipped today", LogStatus.SKIPPED);
        }
    }

    private void handleWeeklyMissedLog(Habit habit, LocalDate yesterday) {
        if (habit.getDayofWeekReminder().equals(yesterday.getDayOfWeek())
                && !alreadyLoggedThisWeek(habit, yesterday)) {
            saveMissedLog(habit, "You skipped this week", LogStatus.SKIPPED);
        }
    }

    private boolean alreadyLoggedThisWeek(Habit habit, LocalDate yesterday) {
        for (int i = 0; i < 7; i++) {
            if (habitLogRepository.existsByHabitAndCreatedAt(habit, yesterday.minusDays(i))) {
                return true;
            }
        }
        return false;
    }

    private void saveMissedLog(Habit habit, String note, LogStatus status) {
        HabitLog missedLog = new HabitLog();
        missedLog.setHabit(habit);
        missedLog.setNote(note);
        missedLog.setStatus(status);

        habitLogRepository.save(missedLog);

        habit.setCompleted(false);
        habitRepository.save(habit);
    }
	
	

}