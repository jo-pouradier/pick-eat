package fr.pick_eat.socketspring.async;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.pick_eat.notification.NotificationDto;
import fr.pick_eat.socketspring.service.SocketService;
import jakarta.jms.JMSException;
import jakarta.jms.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationBrokerReceiver {
    private final SocketService socketService;
    private final ObjectMapper objectMapper;

    public NotificationBrokerReceiver(SocketService socketService, ObjectMapper objectMapper) {
        this.socketService = socketService;
        this.objectMapper = objectMapper;
    }

    @JmsListener(destination = "${notification.topic.name}", containerFactory = "topicConnectionFactory")
    public void receiveMessage(TextMessage message) {
        System.out.println("Received message: " + message);
        try {
            String clazz = message.getJMSType();
            NotificationDto<?> notificationDtoAbstract = (NotificationDto<?>) objectMapper.readValue(message.getText(), Class.forName(clazz));
            System.out.println("Received message: " + notificationDtoAbstract);
            socketService.sendNotification(notificationDtoAbstract);
        } catch (JMSException | ClassNotFoundException | JsonProcessingException e) {
            log.error("Error while receiving message", e);
        }
    }

}
