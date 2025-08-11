package com.tyro.habit_tracker.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyro.habit_tracker.dto.ResetPasswordForm;
import com.tyro.habit_tracker.service.UserService;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class passwordResetController {

	private final UserService userService;
	
    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token) {
        return "redirect:/reset-password-form.html?token=" + token;
    }
    

    @Hidden
    @PostMapping("/update-password")
    @ResponseBody
    public Map<String, String> handlePasswordReset(@ModelAttribute ResetPasswordForm form) {
        Map<String, String> response = new HashMap<String, String>();
        try {
            userService.resetPassword(
                form.getToken(),
                form.getPassword().trim(),
                form.getConfirmPassword().trim()
            );
            response.put("status", "success");
            response.put("message", "Password reset completed.");
        } catch (IllegalArgumentException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        return response;
    }
    
}
