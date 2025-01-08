package fr.pick_eat.restaurant.repository;

import fr.pick_eat.restaurant.entity.RestaurantNoteModel;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface RestaurantNoteRepository extends CrudRepository<RestaurantNoteModel, UUID> {}

