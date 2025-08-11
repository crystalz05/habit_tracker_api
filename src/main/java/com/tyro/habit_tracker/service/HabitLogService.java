package com.tyro.habit_tracker.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
    
    public void checkAndCreateMissedLog() {
        LocalDate today = LocalDate.now();
        List<Habit> allHabits = habitRepository.findAll();

        for (Habit habit : allHabits) {
            LocalDate lastLogDate = habitLogRepository.findLatestHabitLogByHabitId(habit.getId())
                    .map(HabitLog::getCreatedAt) // HabitLog.getCreatedAt() -> LocalDate
                    .orElse(habit.getCreatedAt() != null ? habit.getCreatedAt().toLocalDate() : today);

            if (habit.getFrequency() == Frequency.DAILY) {
                handleDailyMissedLogs(habit, lastLogDate, today);
            } else if (habit.getFrequency() == Frequency.WEEKLY) {
                handleWeeklyMissedLogs(habit, lastLogDate, today);
            } else if (habit.getFrequency() == Frequency.MONTHLY) {
                handleMonthlyMissedLogs(habit, lastLogDate, today);
            }
        }
    }

    private void handleDailyMissedLogs(Habit habit, LocalDate lastLogDate, LocalDate today) {
        long daysBetween = ChronoUnit.DAYS.between(lastLogDate, today);
        if (daysBetween <= 0) return;

        // Create a missed log for each day after the last log up to today
        for (long i = 1; i <= daysBetween; i++) {
            LocalDate missedDate = lastLogDate.plusDays(i);
            if (!habitLogRepository.existsByHabitAndCreatedAt(habit, missedDate)) {
                saveMissedLogForDate(habit, missedDate, "You skipped " + missedDate, LogStatus.SKIPPED);
            }
        }
    }
    
    private void handleWeeklyMissedLogs(Habit habit, LocalDate lastLogDate, LocalDate today) {
        long weeksBetween = ChronoUnit.WEEKS.between(lastLogDate, today);
        if (weeksBetween <= 0) return;

        for (long i = 1; i <= weeksBetween; i++) {
            LocalDate missedDate = lastLogDate.plusWeeks(i);
            if (!habitLogRepository.existsByHabitAndCreatedAt(habit, missedDate)) {
                saveMissedLogForDate(habit, missedDate, "You skipped week of " + missedDate, LogStatus.SKIPPED);
            }
        }
    }
    

	private void handleMonthlyMissedLogs(Habit habit, LocalDate lastLogDate, LocalDate today) {
	    long monthsBetween = ChronoUnit.MONTHS.between(lastLogDate.withDayOfMonth(1), today.withDayOfMonth(1));
	    if (monthsBetween <= 0) return;
	
	    for (long i = 1; i <= monthsBetween; i++) {
	        LocalDate missedDate = lastLogDate.plusMonths(i);
	        if (!habitLogRepository.existsByHabitAndCreatedAt(habit, missedDate)) {
	            saveMissedLogForDate(habit, missedDate, "You skipped month starting " + missedDate.withDayOfMonth(1), LogStatus.SKIPPED);
	        }
	    }
	}

    private void saveMissedLogForDate(Habit habit, LocalDate date, String note, LogStatus status) {
        HabitLog missedLog = new HabitLog();
        missedLog.setHabit(habit);
        missedLog.setNote(note);
        missedLog.setStatus(status);
        missedLog.setCreatedAt(date);            // make sure HabitLog.createdAt is LocalDate
        habitLogRepository.save(missedLog);

        habit.setCompleted(false);
        habitRepository.save(habit);
    }
	
	

}