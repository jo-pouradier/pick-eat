package fr.pick_eat.restaurant.repository;

import fr.pick_eat.restaurant.entity.RestaurantNoteModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RestaurantNoteRepository extends CrudRepository<RestaurantNoteModel, UUID> {}

