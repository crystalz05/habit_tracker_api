package com.tyro.habit_tracker.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tyro.habit_tracker.model.User;
import com.tyro.habit_tracker.repository.UserRepository;

import lombok.AllArgsConstructor;


@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	private UserRepository userRepository;
	

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with email: "+email));	

		return new CustomUserDetails(user);
	}

}
