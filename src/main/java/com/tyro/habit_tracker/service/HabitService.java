package com.tyro.habit_tracker.service;

import java.util.List;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tyro.habit_tracker.dto.HabitRequestDTO;
import com.tyro.habit_tracker.model.Habit;
import com.tyro.habit_tracker.model.User;
import com.tyro.habit_tracker.repository.HabitRepository;
import com.tyro.habit_tracker.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HabitService {
	
	private final HabitRepository habitRepository;
	private final UserRepository userRepository;
	
	public Habit createHabit(HabitRequestDTO habitRequestDTO) {
		
		if(habitRepository.existsByName(habitRequestDTO.getName())) {
			throw new IllegalArgumentException("Habit already exisit with name : "+habitRequestDTO.getName());
		}
		
		if(!userRepository.existsById(habitRequestDTO.getUserId())) {
			throw new IllegalArgumentException("User does not exist with id "+habitRequestDTO.getUserId());
		}
		
		User user = userRepository.findById(habitRequestDTO.getUserId()).get();
		
		Habit habit = Habit.builder()
				.name(habitRequestDTO.getName())
				.description(habitRequestDTO.getDescription())
				.frequency(habitRequestDTO.getFrequency())
				.user(user)
				.build();
		
		return habitRepository.save(habit);
	}
	
	public List<Habit> retrieveHabitsByUserId(Long userId){
		if(!userRepository.existsById(userId)) {
			throw new IllegalArgumentException("User does not exist with id "+userId);
		}
		
		return habitRepository.findAllByUserId(userId);
	}
	
	public Optional<Habit> retrieveHabitById(Long habitId) {
		if(!habitRepository.existsById(habitId)) {
			throw new IllegalArgumentException("Habit does not exist with id "+habitId);
		}
		
		return habitRepository.findById(habitId);
	}

}
