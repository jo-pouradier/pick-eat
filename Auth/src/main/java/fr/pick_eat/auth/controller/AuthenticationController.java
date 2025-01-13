package fr.pick_eat.auth.controller;

import fr.pick_eat.auth.dto.LoginUserDto;
import fr.pick_eat.auth.dto.RegisterUserDto;
import fr.pick_eat.auth.entity.UserBasic;
import fr.pick_eat.auth.mapper.UserMapper;
import fr.pick_eat.auth.scope.EScope;
import fr.pick_eat.auth.service.AuthenticationService;
import fr.pick_eat.auth.service.JwtService;
import fr.pick_eat.auth.utils.CookiesUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserBasic> register(@RequestBody RegisterUserDto registerUserDto, HttpServletResponse response) {
        UserBasic registeredUser = authenticationService.signup(registerUserDto);
        response.addCookie(jwtService.generateDeleteCookie());
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody LoginUserDto loginUserDto, HttpServletResponse response) {
        UserBasic authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser, authenticatedUser.getUuid(), EScope.USER.getScope());
        response.addCookie(jwtService.generateDeleteCookie());
        response.addCookie(jwtService.generateCookie(jwtToken));
        CookiesUtils.addUserCookie(UserMapper.toDto(authenticatedUser));
        return ResponseEntity.ok(jwtToken);
    }


    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        response.addCookie(jwtService.generateDeleteCookie());
        CookiesUtils.removeUserCookie();
        return ResponseEntity.ok("Logout successful");
    }

    @GetMapping("/adminToken")
    public ResponseEntity<String> adminToken() {
        String jwtToken = jwtService.generateToken(new UserBasic().setEmail("admin@cpe.fr").setPassword("admin"), UUID.randomUUID(), "admin");
        return ResponseEntity.ok(jwtToken);
    }
}