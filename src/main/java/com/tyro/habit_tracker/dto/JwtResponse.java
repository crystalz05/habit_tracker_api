package com.tyro.habit_tracker.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;

@Data
public class JwtResponse {
	
	private String token;
	private String username;
	private Collection<? extends GrantedAuthority> roles;
	
	public JwtResponse(String token, String username, Collection<? extends GrantedAuthority> roles) {
		this.token = token;
		this.username = username;
		this.roles = roles;
	}
	
	public JwtResponse(String token) {
		this.token = token;
	}
	
	

}
