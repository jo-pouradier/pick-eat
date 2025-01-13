package fr.pick_eat.event.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.pick_eat.event.entity.EventVoteModel;

@Repository
public interface  EventVoteRepository extends CrudRepository<EventVoteModel, UUID> {
}
