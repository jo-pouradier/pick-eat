package fr.pick_eat.restaurant.repository;

import fr.pick_eat.restaurant.entity.RestaurantAvisModel;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface RestaurantAvisRepository extends CrudRepository<RestaurantAvisModel, UUID> {}

