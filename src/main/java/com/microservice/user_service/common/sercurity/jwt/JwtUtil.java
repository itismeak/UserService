package com.microservice.user_service.common.sercurity.jwt;

import com.microservice.user_service.common.constants.AppConstant;
import com.microservice.user_service.modules.user.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
    @Value("${jwt.secret}")
    private String jwtSecret;
    // Define token expiration time constant (e.g., 15 minutes)
    private static final long JWT_EXPIRATION_TIME_MINUTES = 60*24;  // Expiration time in minutes
    private static final long JWT_EXPIRATION_MS = JWT_EXPIRATION_TIME_MINUTES * 60 * 1000;  // Convert minutes to milliseconds
    private Key key() {
        // Use the raw secret bytes; ensure secret is long enough.
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(User user) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + JWT_EXPIRATION_MS);

        String token = Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .claim("name",user.getName())
                .claim("role",user.getRole())
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();

        log.info("Generated JWT for email={} exp={}", user.getEmail(), exp);
        return token;
    }

    public String extractEmail(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (JwtException e) {
            log.warn("Cannot extract email from token: {}", e.getMessage());
            throw e;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("JWT expired: {}", e.getMessage());
        } catch (UnsupportedJwtException | MalformedJwtException | SecurityException | IllegalArgumentException e) {
            log.warn("Invalid JWT: {}", e.getMessage());
        }
        return false;
    }
}
