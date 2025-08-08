package com.tyro.habit_tracker.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.tyro.habit_tracker.misc.LogStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HabitLogResponseDTO {
	
    private Long id;
    private LogStatus status;
    private String note;
    private LocalDate createdAt;
    private LocalDateTime updatedAt;
    private Long habitId;
}