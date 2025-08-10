package com.tyro.habit_tracker.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.tyro.habit_tracker.exception.ResourceNotFoundException;
import com.tyro.habit_tracker.misc.AppConfig;
import com.tyro.habit_tracker.model.User;
import com.tyro.habit_tracker.model.VerificationToken;
import com.tyro.habit_tracker.repository.UserRepository;
import com.tyro.habit_tracker.repository.VerificationTokenRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class VerificationTokenService {
	
    private final VerificationTokenRepository tokenRepository;
    private final JavaMailSender mailSender;
    private final AppConfig appConfig;
    private final UserRepository userRepository;
    
	@Value("${spring.mail.username}")
	private String senderEmail;
	
    
    public VerificationToken createVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        
        if(tokenRepository.existsByUser(user)) {
        	tokenRepository.delete(tokenRepository.findByUser(user).get());
        }

        VerificationToken verificationToken = VerificationToken
        		.builder()
        		.token(token)
        		.user(user)
        		.build();
        
        return tokenRepository.save(verificationToken);
    }
    
    public VerificationToken validateToken(String token) {
    	  
        return tokenRepository.findByToken(token)
                .filter(t -> !t.isExpired())
                .orElse(null);
    }
    
    public boolean verifyToken(String email, String token) {
    	
        if (!userRepository.existsByEmail(email)) {
            return false;
        }

        VerificationToken verificationToken = validateToken(token);
        if (verificationToken == null) {
            return false;
        }

        User tokenUser = userRepository.findByEmail(email).orElseThrow();
        tokenUser.setVerified(true);
        userRepository.save(tokenUser);
        return true;
    }
    
    public void sendHtmlEmail(String toEmail, String token) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(senderEmail);
            helper.setTo(toEmail);
            helper.setSubject("Verification Email");

            String link = appConfig.getBaseUrl() 
            	    + "/verification.html?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8) 
            	    + "&email=" + URLEncoder.encode(toEmail, StandardCharsets.UTF_8);
            
            String htmlContent = "<p>Click the link below to verify your account:</p>"
                               + "<p><a href=\"" + link + "\">Verify Account</a></p>";

            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
    
    public void sendResetPasswordEmail(String toEmail) {
    	
    	 VerificationToken token = createVerificationToken(userRepository.findByEmail(toEmail).orElseThrow(()-> new ResourceNotFoundException("User not found")));
    	
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(senderEmail);
            helper.setTo(toEmail);
            helper.setSubject("Verification Email");

            String link = appConfig.getBaseUrl() 
            	    + "/reset-password?token=" + URLEncoder.encode(token.getToken(), StandardCharsets.UTF_8);
            
            String htmlContent = "<p>Click the link below to reset your password:</p>"
                               + "<p><a href=\"" + link + "\">Reset password</a></p>";

            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
    


}
