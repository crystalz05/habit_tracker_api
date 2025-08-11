package com.tyro.habit_tracker.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;


@Component
public class JwtUtil {
	
	@Value("${jwt.secret}")
	private String jwtSecret;
	
	@Value("${jwt.expiration}")
	private Long jwtExpiration;
	
    private SecretKey secretKey;
    private JwtParser parser;
    
    @PostConstruct
    private void init() {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        this.parser = Jwts.parser().verifyWith(secretKey).build();
    }

	public String generateToken(String username, Map<String, Object> extraClaims) {
		
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpiration);
        
        return Jwts.builder()
        		.claims(extraClaims)
        		.subject(username)
        		.issuedAt(now)
        		.expiration(expiryDate)
        		.signWith(secretKey, Jwts.SIG.HS256)
        		.compact();
	}
	
	public String extractUsername(String token) {        
		return parser.parseSignedClaims(token).getPayload().getSubject();
	}
	
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = parser.parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }
    
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    public boolean isTokenValid(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }
    

	
	
}
