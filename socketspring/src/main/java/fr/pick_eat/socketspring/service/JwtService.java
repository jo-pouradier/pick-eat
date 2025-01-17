package fr.pick_eat.socketspring.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Service
public class JwtService {

    private final JwtDecoder decoder;

    public JwtService(JwtDecoder jwtDecoder) {
        this.decoder = jwtDecoder;
    }

    public String extractUsername(String token) {
        try {
            return this.decoder.decode(token).getSubject();
        } catch (JwtValidationException e) {
            // Handle the exception (e.g., log it, return a specific error message, etc.)
            throw new RuntimeException("JWT token is expired or invalid", e);
        }
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (JwtValidationException e) {
            // Handle the exception (e.g., log it, return false, etc.)
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            Instant expiration = this.decoder.decode(token).getExpiresAt();
            return expiration.isBefore(Instant.now());
        } catch (JwtValidationException e) {
            // Handle the exception (e.g., log it, return true, etc.)
            return true;
        }
    }

    public String extractRoles(String jwt) {
        try {
            return this.decoder.decode(jwt).getClaimAsString("scope");
        } catch (JwtValidationException e) {
            // Handle the exception (e.g., log it, return null, etc.)
            return null;
        }
    }

    public UUID extractUuid(String jwt) {
        try {
            return UUID.fromString(this.decoder.decode(jwt).getClaimAsString("uuid"));
        } catch (JwtValidationException e) {
            // Handle the exception (e.g., log it, return null, etc.)
            return null;
        }
    }

    public long getExpirationTime(String jwt) {
        return Objects.requireNonNull(this.decoder.decode(jwt).getExpiresAt()).toEpochMilli();
    }
}