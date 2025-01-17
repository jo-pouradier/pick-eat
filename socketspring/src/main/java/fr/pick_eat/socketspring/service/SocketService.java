package fr.pick_eat.socketspring.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import fr.pick_eat.notification.NotificationDto;
import fr.pick_eat.socketspring.listener.ChatConnectListener;
import fr.pick_eat.socketspring.listener.ChatDisconnectListener;
import fr.pick_eat.socketspring.model.SocketUserModel;
import fr.pick_eat.socketspring.repository.SocketRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class SocketService {

    private final SocketIOServer socketIOServer;
    private final ChatConnectListener connectListener;
    private final ChatDisconnectListener disconnectListener;
    private final JwtService jwtService;
    private final SocketRepository socketRepository;

    public SocketService(SocketIOServer socketIOServer, ChatConnectListener connectListener, ChatDisconnectListener disconnectListener, JwtService jwtService, SocketRepository socketRepository) {
        this.socketIOServer = socketIOServer;
        this.connectListener = connectListener;
        this.disconnectListener = disconnectListener;
        this.jwtService = jwtService;
        this.socketRepository = socketRepository;
    }

    public void sendNotification(NotificationDto<?> notificationDto) {
        List<SocketIOClient> clients = this.getClients(notificationDto.getDestination());
        System.out.println("Sending notification to " + clients.size() + " clients");
        clients.forEach(client -> {
            client.sendEvent(notificationDto.getType().getEvent(), notificationDto);
            System.out.println("Notification sent to " + client.getSessionId());
        });
    }

    public List<SocketIOClient> getClients(List<UUID> userIds) {
        return socketRepository.getSocketUserModelByUserIdIn((userIds)).parallelStream()
                .map(socketUserModel -> socketIOServer.getClient(socketUserModel.getSocketId())).filter(Objects::nonNull).toList();
    }

    @PostConstruct
    private void init() {
        socketIOServer.addConnectListener(connectListener);
        socketIOServer.addDisconnectListener(disconnectListener);
        socketIOServer.addEventInterceptor((client, data, ackSender, eventName) -> {
            if (client.get("jwt") == null) {
                log.info("Unauthorized client");
                client.disconnect();
                return;
            }
            if (jwtService.isTokenExpired(client.get("jwt"))) {
                client.disconnect();
                log.info("JWT expired");
            }
        });
    }

    public boolean isSocketConnected(UUID socketId) {
        return socketRepository.existsById(socketId);
    }

    public void registerFromJwtToken(SocketIOClient client) {
        UUID userId = jwtService.extractUuid(client.get("jwt"));
        String username = jwtService.extractUsername(client.get("jwt"));
        SocketUserModel userModel = SocketUserModel.builder().socketId(client.getSessionId()).userId(userId).username(username).build();
        socketRepository.save(userModel);
    }

    public void unregister(SocketIOClient client) {
        try {
            socketRepository.deleteBySocketId(client.getSessionId());
        } catch (Exception e) {
            log.error("Error while deleting socket user model");
        }
    }
}