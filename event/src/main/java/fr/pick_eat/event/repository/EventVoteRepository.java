package fr.pick_eat.event.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.pick_eat.event.entity.EventVoteModel;

@Repository
public interface  EventVoteRepository extends CrudRepository<EventVoteModel, UUID> {

    @Query("SELECT ev FROM EventVoteModel ev WHERE ev.event.id = :eventId")
    List<EventVoteModel> getVotesByEventId(@Param("eventId") UUID eventId);


}
