package fr.pick_eat.auth.controller;

import fr.pick_eat.auth.dto.UserBasicDto;
import fr.pick_eat.auth.entity.UserBasic;
import fr.pick_eat.auth.mapper.UserMapper;
import fr.pick_eat.auth.service.AuthenticationService;
import fr.pick_eat.auth.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping(value = "/users")
public class UsersController {

    private final AuthenticationService authenticationService;

    public UsersController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserBasicDto> getUserById(@PathVariable(value = "userId") String userId) {
        UUID uuid;
        try {
            uuid = UUID.fromString(userId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        UserBasic user;
        try {
            user = authenticationService.getUserById(uuid);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    @PostMapping("/")
    public ResponseEntity<List<UserBasicDto>> getUsersByIds(@RequestBody List<String> uuids) {
        List<UUID> uuidList;
        try {
            uuidList = uuids.stream().map(UUID::fromString).toList();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        List<UserBasic> users;
        try {
            users = uuidList.stream().map(authenticationService::getUserById).toList();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users.stream().map(UserMapper::toDto).toList());
    }
}