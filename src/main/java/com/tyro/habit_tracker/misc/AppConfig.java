package com.tyro.habit_tracker.misc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class AppConfig {
	
    @Value("${app.base-url}")
	private String baseUrl;
	
}
