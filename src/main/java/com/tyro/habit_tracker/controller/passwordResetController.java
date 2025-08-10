package com.tyro.habit_tracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tyro.habit_tracker.dto.ResetPasswordForm;
import com.tyro.habit_tracker.service.UserService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class passwordResetController {

	private final UserService userService;
	
    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "reset-password-form";
    }
    

    @PostMapping("/update-password")
    public String handlePasswordReset(@ModelAttribute ResetPasswordForm form, Model model) {
        try {
            userService.resetPassword(
                form.getToken(),
                form.getPassword().trim(),
                form.getConfirmPassword().trim()
            );
            return "password-reset-completed";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "password-reset-failed";
        }
    }
    
}
