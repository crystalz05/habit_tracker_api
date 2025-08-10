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

import com.tyro.habit_tracker.dto.HabitLogRequestDTO;
import com.tyro.habit_tracker.dto.HabitLogResponseDTO;
import com.tyro.habit_tracker.model.HabitLog;
import com.tyro.habit_tracker.service.HabitLogService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/habitlog")
public class HabitLogController {
	
	private final HabitLogService habitLogService;
	
	@PostMapping("/new-habit")
	public ResponseEntity<String> addNewHabitLog(@Valid @RequestBody HabitLogRequestDTO habitLogRequestDTO){
		
		try {			
			habitLogService.addNewHabitLog(habitLogRequestDTO);
			return ResponseEntity.status(HttpStatus.CREATED).body("New habit log added successfully");
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	
	@GetMapping("/retrieve-habit-log/{logId}")
	public ResponseEntity<HabitLogResponseDTO> retrieveHabitLogById(@Valid @PathVariable Long logId){
		
		HabitLog habitLog = habitLogService.retrieveLogById(logId);
		
		HabitLogResponseDTO habitLogResponseDTO = HabitLogResponseDTO.builder()
				.id(habitLog.getId())
				.status(habitLog.getStatus())
				.note(habitLog.getNote())
				.createdAt(habitLog.getCreatedAt())
				.updatedAt(habitLog.getUpdatedAt())
				.habitId(habitLog.getHabit().getId())
		        .build();
		
		return ResponseEntity.status(HttpStatus.OK).body(habitLogResponseDTO);
	}
	
	@GetMapping("/retrieve-all-logs/{habitId}")
	public ResponseEntity<List<HabitLogResponseDTO>> retrieveAllHabitLogsByHabitId(Long habitId){
		
		List<HabitLog> habitLogs = habitLogService.retrieveAllHabitLogsByHabitId(habitId);
		
				
		List<HabitLogResponseDTO> habitLogsResponse = habitLogs.stream()
				.map(habitLog -> HabitLogResponseDTO.builder()
						.id(habitLog.getId())
						.status(habitLog.getStatus())
						.note(habitLog.getNote())
						.createdAt(habitLog.getCreatedAt())
						.updatedAt(habitLog.getUpdatedAt())
						.habitId(habitLog.getHabit().getId())
						.build()
						).toList();
		return ResponseEntity.status(HttpStatus.OK).body(habitLogsResponse);
			
	}
	
}
//
//List<HabitResponseDTO> habitResponse = habits.stream()
//.map(habit -> new HabitResponseDTO(
