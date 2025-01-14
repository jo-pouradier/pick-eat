package fr.pick_eat.restaurant.repository;

import fr.pick_eat.restaurant.entity.RestaurantModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RestaurantRepository extends CrudRepository<RestaurantModel, UUID> {


    @Query(value="SELECT r FROM RestaurantModel r WHERE r.lat BETWEEN :minLat AND :maxLat AND r.lon BETWEEN :minLon AND :maxLon", nativeQuery = false)
    List<RestaurantModel> findBetweenLatAndLon(@Param("minLat") float minLat,
                                               @Param("maxLat")float maxLat,
                                               @Param("minLon")float minLon,
                                               @Param("maxLon")float maxLon);

}

