package fr.pick_eat.event.repository;

import fr.pick_eat.event.entity.EventChatModel;
import fr.pick_eat.event.entity.EventFeedbackModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventChatRepository extends CrudRepository<EventChatModel, UUID> {
    List<EventChatModel> findAllByEventId(UUID eventId);
}