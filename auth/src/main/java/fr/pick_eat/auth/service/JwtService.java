package fr.pick_eat.auth.service;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class JwtService {

    private final JwtDecoder decoder;

    private final JwtEncoder encoder;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    public JwtService(JwtDecoder jwtDecoder, JwtEncoder jwtEncoder) {
        this.decoder = jwtDecoder;
        this.encoder = jwtEncoder;
    }

    public String generateToken(UserDetails userDetails, UUID uuid, String scope) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("pick-eat")
                .issuedAt(now)
                .expiresAt(now.plus(jwtExpiration, ChronoUnit.MILLIS))
                .subject(userDetails.getUsername())
                .claim("scope", scope)
                .claim("uuid", uuid.toString())
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public Cookie generateCookie(String token) {
        Cookie cookie = new Cookie("jwt", token);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) jwtExpiration);
        cookie.setPath("/");
        return cookie;
    }

    public Cookie generateDeleteCookie() {
        Cookie cookie = new Cookie("jwt", "null");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
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

    public int getExpiration() {
        return (int) jwtExpiration;
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
}