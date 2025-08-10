package com.tyro.habit_tracker.dto;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

import com.tyro.habit_tracker.misc.Frequency;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class HabitResponseDTO {
	

    @NotNull(message = "ID is required")
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Creation date is required")
    private LocalDateTime createdAt;

    @NotNull(message = "Update date is required")
    private LocalDateTime updatedAt;

    private boolean completed;
    
    private DayOfWeek dayofWeekReminder;

    @NotNull(message = "Streak count is required")
    private Long streakCount;

    @NotNull(message = "Frequency is required")
    private Frequency frequency;

    @NotNull(message = "User ID is required")
    private Long userId;
}
