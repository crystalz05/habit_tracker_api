package com.tyro.habit_tracker.dto;

import com.tyro.habit_tracker.misc.LogStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class HabitLogRequestDTO {
	
	@NotBlank(message = "A note is required")
	private String note;
	
	@NotNull(message = "Status is required")
	private LogStatus status;
	
	@NotNull(message = "Habit id is required")
	private Long habitId;

}
