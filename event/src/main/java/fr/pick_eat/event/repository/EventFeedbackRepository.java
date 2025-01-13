package fr.pick_eat.event.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.pick_eat.event.entity.EventFeedbackModel;

@Repository
public interface EventFeedbackRepository extends CrudRepository<EventFeedbackModel, UUID> {

    EventFeedbackModel findByParticipantIdAndEventId(UUID id, UUID id2);
}