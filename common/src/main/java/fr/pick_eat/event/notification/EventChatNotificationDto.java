package fr.pick_eat.event.notification;

import fr.pick_eat.event.dto.EventChatDto;
import fr.pick_eat.notification.ENotificationType;
import fr.pick_eat.notification.NotificationDto;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
public class EventChatNotificationDto extends NotificationDto<EventChatDto> {

    public EventChatNotificationDto(ENotificationType eNotificationType, List<UUID> destination, EventChatDto payload) {
        setType(eNotificationType);
        setDestination(destination);
        setContent(payload);
    }
}
