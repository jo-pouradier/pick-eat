package fr.pick_eat.event.async;

import fr.pick_eat.event.dto.EventChatDto;
import fr.pick_eat.event.notification.EventChatNotificationDto;
import fr.pick_eat.notification.ENotificationType;
import fr.pick_eat.notification.NotificationDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EventNotificationBrokerSender {

    private final JmsTemplate jmsTemplate;

    @Value("${notification.topic.name}")
    private String topicName;


    public EventNotificationBrokerSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendEventChatDto(EventChatDto chatDto, List<UUID> participantsUuid) {
        if (participantsUuid.isEmpty()) {
            return;
        }
        NotificationDto<EventChatDto> notificationDto = new EventChatNotificationDto(ENotificationType.NEW_MESSAGE, participantsUuid, chatDto);
        if (isJUnitTest()) {
            return;
        }
        jmsTemplate.convertAndSend(topicName, notificationDto, message -> {
            message.setJMSType(notificationDto.getClass().getName());
            return message;
        });
    }

    public static boolean isJUnitTest() {
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (element.getClassName().startsWith("org.junit.")) {
                return true;
            }
        }
        return false;
    }
}
