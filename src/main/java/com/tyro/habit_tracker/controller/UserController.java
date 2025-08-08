package com.tyro.habit_tracker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tyro.habit_tracker.dto.UserResponseDTO;
import com.tyro.habit_tracker.security.JwtUtil;
import com.tyro.habit_tracker.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
	
	private final UserService userService;
	private final JwtUtil jwtUtil;
	
	@DeleteMapping("/delete/{userId}")
	public ResponseEntity<String> deleteUserbyId(@PathVariable Long userId){
		try {
			userService.deleteUserById(userId);		
			return ResponseEntity.status(HttpStatus.OK).body("User Deleted Successfully");			
		}catch (UsernameNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");						
		}
	}
	
	@GetMapping("/me")
	public ResponseEntity<UserResponseDTO> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
	    String token = authHeader.replace("Bearer ", "");
	    String email = jwtUtil.extractUsername(token);
	    UserResponseDTO userResponseDTO = userService.getUserByUsername(email);
	    return ResponseEntity.ok(userResponseDTO);
	}

}
