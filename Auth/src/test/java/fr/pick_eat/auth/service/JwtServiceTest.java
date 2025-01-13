package fr.pick_eat.auth.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

@SpringBootTest
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    private UserDetails userDetails1 = User.withUsername("test").password("test").build();
    private UUID user1UUID = UUID.randomUUID();
    private UserDetails userDetails2 = User.withUsername("test2").password("test").build();
    private UUID user2UUID = UUID.randomUUID();

    @Test
    void generateToken() {
        jwtService.generateToken(userDetails1, user1UUID, "test");
    }

    @Test
    void extractUsername() {
        String token = jwtService.generateToken(userDetails1, user1UUID, "test");
        assert jwtService.extractUsername(token).equals("test");
    }

    @Test
    void validateToken() {
        String token = jwtService.generateToken(userDetails1, user1UUID, "test");
        assert jwtService.validateToken(token, userDetails1);
        token = jwtService.generateToken(userDetails2, user2UUID, "test");
        assert !jwtService.validateToken(token, userDetails1);
    }
}