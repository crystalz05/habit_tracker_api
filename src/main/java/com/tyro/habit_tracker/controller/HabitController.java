package com.tyro.habit_tracker.controller;

import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tyro.habit_tracker.dto.HabitRequestDTO;
import com.tyro.habit_tracker.dto.HabitResponseDTO;
import com.tyro.habit_tracker.model.Habit;
import com.tyro.habit_tracker.service.HabitService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/habit")
public class HabitController {
	
	private final HabitService habitService;
	
	@PostMapping("/add-habit")
	public ResponseEntity<String> createHabit(@Valid @RequestBody HabitRequestDTO habitRequestDTO){
		try{
			habitService.createHabit(habitRequestDTO);
			return ResponseEntity.status(HttpStatus.CREATED).body("New habit added successfully");
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@GetMapping("/retrieve-habit/{habitId}")
	public ResponseEntity<HabitResponseDTO> retrieveHabitById(@Valid @PathVariable Long habitId){
		
			Habit habit = habitService.retrieveHabitById(habitId).orElseThrow();
			
			HabitResponseDTO habitResponse = new HabitResponseDTO(
	                habit.getId(),
	                habit.getName(),
	                habit.getDescription(),
	                habit.getCreatedAt(),
	                habit.getUpDatedAt(),
	                habit.getCompleted(),
	                habit.getStreakCount(),
	                habit.getFrequency(),
	                habit.getUser().getId()
	        );
			
			return ResponseEntity.status(HttpStatus.OK).body(habitResponse);
	}
	
	@GetMapping("/retrieve-all-habits/{userId}")
	public ResponseEntity<List<HabitResponseDTO>> retrieveAllHabitsByUserId(@PathVariable Long userId){
			List<Habit> habits = habitService.retrieveHabitsByUserId(userId);
			
			List<HabitResponseDTO> habitResponse = habits.stream()
					.map(habit -> new HabitResponseDTO(
							habit.getId(),
			                habit.getName(),
			                habit.getDescription(),
			                habit.getCreatedAt(),
			                habit.getUpDatedAt(),
			                habit.getCompleted(),
			                habit.getStreakCount(),
			                habit.getFrequency(),
			                habit.getUser().getId()
	        )).toList();
		
			return ResponseEntity.status(HttpStatus.OK).body(habitResponse);
	}
	
}
