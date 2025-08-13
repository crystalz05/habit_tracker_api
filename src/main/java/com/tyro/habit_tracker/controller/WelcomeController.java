package com.tyro.habit_tracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tyro.habit_tracker.misc.AppConfig;

@RestController
public class WelcomeController {
	
	@Autowired
	private AppConfig appConfig;
	
	@GetMapping("/")
	public String welcomePage() {
		
		String linkAddress = appConfig.getBaseUrl()+"/swagger-ui/index.html#/"; 
		return "Welcome to the habit tracker api, <a href="+linkAddress+">click here to continue to Swagger documentation</a>";
	}

}
