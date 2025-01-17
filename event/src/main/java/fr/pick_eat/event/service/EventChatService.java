package fr.pick_eat.event.service;

import fr.pick_eat.event.entity.EventChatModel;
import fr.pick_eat.event.repository.EventChatRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EventChatService {

    private final EventChatRepository eventChatRepository;

    public EventChatService(EventChatRepository eventChatRepository) {
        this.eventChatRepository = eventChatRepository;
    }

    public EventChatModel send_message(UUID eventId, UUID userUuid, String message, String filePath) {
        EventChatModel eventChat = EventChatModel.builder().eventId(eventId).userId(userUuid).content(message).imagePath(filePath).build();
        eventChatRepository.save(eventChat);
        return eventChat;
    }

    public List<EventChatModel> get_messages(UUID eventId) {
        return eventChatRepository.findAllByEventId(eventId);
    }
}
