package fr.pick_eat.event.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.pick_eat.event.entity.EventParticipantModel;

@Repository
public interface EventParticipantRepository extends CrudRepository<EventParticipantModel, UUID> {

    List<EventParticipantModel> findByUserId(UUID userUuid);

    @Query("SELECT ep FROM EventParticipantModel ep WHERE ep.userId = :userUuid AND ep.event = :eventId")
    boolean deleteByUserIdAndEventId(UUID userUuid, UUID eventId);

    @Query("SELECT ep FROM EventParticipantModel ep WHERE ep.userId = :userUuid AND ep.event.id = :eventId")
    EventParticipantModel findByUserIdAndEventId(@Param("userUuid") UUID userUuid,@Param("eventId") UUID eventId);
}