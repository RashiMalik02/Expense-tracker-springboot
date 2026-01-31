package com.expensetracker.expense_tracker.security;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtil {
    private static final String SECRET_KEY = "Super-secure-long-complex-jwt-key";
    private static final Long EXPIRATION_TIME = 1000 * 60 * 60l;
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generateToken(String email){
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(
                    new Date(System.currentTimeMillis() + EXPIRATION_TIME)
                )
                .signWith(key)
                .compact();
    }

    public String extractEmail(String token){
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            if (claims.getExpiration().before(new Date())) {
                log.warn("JWT token has expired");
                return null;
            }
            
            return claims.getSubject();
        } catch (Exception e) {
            log.error("Error extracting email from JWT token: {}", e.getMessage());
            return null;
        }
    }
}
