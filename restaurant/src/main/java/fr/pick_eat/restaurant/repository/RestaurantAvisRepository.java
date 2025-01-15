package fr.pick_eat.restaurant.repository;

import fr.pick_eat.restaurant.entity.RestaurantAvisModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RestaurantAvisRepository extends CrudRepository<RestaurantAvisModel, UUID> {

    @Query(value="SELECT ra FROM RestaurantAvisModel ra WHERE ra.restaurantId = :restaurantId", nativeQuery = false)
    List<RestaurantAvisModel> findByRestaurantId(@Param("restaurantId") UUID restaurantId);
}

