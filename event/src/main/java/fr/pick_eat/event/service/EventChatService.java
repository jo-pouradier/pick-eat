package fr.pick_eat.event.service;

import fr.pick_eat.event.async.EventNotificationBrokerSender;
import fr.pick_eat.event.dto.EChatType;
import fr.pick_eat.event.entity.EventChatModel;
import fr.pick_eat.event.mapper.EventMapper;
import fr.pick_eat.event.repository.EventChatRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EventChatService {

    private final EventChatRepository eventChatRepository;

    private final EventNotificationBrokerSender eventNotificationBrokerSender;

    public EventChatService(EventChatRepository eventChatRepository, EventNotificationBrokerSender eventNotificationBrokerSender) {
        this.eventChatRepository = eventChatRepository;
        this.eventNotificationBrokerSender = eventNotificationBrokerSender;
    }

    private EventChatModel send(UUID eventId, UUID userUuid, String message, String filePath, List<UUID> participantsUuid, EChatType type) {
        EventChatModel eventChat = EventChatModel.builder().eventId(eventId).userId(userUuid).content(message).imagePath(filePath).type(type).build();
        EventChatModel eventSaved = eventChatRepository.save(eventChat);
        eventNotificationBrokerSender.sendEventChatDto(EventMapper.toEventChatDto(eventSaved), participantsUuid);
        return eventChat;
    }


    public EventChatModel sendMessage(UUID eventId, UUID userUuid, String message, String filePath, List<UUID> participantsUuid) {
        return send(eventId, userUuid, message, filePath, participantsUuid, EChatType.user);
    }

    public void sendLog(UUID eventId, UUID userUuid, String message, List<UUID> participantsUuid) {
        send(eventId, userUuid, message, null, participantsUuid, EChatType.log);
    }

public EventChatModel sendResultRestaurant(UUID eventId, UUID restaurantUuid, List<UUID> participantsUuid) {
        EventChatModel eventChat = EventChatModel.builder().eventId(eventId).content(String.valueOf(restaurantUuid)).type(EChatType.resultRestaurant).build();
        eventChat = eventChatRepository.save(eventChat);
        eventNotificationBrokerSender.sendEventChatDto(EventMapper.toEventChatDto(eventChat), participantsUuid);
        return eventChat;
    }

    public List<EventChatModel> getMessages(UUID eventId) {
        return eventChatRepository.findAllByEventId(eventId);
    }
}
