package fr.pick_eat.restaurant.dto;

import fr.pick_eat.restaurant.entity.RestaurantNoteModel;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.UUID;

public class RestaurantNoteDTO {
    @Id
    private UUID id;
    private UUID restaurantId;
    private Integer note;
    private UUID userId;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(UUID restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Integer getNote() {
        return note;
    }

    public void setNote(Integer note) {
        this.note = note;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public static RestaurantNoteDTO fromEntity(RestaurantNoteModel entity) {
        RestaurantNoteDTO dto = new RestaurantNoteDTO();
        dto.setId(entity.getId());
        dto.setRestaurantId(entity.getRestaurantId());
        dto.setNote(entity.getNote());
        dto.setUserId(entity.getUserId());
        return dto;
    }
}
