package com.tyro.habit_tracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordForm {
	
	@NotBlank(message = "Password is required")
    private String token;
    
	@NotBlank(message = "Password is required")
	@Size(min = 6, max = 100, message = "Password mustbe at least 6 characters")
    private String password;
    
	@NotBlank(message = "Password is required")
	@Size(min = 6, max = 100, message = "Password mustbe at least 6 characters")
    private String confirmPassword;

}
