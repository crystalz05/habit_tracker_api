package com.tyro.habit_tracker.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tyro.habit_tracker.dto.JwtResponse;
import com.tyro.habit_tracker.dto.LoginRequest;
import com.tyro.habit_tracker.dto.UserDTO;
import com.tyro.habit_tracker.dto.UserResponseDTO;
import com.tyro.habit_tracker.model.User;
import com.tyro.habit_tracker.repository.UserRepository;
import com.tyro.habit_tracker.security.CustomUserDetails;
import com.tyro.habit_tracker.security.JwtUtil;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    private void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
    }
    
    public UserResponseDTO getUserByUsername(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        
        UserResponseDTO userResponseDTO = UserResponseDTO.builder()
        		.id(user.getId())
        		.email(user.getEmail())
        		.fullName(user.getFullName())
        		.username(user.getUsername())
        		.createdAt(user.getCreatedAt())
        		.verified(user.isVerified())
        		.build();
        
        return userResponseDTO;
    }

    @Transactional
    public User registerUser(UserDTO userDTO) {
        if (userDTO == null || userDTO.getEmail() == null || userDTO.getUsername() == null || userDTO.getPassword() == null) {
            throw new IllegalArgumentException("Invalid input data");
        }

        String email = userDTO.getEmail().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            logger.warn("Registration failed: Email {} is already in use", email);
            throw new IllegalArgumentException("Registration failed");
        }

        if (userRepository.existsByUsername(userDTO.getUsername())) {
            logger.warn("Registration failed: Username {} is already in use", userDTO.getUsername());
            throw new IllegalArgumentException("Registration failed");
        }

        validatePassword(userDTO.getPassword());

        User user = User.builder()
                .username(userDTO.getUsername())
                .email(email)
                .fullName(userDTO.getFullName())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .build();

        return userRepository.save(user);
    }

    
    public JwtResponse authenticate(LoginRequest loginRequest) {
        if (loginRequest == null || loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
            throw new IllegalArgumentException("Invalid login credentials");
        }

        User user = userRepository.findByEmail(loginRequest.getEmail().toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.isVerified()) {
            throw new IllegalStateException("User account is not verified");
        }

        logger.debug("Attempting authentication for user: {}", loginRequest.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        if (!(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new IllegalStateException("Unexpected principal type");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Map<String, Object> extraClaims = new HashMap<>();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        extraClaims.put("roles", roles);

        String jwt = jwtUtil.generateToken(userDetails.getUsername(), extraClaims);
        return new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities());
    }
    
    public void deleteUserById(Long userId) {
    	if(userRepository.existsById(userId)) {
        	userRepository.deleteById(userId);
    	}else {
    		throw new UsernameNotFoundException("user not found");
    	}
    }
}