package fr.pick_eat.restaurant.repository;

import fr.pick_eat.restaurant.entity.RestaurantAvisModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RestaurantAvisRepository extends CrudRepository<RestaurantAvisModel, UUID> {}

