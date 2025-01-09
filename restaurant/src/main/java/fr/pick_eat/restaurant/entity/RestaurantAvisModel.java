package fr.pick_eat.restaurant.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.Generated;

import java.util.UUID;

@Entity
public class RestaurantAvisModel {
    @Id
    @Generated
    private UUID id;
    private UUID restaurantId;
    private String comment;
    private UUID userId;

    public RestaurantAvisModel() {
        this.id = UUID.randomUUID();
    }

    public RestaurantAvisModel(UUID restaurantId, String comment) {
        this.id = UUID.randomUUID();
        this.restaurantId = restaurantId;
        this.comment = comment;
    }

    public UUID getId() {return id;}

    public void setId(UUID id) {this.id = id;}

    public UUID getRestaurantId() {return restaurantId;}

    public void setRestaurantId(UUID restaurantId) {this.restaurantId = restaurantId;}

    public String getComment() {return comment;}

    public void setComment(String comment) {this.comment = comment;}

    public UUID getUserId() {return userId;}

    public void setUserId(UUID userId) {this.userId = userId;}
}
