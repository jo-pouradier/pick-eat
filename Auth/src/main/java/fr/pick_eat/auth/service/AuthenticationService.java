package fr.pick_eat.auth.service;

import fr.pick_eat.auth.dto.LoginUserDto;
import fr.pick_eat.auth.dto.RegisterUserDto;
import fr.pick_eat.auth.entity.UserBasic;
import fr.pick_eat.auth.repository.UserRepository;
import fr.pick_eat.auth.scope.EScope;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public AuthenticationService(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UserBasic signup(RegisterUserDto input) {
        UserBasic user = new UserBasic()
                .setFirstName(input.getFirstName())
                .setLastName(input.getLastName())
                .setEmail(input.getEmail())
                .setPassword(passwordEncoder.encode(input.getPassword()))
                .setRoles(EScope.USER.getScope());
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }
    }

    public UserBasic signupGitHub(RegisterUserDto input, Long githubId) {
        UserBasic user = new UserBasic()
                .setFirstName(input.getFirstName())
                .setLastName(input.getLastName())
                .setEmail(input.getEmail())
                .setPassword(passwordEncoder.encode(input.getPassword()))
                .setRoles(EScope.USER.getScope())
                .setGithubId(githubId);
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }
    }


    public UserBasic authenticate(LoginUserDto input) throws ResponseStatusException {
        Optional<UserBasic> userOptional = userRepository.findByEmail(input.getEmail());
        if (userOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        UserBasic user = userOptional.get();
        if (user.isGitHubUser()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword()));

        return userRepository.findByEmail(input.getEmail()).orElseThrow();
    }

    public boolean isRegistered(String email) {
        return !userRepository.findByEmail(email).isEmpty();
    }

    public Optional<UserBasic> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserBasic getAuthenticatedUser(String bearerToken) {
        String token = bearerToken.substring(7); // Remove "Bearer " prefix
        String username = jwtService.extractUsername(token);
        Optional<UserBasic> user = userRepository.findByEmail(username);
        return userRepository.findByEmail(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }
}