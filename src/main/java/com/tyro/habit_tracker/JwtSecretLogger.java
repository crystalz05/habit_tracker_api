package com.tyro.habit_tracker;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class JwtSecretLogger implements CommandLineRunner {

    @Value("${jwt.secret}")
    private String jwtSecret;
    
	@Value("${jwt.expiration}")
	private Long jwtExpiration;
    

    @Override
    public void run(String... args) throws Exception {
        System.out.println("JWT_SECRET: " + jwtSecret);
        System.out.println("JWT_EXPIRATION: " + jwtExpiration);
        
    }
}
