package fr.pick_eat.event.async;

import fr.pick_eat.event.dto.EventChatDto;
import fr.pick_eat.event.notification.EventChatNotificationDto;
import fr.pick_eat.notification.ENotificationType;
import fr.pick_eat.notification.NotificationDto;
import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DestinationResolver;
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

        this.jmsTemplate.setDestinationResolver(new CustomDestinationResolver());
    }

    public void sendEventChatDto(EventChatDto chatDto, List<UUID> participantsUuid) {
        NotificationDto<EventChatDto> notificationDto = new EventChatNotificationDto(ENotificationType.NEW_MESSAGE, participantsUuid, chatDto);
        jmsTemplate.convertAndSend(topicName, notificationDto, message -> {
            message.setJMSType(notificationDto.getClass().getName());
            return message;
        });
    }

    class CustomDestinationResolver implements DestinationResolver {
        public Destination resolveDestinationName(Session session, String destinationName, boolean pubSubDomain) throws JMSException {
            return session.createTopic(destinationName);
        }
    }
}
