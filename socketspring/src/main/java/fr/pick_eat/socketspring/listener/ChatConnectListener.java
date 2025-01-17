package fr.pick_eat.socketspring.listener;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import fr.pick_eat.auth.scope.EScope;
import fr.pick_eat.socketspring.model.SocketUserModel;
import fr.pick_eat.socketspring.service.JwtService;
import fr.pick_eat.socketspring.service.SocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
@Slf4j
public class ChatConnectListener implements ConnectListener {

    private final JwtService jwtService;
    private final SocketService socketService;

    public ChatConnectListener(@Lazy JwtService jwtService, @Lazy SocketService socketService) {
        this.jwtService = jwtService;
        this.socketService = socketService;
    }

    @Override
    public void onConnect(SocketIOClient client) {
        log.info("New connection from {}", client.getRemoteAddress());
        String cookie = client.getHandshakeData().getHttpHeaders().get("Cookie");
        if (cookie == null || cookie.isBlank()) {
            log.info("Cookie not found");
            client.disconnect();
            return;
        }
        Optional<String> jwt = Arrays.stream(cookie.split(";")).filter(c -> c.trim().startsWith("jwt=")).map(c -> c.split("=")[1]).findFirst();
        if (!jwt.isPresent()) {
            client.disconnect();
            log.info("JWT not found");
            return;
        }
        if (jwtService.isTokenExpired(jwt.get())) {
            client.disconnect();
            log.info("JWT expired");
            return;
        }
        client.set("jwt", jwt.get());
        client.set("username", jwtService.extractUsername(jwt.get()));
        client.set("uuid", jwtService.extractUuid(jwt.get()));
        client.set("scope", Arrays.stream(jwtService.extractRoles(jwt.get()).split(",")).map(EScope::valueOf).toArray(EScope[]::new));
        client.set("expiration", jwtService.getExpirationTime(jwt.get()));
        log.info("Connected user : {} {} {} {}", Optional.ofNullable(client.get("username")).orElse("null"), Optional.ofNullable(client.get("uuid")).orElse("null"), Optional.ofNullable(client.get("scope")).orElse("null"), client.getSessionId().toString());
        try {
            socketService.registerFromJwtToken(client);
        }catch (DataIntegrityViolationException e) {
            log.error("Error while registering client");
        }
    }
}