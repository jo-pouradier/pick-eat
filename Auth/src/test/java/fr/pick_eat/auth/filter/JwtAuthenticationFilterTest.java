package fr.pick_eat.auth.filter;

import fr.pick_eat.auth.service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.ResultActions;

import java.io.IOException;
import java.util.UUID;

@SpringBootTest
class JwtAuthenticationFilterTest {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtService jwtService;

    private UserDetails userDetails1 = User.withUsername("test").password("test").build();
    private UUID user1UUID = UUID.randomUUID();

    @Test
    void doFilterInternal() throws ServletException, IOException {
        MockFilterChain mockChain = new MockFilterChain();
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse rsp = new MockHttpServletResponse();
        req.setCookies(new Cookie("jwt", jwtService.generateToken(userDetails1, user1UUID, "test")));
        jwtAuthenticationFilter.doFilterInternal(req, rsp, mockChain);
    }
}