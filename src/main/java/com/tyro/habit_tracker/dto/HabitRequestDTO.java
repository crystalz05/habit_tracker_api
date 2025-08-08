package com.tyro.habit_tracker.dto;



import com.tyro.habit_tracker.misc.Frequency;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HabitRequestDTO {
	
	@NotBlank(message = "Name is required")
	@Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
	private String name;

	@NotBlank(message = "Description is required")
	@Size(min = 5, max = 500, message = "Description must be between 5 and 500 characters")
	private String description;
	
    @NotBlank(message = "Frequency is required")
    private Frequency frequency;
	
	@NotNull(message = "User Id is required")
	private Long userId;
}
