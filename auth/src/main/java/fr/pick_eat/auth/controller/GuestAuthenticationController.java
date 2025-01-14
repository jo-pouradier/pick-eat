package fr.pick_eat.auth.controller;

import fr.pick_eat.auth.dto.GuestUserDto;
import fr.pick_eat.auth.scope.EScope;
import fr.pick_eat.auth.service.AuthenticationService;
import fr.pick_eat.auth.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(value = "/guest")
public class GuestAuthenticationController {
    private final JwtService jwtService;

    public GuestAuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateGuest(@RequestBody GuestUserDto guestUserDto, HttpServletResponse response) {
        UserDetails userDetails = User.withUsername(guestUserDto.getFirstName().toLowerCase() + "." + guestUserDto.getLastName().toLowerCase() + "@guest.com").password("").build();
        UUID guestUuid = guestUserDto.getUuid() != null ? guestUserDto.getUuid() : UUID.randomUUID();
        String jwtToken = jwtService.generateToken(userDetails, guestUuid, EScope.GUEST.getScope());
        response.addCookie(jwtService.generateDeleteCookie());
        response.addCookie(jwtService.generateCookie(jwtToken));
        return ResponseEntity.ok(jwtToken);
    }
}