package com.tyro.habit_tracker.controller;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tyro.habit_tracker.dto.AuthResponse;
import com.tyro.habit_tracker.dto.JwtResponse;
import com.tyro.habit_tracker.dto.LoginRequest;
import com.tyro.habit_tracker.dto.ResetPasswordForm;
import com.tyro.habit_tracker.dto.UserDTO;
import com.tyro.habit_tracker.service.UserService;
import com.tyro.habit_tracker.service.VerificationTokenService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
	

	private final UserService userService;
	private final VerificationTokenService tokenService;
	
	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@Valid @RequestBody UserDTO userDTO){
		try {
			userService.registerUser(userDTO);
			return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully! Please verify your email.");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<AuthResponse<JwtResponse>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
		try {
			JwtResponse jwtResponse = userService.authenticate(loginRequest);
	        return ResponseEntity.ok(new AuthResponse<>(true, jwtResponse, "Authentication successful"));
		}catch (Exception e) {
	        return ResponseEntity
	                .status(HttpStatus.UNAUTHORIZED)
	                .body(new AuthResponse<>(false, null, e.getMessage()));		}
	}
	
	@GetMapping("/verify")
	public ResponseEntity<String> verifyToken(@RequestParam String token, @RequestParam String email) {
	    boolean verified = tokenService.verifyToken(email, token);

	    if (verified) {
	        return ResponseEntity.ok("Verified successfully");
	    } else {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                             .body("Invalid or expired token");
	    }
	}
	
	@GetMapping("/reset-password")
	public ResponseEntity<String> showResetPasswordForm(@RequestParam String email) {
		
		try {
		    tokenService.sendResetPasswordEmail(email);
	        return ResponseEntity.status(HttpStatus.OK).body("Reset email sent successfully");
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
}







