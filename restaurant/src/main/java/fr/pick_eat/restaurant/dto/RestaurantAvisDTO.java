package fr.pick_eat.restaurant.dto;

import fr.pick_eat.restaurant.entity.RestaurantAvisModel;
import jakarta.persistence.Id;

import java.util.UUID;

public class RestaurantAvisDTO {
    @Id
    private UUID id;
    private UUID restaurantId;
    private String comment;
    private UUID userId;

    public UUID getId() {return id;}

    public void setId(UUID id) {this.id = id;}

    public UUID getRestaurantId() {return restaurantId;}

    public void setRestaurantId(UUID restaurantId) {this.restaurantId = restaurantId;}

    public String getComment() {return comment;}

    public void setComment(String comment) {this.comment = comment;}

    public UUID getUserId() {return userId;}

    public void setUserId(UUID userId) {this.userId = userId;}

    public static RestaurantAvisDTO fromEntity(RestaurantAvisModel entity) {
        RestaurantAvisDTO dto = new RestaurantAvisDTO();
        dto.setId(entity.getId());
        dto.setRestaurantId(entity.getRestaurantId());
        dto.setComment(entity.getComment());
        dto.setUserId(entity.getUserId());
        return dto;
    }
}
