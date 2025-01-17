package fr.pick_eat.socketspring.listener;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DisconnectListener;
import fr.pick_eat.socketspring.model.SocketUserModel;
import fr.pick_eat.socketspring.service.SocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ChatDisconnectListener implements DisconnectListener {

    private final SocketService socketService;

    public ChatDisconnectListener(@Lazy SocketService socketService) {
        this.socketService = socketService;
    }


    @Override
    public void onDisconnect(SocketIOClient client) {
        if (client.get("jwt") != null) {
            log.info("User disconnected : {}", client.getSessionId().toString());
        }
        socketService.unregister(client);

    }
}