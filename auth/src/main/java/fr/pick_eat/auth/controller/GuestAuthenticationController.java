package fr.pick_eat.auth.controller;

import fr.pick_eat.auth.dto.GuestUserDto;
import fr.pick_eat.auth.dto.UserBasicDto;
import fr.pick_eat.auth.scope.EScope;
import fr.pick_eat.auth.service.JwtService;
import fr.pick_eat.auth.utils.CookiesUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(value = "/guest")
public class GuestAuthenticationController {
    private final JwtService jwtService;

    public GuestAuthenticationController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<UUID> authenticateGuest(@RequestBody GuestUserDto guestUserDto, HttpServletResponse response) {
        UUID guestUuid = guestUserDto.getUuid() != null ? guestUserDto.getUuid() : UUID.randomUUID();
        UserBasicDto userBasicDto = new UserBasicDto()
                .setEmail(guestUuid+"@guest.com")
                .setFirstName(guestUserDto.getFirstName())
                .setLastName(guestUserDto.getLastName())
                .setUuid(guestUuid);
        String jwtToken = jwtService.generateToken(userBasicDto, guestUuid, EScope.GUEST.getScope());
        response.addCookie(jwtService.generateDeleteCookie());
        response.addCookie(jwtService.generateCookie(jwtToken));
        CookiesUtils.addUserCookie(userBasicDto);
        return ResponseEntity.ok(guestUuid);
    }
}