package fr.pick_eat.restaurant.repository;

import fr.pick_eat.restaurant.entity.RestaurantModel;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface RestaurantRepository extends CrudRepository<RestaurantModel, UUID> {
}
