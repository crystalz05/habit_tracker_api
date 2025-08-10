package com.tyro.habit_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class HabitTrackerApplication {

	
	
	public static void main(String[] args) {
		
		System.out.println("DB_URL: " + System.getenv("DB_URL"));

		SpringApplication.run(HabitTrackerApplication.class, args);
	}
}
